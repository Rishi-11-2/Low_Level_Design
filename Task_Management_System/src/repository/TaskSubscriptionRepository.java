package repository;

import model.TaskSubscription;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskSubscriptionRepository {
    private final Map<Integer, TaskSubscription> subscriptionMap = new ConcurrentHashMap<>();

    public TaskSubscription save(TaskSubscription subscription) {
        subscriptionMap.put(subscription.getId(), subscription);
        return subscription;
    }

    public List<TaskSubscription> findByTaskId(int taskId) {
        return subscriptionMap.values().stream()
                .filter(s -> s.isActive() && s.getTaskId() == taskId)
                .collect(Collectors.toList());
    }

    public List<TaskSubscription> findByUserId(int userId) {
        return subscriptionMap.values().stream()
                .filter(s -> s.isActive() && s.getUserId() == userId)
                .collect(Collectors.toList());
    }
}
