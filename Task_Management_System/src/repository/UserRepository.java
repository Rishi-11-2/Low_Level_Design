package repository;

import model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Map<Integer, User> userMap = new ConcurrentHashMap<>();

    public User save(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    public User findById(int userId) {
        return userMap.get(userId);
    }
}
