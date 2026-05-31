package repository;

import model.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    public User save(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
    }
}
