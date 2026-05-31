package service;

import model.*;
import repository.HotelRepository;
import repository.RoomTypeRepository;
import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final InventoryService inventoryService;
    private final PricingService pricingService;

    public SearchService(HotelRepository hotelRepository,
                         RoomTypeRepository roomTypeRepository,
                         InventoryService inventoryService,
                         PricingService pricingService) {
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.inventoryService = inventoryService;
        this.pricingService = pricingService;
    }

    public List<Hotel> searchHotels(SearchFilter filter) {
        System.out.println("[SearchService] Searching hotels in city=" + filter.getCity() + ", country=" + filter.getCountry());
        return hotelRepository.findByLocation(filter.getCity(), filter.getCountry());
    }

    public List<RoomTypeAvailability> getAvailability(String hotelId, DateRange range) {
        System.out.println("[SearchService] Fetching availability for hotelId=" + hotelId + " nights=" + range.getNumberOfNights());
        List<RoomTypeAvailability> availabilities = new ArrayList<>();
        
        List<RoomType> roomTypes = roomTypeRepository.findByHotel(hotelId);
        for (RoomType rt : roomTypes) {
            if (!rt.isActive()) continue;
            
            // Check availability for single room booking (qty = 1)
            boolean isAvailable = inventoryService.checkAvailability(hotelId, rt.getId(), range, 1);
            if (isAvailable) {
                List<NightlyPrice> prices = pricingService.rateStay(hotelId, rt.getId(), range);
                long total = pricingService.computeTotal(prices);
                double avg = pricingService.computeAveragePricePerNight(prices, range.getNumberOfNights());
                
                RoomTypeAvailability availability = new RoomTypeAvailability(
                        rt.getId(), rt.getName(), rt.getCapacity(), rt.getBedType(),
                        rt.getAmenities(), true, total, avg, prices
                );
                availabilities.add(availability);
            }
        }
        return availabilities;
    }
}
