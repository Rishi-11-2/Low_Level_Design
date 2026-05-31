package repository;

import model.Booking;
import model.BookingStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BookingRepository {
    private final Map<String, Booking> bookingMap = new ConcurrentHashMap<>();

    public Booking save(Booking booking) {
        bookingMap.put(booking.getId(), booking);
        return booking;
    }

    public Optional<Booking> findById(String bookingId) {
        return Optional.ofNullable(bookingMap.get(bookingId));
    }

    public List<Booking> findByUser(String userId) {
        return bookingMap.values().stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Booking> findAll() {
        return new ArrayList<>(bookingMap.values());
    }

    private long truncateToMidnight(long ms) {
        return (ms / (24 * 60 * 60 * 1000L)) * (24 * 60 * 60 * 1000L);
    }

    private boolean isDateInStayRange(Booking booking, long dateUtc) {
        long targetDay = truncateToMidnight(dateUtc);
        long startDay = truncateToMidnight(booking.getCheckInDateUtc());
        long endDay = truncateToMidnight(booking.getCheckOutDateUtc());
        return targetDay >= startDay && targetDay < endDay;
    }

    public int countConfirmedBookings(String hotelId, String roomTypeId, long dateUtc) {
        return (int) bookingMap.values().stream()
                .filter(b -> b.getHotelId().equals(hotelId) && b.getRoomTypeId().equals(roomTypeId))
                .filter(b -> b.getBookingStatus() == BookingStatus.CONFIRMED)
                .filter(b -> isDateInStayRange(b, dateUtc))
                .count();
    }

    public int countHeldBookings(String hotelId, String roomTypeId, long dateUtc, long nowUtc) {
        return (int) bookingMap.values().stream()
                .filter(b -> b.getHotelId().equals(hotelId) && b.getRoomTypeId().equals(roomTypeId))
                .filter(b -> b.getBookingStatus() == BookingStatus.HELD && b.getHoldExpiresAt() > nowUtc)
                .filter(b -> isDateInStayRange(b, dateUtc))
                .count();
    }

    public int countCheckedInBookings(String hotelId, String roomTypeId, long dateUtc) {
        return (int) bookingMap.values().stream()
                .filter(b -> b.getHotelId().equals(hotelId) && b.getRoomTypeId().equals(roomTypeId))
                .filter(b -> b.getBookingStatus() == BookingStatus.CHECKED_IN)
                .filter(b -> isDateInStayRange(b, dateUtc))
                .count();
    }
}
