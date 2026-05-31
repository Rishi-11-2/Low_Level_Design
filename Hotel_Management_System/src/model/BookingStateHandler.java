package model;

import java.util.Arrays;

public class BookingStateHandler {
    public static boolean canTransition(BookingStatus current, BookingStatus newStatus) {
        switch (current) {
            case CREATED:
                return newStatus == BookingStatus.HELD || newStatus == BookingStatus.CANCELLED;
            case HELD:
                return newStatus == BookingStatus.CONFIRMED || newStatus == BookingStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == BookingStatus.CHECKED_IN || newStatus == BookingStatus.CANCELLED;
            case CHECKED_IN:
                return newStatus == BookingStatus.CHECKED_OUT;
            case CHECKED_OUT:
            case CANCELLED:
            default:
                return false; // Terminal states
        }
    }

    public static void transition(Booking booking, BookingStatus newStatus) {
        BookingStatus current = booking.getBookingStatus();
        if (!canTransition(current, newStatus)) {
            throw new IllegalStateException("Invalid booking state transition: " + current + " -> " + newStatus);
        }
        booking.setBookingStatus(newStatus);
        System.out.println("[State Pattern] Booking " + booking.getId() + " transitioned: " + current + " -> " + newStatus);
    }

    public static void requireStatus(Booking booking, BookingStatus expectedStatus) {
        if (booking.getBookingStatus() != expectedStatus) {
            throw new IllegalStateException("Booking must be in state " + expectedStatus + " but was " + booking.getBookingStatus());
        }
    }

    public static void requireAnyStatus(Booking booking, BookingStatus... allowedStatuses) {
        boolean match = Arrays.stream(allowedStatuses)
                .anyMatch(s -> booking.getBookingStatus() == s);
        if (!match) {
            throw new IllegalStateException("Booking state " + booking.getBookingStatus() + " is not in allowed list: " + Arrays.toString(allowedStatuses));
        }
    }

    public static boolean canCancel(Booking booking) {
        BookingStatus state = booking.getBookingStatus();
        return state == BookingStatus.HELD || state == BookingStatus.CONFIRMED;
    }

    public static boolean canCheckIn(Booking booking) {
        return booking.getBookingStatus() == BookingStatus.CONFIRMED;
    }

    public static boolean canCheckOut(Booking booking) {
        return booking.getBookingStatus() == BookingStatus.CHECKED_IN;
    }

    public static boolean canInitiateTransaction(Booking booking) {
        return booking.getBookingStatus() == BookingStatus.CREATED;
    }

    public static boolean countsInInventory(Booking booking) {
        BookingStatus status = booking.getBookingStatus();
        return status == BookingStatus.HELD || status == BookingStatus.CONFIRMED || status == BookingStatus.CHECKED_IN;
    }
}
