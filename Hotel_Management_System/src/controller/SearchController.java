package controller;

import model.DateRange;
import model.Hotel;
import model.RoomTypeAvailability;
import model.SearchFilter;
import service.SearchService;
import java.util.List;

public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    public List<Hotel> searchHotels(SearchFilter filter) {
        return searchService.searchHotels(filter);
    }

    public List<RoomTypeAvailability> getAvailability(String hotelId, DateRange range) {
        return searchService.getAvailability(hotelId, range);
    }
}
