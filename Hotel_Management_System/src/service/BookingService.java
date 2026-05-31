package service;

import model.*;
import repository.BookingRepository;
import repository.HotelRepository;
import repository.RoomRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final InventoryService inventoryService;
    private final PricingService pricingService;
    private final PolicyService policyService;
    private final TransactionService transactionService;
    private final HotelRepository hotelRepository;

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository,
                          InventoryService inventoryService,
                          PricingService pricingService,
                          PolicyService policyService,
                          TransactionService transactionService,
                          HotelRepository hotelRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.inventoryService = inventoryService;
        this.pricingService = pricingService;
        this.policyService = policyService;
        this.transactionService = transactionService;
        this.hotelRepository = hotelRepository;
    }

    public synchronized Booking createBooking(String userId, String hotelId, String roomTypeId,
                                              DateRange range, long expectedTotalPrice) {
        System.out.println("\n>>> [BookingService] Request: Create Booking Hold for user=" + userId + ", hotel=" + hotelId);

        // 1. Pre-check availability
        boolean available = inventoryService.checkAvailability(hotelId, roomTypeId, range, 1);
        if (!available) {
            throw new IllegalStateException("Requested stay dates are not available.");
        }

        // 2. Fetch prices day-by-day and lock nightly pricing
        List<NightlyPrice> nightlyPrices = pricingService.rateStay(hotelId, roomTypeId, range);
        long computedTotal = pricingService.computeTotal(nightlyPrices);

        // 3. Prevent pricing drift
        if (expectedTotalPrice != computedTotal) {
            throw new IllegalArgumentException("Pricing mismatch! Expected: $" + (expectedTotalPrice / 100.0) + ", Computed: $" + (computedTotal / 100.0));
        }

        // 4. Create booking with CREATED status (does not reduce inventory yet)
        String bookingId = "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Booking booking = new Booking(bookingId, userId, hotelId, roomTypeId,
                range.getStartDateUtc(), range.getEndDateUtc(), nightlyPrices, computedTotal);
        
        bookingRepository.save(booking);
        System.out.println("<<< [BookingService] Success: Booking hold created. ID=" + bookingId + ", Locked Total: $" + (computedTotal / 100.0));
        return booking;
    }

    public synchronized void cancelBooking(String bookingId, String userId) {
        System.out.println("\n>>> [BookingService] Request: Cancel Booking ID=" + bookingId + " by user=" + userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Validate ownership
        if (!booking.getUserId().equals(userId)) {
            throw new SecurityException("Unauthorized cancellation request.");
        }

        // Disallow if checked in or out
        BookingStatus status = booking.getBookingStatus();
        if (status == BookingStatus.CHECKED_IN || status == BookingStatus.CHECKED_OUT) {
            throw new IllegalStateException("Cannot cancel booking. Already " + status);
        }

        // Get cancellation policy
        Hotel hotel = hotelRepository.findById(booking.getHotelId())
                .orElseThrow(() -> new IllegalStateException("Hotel not found"));
        
        CancellationPolicy policy = policyService.getPolicy(hotel.getCancellationPolicyId());
        long now = System.currentTimeMillis();
        RefundDecision refundDecision = policyService.evaluateCancellation(booking, policy, now);

        // Transition status to CANCELLED (restores inventory immediately)
        BookingStateHandler.transition(booking, BookingStatus.CANCELLED);

        if (refundDecision.isRefundable() && refundDecision.getRefundAmountMinor() > 0) {
            booking.setPaymentStatus(TransactionStatus.REFUNDED);
            transactionService.issueRefund(bookingId, refundDecision.getRefundAmountMinor());
        } else {
            booking.setPaymentStatus(TransactionStatus.FAILED);
        }

        bookingRepository.save(booking);
        System.out.println("<<< [BookingService] Booking cancelled. Refund decision: " + refundDecision.getMessage());
    }

    public synchronized Booking checkIn(String bookingId, String roomId, long checkInTimeUtc) {
        System.out.println("\n>>> [BookingService] Request: Check-in Guest for Booking ID=" + bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        BookingStateHandler.requireStatus(booking, BookingStatus.CONFIRMED);

        booking.setAllocatedRoomId(roomId);
        booking.setCheckInTimeUtc(checkInTimeUtc);
        BookingStateHandler.transition(booking, BookingStatus.CHECKED_IN);
        
        bookingRepository.save(booking);
        System.out.println("<<< [BookingService] Check-in completed. Assigned Room: " + roomId);
        return booking;
    }

    public synchronized Booking checkOut(String bookingId, long checkOutTimeUtc) {
        System.out.println("\n>>> [BookingService] Request: Check-out Guest for Booking ID=" + bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        BookingStateHandler.requireStatus(booking, BookingStatus.CHECKED_IN);

        booking.setCheckOutTimeUtc(checkOutTimeUtc);
        BookingStateHandler.transition(booking, BookingStatus.CHECKED_OUT); // Releases inventory for remaining nights
        
        bookingRepository.save(booking);
        System.out.println("<<< [BookingService] Check-out completed. Room released.");
        return booking;
    }
}
