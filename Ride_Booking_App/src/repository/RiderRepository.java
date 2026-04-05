package repository;

import model.Rider;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RiderRepository {

    private final Map<Integer, Rider> ridersById = new ConcurrentHashMap<>();
    private final Map<String, Rider> ridersByEmail = new ConcurrentHashMap<>();

    public Rider save(Rider rider) {
        ridersById.put(rider.getId(), rider);
        ridersByEmail.put(rider.getEmail(), rider);
        return rider;
    }

    public Optional<Rider> findById(int id) {
        return Optional.ofNullable(ridersById.get(id));
    }

    public Optional<Rider> findByEmail(String email) {
        return Optional.ofNullable(ridersByEmail.get(email));
    }
}
