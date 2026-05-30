package service;

import model.ChangeType;
import model.Task;
import model.TaskStatus;
import repository.TaskRepository;

public class TaskStateService {
    private final TaskRepository taskRepository;
    private final TaskNotificationService notificationService;

    public TaskStateService(TaskRepository taskRepository,
                            TaskNotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    public synchronized void updateTaskStatus(int taskId, TaskStatus newStatus, int actorUserId) {
        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found ID: " + taskId);
        }

        String oldStatus = String.valueOf(task.getStatus());
        System.out.println("[TaskStateService] Request to update status of Task ID " + taskId + " to " + newStatus);
        
        // Delegate state transition (Enforces State Pattern transitions)
        task.transitionTo(newStatus);
        taskRepository.save(task);

        // Notify subscribers of the change
        notificationService.notifySubscribers(task, ChangeType.STATUS_CHANGED, oldStatus, String.valueOf(newStatus), actorUserId);
    }
}
