package repository;

import model.Hotel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HotelRepository {
    private final Map<String, Hotel> hotelMap = new ConcurrentHashMap<>();

    public Hotel save(Hotel hotel) {
        hotelMap.put(hotel.getId(), hotel);
        return hotel;
    }

    public Optional<Hotel> findById(String hotelId) {
        return Optional.ofNullable(hotelMap.get(hotelId));
    }

    public List<Hotel> findByLocation(String city, String country) {
        return hotelMap.values().stream()
                .filter(h -> h.isActive() && h.getCity().equalsIgnoreCase(city) && h.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }

    public List<Hotel> findAll() {
        return new ArrayList<>(hotelMap.values());
    }
}
