package model;

import java.util.ArrayList;
import java.util.List;

public class Booking {
    private final String id;
    private final String userId;
    private final String hotelId;
    private final String roomTypeId;
    private final long checkInDateUtc;
    private final long checkOutDateUtc;
    private final List<NightlyPrice> nightlyPrices;
    private final long totalAmountMinor;
    private BookingStatus bookingStatus;
    private TransactionStatus paymentStatus;
    private String allocatedRoomId;
    private Long checkInTimeUtc;
    private Long checkOutTimeUtc;
    private long holdExpiresAt;
    private final long createdAt;

    public Booking(String id, String userId, String hotelId, String roomTypeId,
                   long checkInDateUtc, long checkOutDateUtc, List<NightlyPrice> nightlyPrices,
                   long totalAmountMinor) {
        this.id = id;
        this.userId = userId;
        this.hotelId = hotelId;
        this.roomTypeId = roomTypeId;
        this.checkInDateUtc = checkInDateUtc;
        this.checkOutDateUtc = checkOutDateUtc;
        this.nightlyPrices = new ArrayList<>(nightlyPrices);
        this.totalAmountMinor = totalAmountMinor;
        this.bookingStatus = BookingStatus.CREATED;
        this.paymentStatus = TransactionStatus.PENDING;
        this.allocatedRoomId = null;
        this.checkInTimeUtc = null;
        this.checkOutTimeUtc = null;
        this.holdExpiresAt = 0;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public long getCheckInDateUtc() {
        return checkInDateUtc;
    }

    public long getCheckOutDateUtc() {
        return checkOutDateUtc;
    }

    public List<NightlyPrice> getNightlyPrices() {
        return nightlyPrices;
    }

    public long getTotalAmountMinor() {
        return totalAmountMinor;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public TransactionStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(TransactionStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAllocatedRoomId() {
        return allocatedRoomId;
    }

    public void setAllocatedRoomId(String allocatedRoomId) {
        this.allocatedRoomId = allocatedRoomId;
    }

    public Long getCheckInTimeUtc() {
        return checkInTimeUtc;
    }

    public void setCheckInTimeUtc(Long checkInTimeUtc) {
        this.checkInTimeUtc = checkInTimeUtc;
    }

    public Long getCheckOutTimeUtc() {
        return checkOutTimeUtc;
    }

    public void setCheckOutTimeUtc(Long checkOutTimeUtc) {
        this.checkOutTimeUtc = checkOutTimeUtc;
    }

    public long getHoldExpiresAt() {
        return holdExpiresAt;
    }

    public void setHoldExpiresAt(long holdExpiresAt) {
        this.holdExpiresAt = holdExpiresAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
