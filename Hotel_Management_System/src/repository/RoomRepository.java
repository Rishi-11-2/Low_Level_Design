package repository;

import model.Room;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RoomRepository {
    private final Map<String, Room> roomMap = new ConcurrentHashMap<>();

    public Room save(Room room) {
        roomMap.put(room.getId(), room);
        return room;
    }

    public List<Room> findByHotelAndType(String hotelId, String roomTypeId) {
        return roomMap.values().stream()
                .filter(r -> r.isActive() && r.getHotelId().equals(hotelId) && r.getRoomTypeId().equals(roomTypeId))
                .collect(Collectors.toList());
    }
}
