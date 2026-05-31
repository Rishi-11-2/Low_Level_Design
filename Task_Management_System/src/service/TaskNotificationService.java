package service;

import model.*;
import repository.TaskChangeLogRepository;
import repository.TaskSubscriptionRepository;
import repository.UserRepository;
import java.util.List;
import java.util.UUID;

public class TaskNotificationService {
    private final TaskSubscriptionRepository subscriptionRepository;
    private final TaskChangeLogRepository changeLogRepository;
    private final UserRepository userRepository;
    private int logIdCounter = 1;
    private int subIdCounter = 1;

    public TaskNotificationService(TaskSubscriptionRepository subscriptionRepository,
                                   TaskChangeLogRepository changeLogRepository,
                                   UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.changeLogRepository = changeLogRepository;
        this.userRepository = userRepository;
    }

    public void subscribeToTask(int taskId, int userId) {
        TaskSubscription sub = new TaskSubscription(subIdCounter++, userId, taskId);
        subscriptionRepository.save(sub);
        System.out.println("[TaskNotificationService] User ID " + userId + " subscribed to updates for Task ID: " + taskId);
    }

    public void unsubscribeFromTask(int taskId, int userId) {
        List<TaskSubscription> subs = subscriptionRepository.findByTaskId(taskId);
        for (TaskSubscription sub : subs) {
            if (sub.getUserId() == userId) {
                sub.setActive(false);
                subscriptionRepository.save(sub);
            }
        }
        System.out.println("[TaskNotificationService] User ID " + userId + " unsubscribed from Task ID: " + taskId);
    }

    public void syncObservers(Task task) {
        List<TaskSubscription> subs = subscriptionRepository.findByTaskId(task.getId());
        for (TaskSubscription sub : subs) {
            if (sub.isActive()) {
                User user = userRepository.findById(sub.getUserId());
                if (user != null) {
                    task.attach(new EmailSubscriber(user.getEmail()));
                    task.attach(new MobileAppSubscriber("DEVICE-TOKEN-" + user.getUsername().toUpperCase()));
                }
            }
        }
    }

    public void notifySubscribers(Task task, ChangeType changeType, String oldValue, String newValue, int actorUserId) {
        // 1. Log in Audit Trail
        TaskChangeLog log = new TaskChangeLog(logIdCounter++, task.getId(), actorUserId, changeType, oldValue, newValue);
        changeLogRepository.save(log);

        // 2. Sync observers to Task model
        syncObservers(task);
        
        System.out.println("\n>>> [Notification Engine] Broadcaster: Task ID " + task.getId() + " modified. Event: " + changeType);
        
        // Trigger notify directly on the Task subject
        task.notifySubscribers(changeType, oldValue, newValue);
        
        System.out.println("<<< [Notification Engine] Broadcast complete.");
    }

    public List<TaskChangeLog> getTaskHistory(int taskId) {
        return changeLogRepository.findByTaskId(taskId);
    }
}
