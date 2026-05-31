package service;

import model.DateRange;
import model.Hotel;
import model.RoomType;
import repository.BookingRepository;
import repository.HotelRepository;
import repository.RoomTypeRepository;

public class InventoryService {
    private final BookingRepository bookingRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;

    public InventoryService(BookingRepository bookingRepository,
                            RoomTypeRepository roomTypeRepository,
                            HotelRepository hotelRepository) {
        this.bookingRepository = bookingRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.hotelRepository = hotelRepository;
    }

    public int getConfirmedBookingsCount(String hotelId, String roomTypeId, long dateUtc) {
        return bookingRepository.countConfirmedBookings(hotelId, roomTypeId, dateUtc);
    }

    public int getHeldBookingsCount(String hotelId, String roomTypeId, long dateUtc) {
        long now = System.currentTimeMillis();
        return bookingRepository.countHeldBookings(hotelId, roomTypeId, dateUtc, now);
    }

    public int getCheckedInBookingsCount(String hotelId, String roomTypeId, long dateUtc) {
        return bookingRepository.countCheckedInBookings(hotelId, roomTypeId, dateUtc);
    }

    public boolean checkAvailability(String hotelId, String roomTypeId, DateRange range, int qty) {
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElse(null);
        
        if (hotel == null || roomType == null || !hotel.isActive() || !roomType.isActive()) {
            return false;
        }

        int totalRooms = roomType.getTotalRooms();
        int overbookPercent = hotel.getDefaultOverbookPercent();
        int overbookAllowed = (int) Math.ceil(totalRooms * (overbookPercent / 100.0));
        int totalCapacity = totalRooms + overbookAllowed;

        long dayMs = 24 * 60 * 60 * 1000L;
        long start = range.getStartDateUtc();
        long end = range.getEndDateUtc();

        // Check availability night-by-night
        for (long date = start; date < end; date += dayMs) {
            int confirmed = getConfirmedBookingsCount(hotelId, roomTypeId, date);
            int held = getHeldBookingsCount(hotelId, roomTypeId, date);
            int checkedIn = getCheckedInBookingsCount(hotelId, roomTypeId, date);
            
            int booked = confirmed + held + checkedIn;
            int available = totalCapacity - booked;

            if (available < qty) {
                System.out.println("[InventoryService] Availability check FAILED for date=" + date + ". Capacity=" + totalCapacity + ", Booked=" + booked + ", Requested=" + qty);
                return false;
            }
        }
        return true;
    }
}
