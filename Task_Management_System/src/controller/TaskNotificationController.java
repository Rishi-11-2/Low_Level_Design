package controller;

import model.TaskChangeLog;
import service.TaskNotificationService;
import java.util.List;

public class TaskNotificationController {
    private final TaskNotificationService notificationService;

    public TaskNotificationController(TaskNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void subscribeToTask(int taskId, int userId) {
        notificationService.subscribeToTask(taskId, userId);
    }

    public void unsubscribeFromTask(int taskId, int userId) {
        notificationService.unsubscribeFromTask(taskId, userId);
    }

    public List<TaskChangeLog> getTaskHistory(int taskId) {
        return notificationService.getTaskHistory(taskId);
    }
}
