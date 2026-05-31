package repository;

import model.RoomType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RoomTypeRepository {
    private final Map<String, RoomType> roomTypeMap = new ConcurrentHashMap<>();

    public RoomType save(RoomType roomType) {
        roomTypeMap.put(roomType.getId(), roomType);
        return roomType;
    }

    public Optional<RoomType> findById(String roomTypeId) {
        return Optional.ofNullable(roomTypeMap.get(roomTypeId));
    }

    public List<RoomType> findByHotel(String hotelId) {
        return roomTypeMap.values().stream()
                .filter(rt -> rt.getHotelId().equals(hotelId))
                .collect(Collectors.toList());
    }
}
