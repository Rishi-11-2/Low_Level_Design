package service;

import model.Booking;
import model.BookingStatus;
import model.BookingStateHandler;
import model.TransactionStatus;
import repository.BookingRepository;
import java.util.List;
import java.util.stream.Collectors;

public class SchedulerService {
    private final BookingRepository bookingRepository;

    public SchedulerService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public synchronized void processExpiredHolds() {
        long now = System.currentTimeMillis();
        
        List<Booking> expired = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.HELD && b.getHoldExpiresAt() < now)
                .collect(Collectors.toList());

        if (expired.isEmpty()) {
            return;
        }

        System.out.println("\n>>> [SchedulerService] Found " + expired.size() + " expired booking holds. Releasing holds...");
        for (Booking booking : expired) {
            booking.setPaymentStatus(TransactionStatus.FAILED);
            BookingStateHandler.transition(booking, BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            System.out.println("[SchedulerService] Hold expired for Booking: " + booking.getId() + ". Inventory restored.");
        }
        System.out.println("<<< [SchedulerService] Hold processing completed.");
    }
}
