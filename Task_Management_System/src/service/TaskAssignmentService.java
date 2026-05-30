package service;

import model.ChangeType;
import model.Task;
import model.User;
import repository.TaskRepository;
import repository.UserRepository;

public class TaskAssignmentService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskNotificationService notificationService;

    public TaskAssignmentService(TaskRepository taskRepository,
                                 UserRepository userRepository,
                                 TaskNotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public synchronized void assignTask(int taskId, int assigneeId, int actorUserId) {
        Task task = taskRepository.findById(taskId);
        User assignee = userRepository.findById(assigneeId);

        if (task == null) {
            throw new IllegalArgumentException("Task not found ID: " + taskId);
        }
        if (assignee == null) {
            throw new IllegalArgumentException("Assignee not found User ID: " + assigneeId);
        }

        System.out.println("[TaskAssignmentService] Assigning Task ID: " + taskId + " to User: " + assignee.getUsername());
        String oldAssignee = String.valueOf(task.getAssigneeId());
        
        task.setAssigneeId(assigneeId);
        taskRepository.save(task);

        // Auto subscribe the assignee to updates
        notificationService.subscribeToTask(taskId, assigneeId);
        notificationService.notifySubscribers(task, ChangeType.ASSIGNED, oldAssignee, assignee.getUsername(), actorUserId);
    }
}
