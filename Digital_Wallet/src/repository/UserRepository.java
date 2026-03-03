package repository;

import model.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory User repository.
 */
public class UserRepository {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(int userId) {
        return Optional.ofNullable(users.get(userId));
    }
}
