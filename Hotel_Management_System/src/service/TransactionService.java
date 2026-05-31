package service;

import model.*;
import repository.BookingRepository;
import repository.TransactionRepository;
import java.util.Optional;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;
    private final InventoryService inventoryService;

    public TransactionService(TransactionRepository transactionRepository,
                              BookingRepository bookingRepository,
                              InventoryService inventoryService) {
        this.transactionRepository = transactionRepository;
        this.bookingRepository = bookingRepository;
        this.inventoryService = inventoryService;
    }

    public synchronized Transaction initiateTransaction(String bookingId) {
        System.out.println("\n>>> [TransactionService] Initiating payment transaction for Booking ID: " + bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        BookingStateHandler.requireStatus(booking, BookingStatus.CREATED);

        // Re-check availability under write lock (prevent double booking)
        DateRange range = new DateRange(booking.getCheckInDateUtc(), booking.getCheckOutDateUtc());
        boolean stillAvailable = inventoryService.checkAvailability(
                booking.getHotelId(), booking.getRoomTypeId(), range, 1
        );
        if (!stillAvailable) {
            throw new IllegalStateException("Room type is no longer available. Releasing booking hold.");
        }

        // Generate mock transaction provider reference
        String providerRef = "TX-REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Transaction transaction = new Transaction(
                "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                bookingId, booking.getTotalAmountMinor(), "USD", providerRef
        );

        // Transition booking CREATED -> HELD (held bookings reduce availability!)
        BookingStateHandler.transition(booking, BookingStatus.HELD);
        long holdTtlMs = 10 * 1000L; // 10 seconds hold expiry for simulation speed
        booking.setHoldExpiresAt(System.currentTimeMillis() + holdTtlMs);

        transactionRepository.save(transaction);
        bookingRepository.save(booking);

        System.out.println("<<< [TransactionService] Transaction created. Provider Ref: " + providerRef + ". Booking is HELD for 10s.");
        return transaction;
    }

    public synchronized void handleCallback(String providerRef, TransactionStatus status) {
        System.out.println("\n>>> [TransactionService] Payment Callback received. Ref: " + providerRef + " status: " + status);
        Transaction transaction = transactionRepository.findByProviderRef(providerRef)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found for provider reference: " + providerRef));

        Booking booking = bookingRepository.findById(transaction.getBookingId())
                .orElseThrow(() -> new IllegalStateException("Booking not found for transaction: " + transaction.getId()));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            System.out.println("[TransactionService] Warning: Duplicate callback received. Ignored.");
            return;
        }

        transaction.setStatus(status);
        booking.setPaymentStatus(status);

        if (status == TransactionStatus.COMPLETED) {
            // Success: HELD -> CONFIRMED
            BookingStateHandler.transition(booking, BookingStatus.CONFIRMED);
            transaction.setCompletedAt(System.currentTimeMillis());
            System.out.println("<<< [TransactionService] Payment SUCCESS. Booking is CONFIRMED.");
        } else {
            // Failure: HELD -> CANCELLED (releases inventory)
            BookingStateHandler.transition(booking, BookingStatus.CANCELLED);
            System.out.println("<<< [TransactionService] Payment FAILED. Booking hold is CANCELLED (Inventory restored).");
        }

        transactionRepository.save(transaction);
        bookingRepository.save(booking);
    }

    public void issueRefund(String bookingId, long amountMinor) {
        System.out.println("[TransactionService] Refund issued successfully for Booking " + bookingId + " of amount $" + (amountMinor / 100.0));
    }
}
