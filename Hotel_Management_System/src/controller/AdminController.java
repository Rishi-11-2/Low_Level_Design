package controller;

import model.*;
import repository.CancellationPolicyRepository;
import repository.HotelRepository;
import repository.RoomRepository;
import repository.RoomTypeRepository;
import repository.SeasonalPriceRepository;
import service.BookingService;
import java.util.UUID;

public class AdminController {
    private final BookingService bookingService;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final SeasonalPriceRepository seasonalPriceRepository;
    private final CancellationPolicyRepository cancellationPolicyRepository;

    public AdminController(BookingService bookingService,
                           HotelRepository hotelRepository,
                           RoomTypeRepository roomTypeRepository,
                           RoomRepository roomRepository,
                           SeasonalPriceRepository seasonalPriceRepository,
                           CancellationPolicyRepository cancellationPolicyRepository) {
        this.bookingService = bookingService;
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.seasonalPriceRepository = seasonalPriceRepository;
        this.cancellationPolicyRepository = cancellationPolicyRepository;
    }

    public Hotel createOrUpdateHotel(Hotel hotel) {
        System.out.println("[AdminController] Registering Hotel: " + hotel.getName());
        return hotelRepository.save(hotel);
    }

    public RoomType createOrUpdateRoomType(RoomType roomType) {
        System.out.println("[AdminController] Registering Room Type: " + roomType.getName() + " for Hotel ID: " + roomType.getHotelId());
        return roomTypeRepository.save(roomType);
    }

    public void updateOverbookingPercent(String hotelId, int percent) {
        System.out.println("[AdminController] Setting Overbooking limit for Hotel " + hotelId + " to: " + percent + "%");
        hotelRepository.findById(hotelId).ifPresent(h -> {
            h.setDefaultOverbookPercent(percent);
            hotelRepository.save(h);
        });
    }

    public SeasonalPrice setSeasonalPrice(String hotelId, String roomTypeId, long dateUtc, long priceMinor) {
        System.out.println("[AdminController] Setting seasonal pricing for Hotel " + hotelId + ", RoomType " + roomTypeId + " on date " + dateUtc + " to $" + (priceMinor / 100.0));
        String id = "SP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        SeasonalPrice price = new SeasonalPrice(id, hotelId, roomTypeId, dateUtc, priceMinor);
        return seasonalPriceRepository.upsert(price);
    }

    public CancellationPolicy createOrUpdatePolicy(CancellationPolicy policy) {
        System.out.println("[AdminController] Creating Cancellation Policy: " + policy.getName());
        return cancellationPolicyRepository.save(policy);
    }

    public void addRoom(Room room) {
        System.out.println("[AdminController] Creating physical Room: " + room.getRoomNumber());
        roomRepository.save(room);
    }

    public Booking checkIn(String bookingId, String roomId, long checkInTimeUtc) {
        return bookingService.checkIn(bookingId, roomId, checkInTimeUtc);
    }

    public Booking checkOut(String bookingId, long checkOutTimeUtc) {
        return bookingService.checkOut(bookingId, checkOutTimeUtc);
    }
}
