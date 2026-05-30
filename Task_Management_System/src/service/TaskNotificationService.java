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

    public void notifySubscribers(Task task, ChangeType changeType, String oldValue, String newValue, int actorUserId) {
        // 1. Log in Audit Trail
        TaskChangeLog log = new TaskChangeLog(logIdCounter++, task.getId(), actorUserId, changeType, oldValue, newValue);
        changeLogRepository.save(log);

        // 2. Fetch all subscribers
        List<TaskSubscription> subs = subscriptionRepository.findByTaskId(task.getId());
        
        System.out.println("\n>>> [Notification Engine] Broadcaster: Task ID " + task.getId() + " modified. Event: " + changeType);
        
        // Notify each active subscriber using observers
        for (TaskSubscription sub : subs) {
            User user = userRepository.findById(sub.getUserId());
            if (user != null) {
                // Channel 1: Email Observer
                EmailSubscriber emailObserver = new EmailSubscriber(user.getEmail());
                task.attach(emailObserver);
                
                // Channel 2: Mobile Observer
                MobileAppSubscriber mobileObserver = new MobileAppSubscriber("DEVICE-TOKEN-" + user.getUsername().toUpperCase());
                task.attach(mobileObserver);
                
                // Trigger notify
                task.notifySubscribers(changeType, oldValue, newValue);
                
                // Clean up observers
                task.detach(emailObserver);
                task.detach(mobileObserver);
            }
        }
        System.out.println("<<< [Notification Engine] Broadcast complete.");
    }

    public List<TaskChangeLog> getTaskHistory(int taskId) {
        return changeLogRepository.findByTaskId(taskId);
    }
}
