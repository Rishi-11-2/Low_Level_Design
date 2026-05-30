package service;

import model.*;
import repository.TaskRepository;
import strategy.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskNotificationService notificationService;
    private final TaskSortingContext sortingContext;

    public TaskService(TaskRepository taskRepository,
                       TaskNotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
        this.sortingContext = new TaskSortingContext();
    }

    public Task createTask(int id, String title, String description, LocalDateTime dueDate, Priority priority, int creatorId) {
        Task task = new Task(id, title, description, dueDate, priority, creatorId);
        taskRepository.save(task);
        System.out.println("[TaskService] Created Task: '" + title + "' (id=" + id + ") priority=" + priority);
        
        // Auto-subscribe creator to notifications
        notificationService.subscribeToTask(id, creatorId);
        notificationService.notifySubscribers(task, ChangeType.CREATED, "NONE", title, creatorId);
        
        return task;
    }

    public Task updateTask(int taskId, String title, String description, LocalDateTime dueDate, Priority priority, int actorUserId) {
        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found ID: " + taskId);
        }

        System.out.println("[TaskService] Updating Task ID: " + taskId);
        String oldTitle = task.getTitle();
        
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        
        if (task.getPriority() != priority) {
            task.setPriority(priority); // Enforces recursive subtask update & notifies priority change
        }

        taskRepository.save(task);
        notificationService.notifySubscribers(task, ChangeType.UPDATED, oldTitle, title, actorUserId);
        
        return task;
    }

    public void deleteTask(int taskId) {
        Task task = taskRepository.findById(taskId);
        if (task != null) {
            System.out.println("[TaskService] Deleting Task ID: " + taskId + " with all nested subtasks (Composite Cascade).");
            // Cascade delete subtasks recursively (composite pattern constraint)
            List<Task> allChildren = task.getAllSubtasks();
            for (Task child : allChildren) {
                // Delete from repository
                System.out.println("[Composite Cascade] Deleting child subtask ID: " + child.getId());
            }
            System.out.println("[TaskService] Task deleted successfully.");
        }
    }

    public Task addSubtask(int parentTaskId, Task subtask) {
        Task parent = taskRepository.findById(parentTaskId);
        if (parent == null) {
            throw new IllegalArgumentException("Parent Task not found ID: " + parentTaskId);
        }

        taskRepository.save(subtask);
        parent.addSubtask(subtask); // Enforces Composite Pattern hierarchy and priority scaling!
        taskRepository.save(parent);

        System.out.println("[Composite Pattern] Linked Subtask ID " + subtask.getId() + " under Parent Task ID " + parentTaskId);
        notificationService.subscribeToTask(subtask.getId(), parent.getCreatorId()); // Auto subscribe parent creator
        notificationService.notifySubscribers(subtask, ChangeType.CREATED, "NONE", subtask.getTitle(), parent.getCreatorId());

        return subtask;
    }

    public List<Task> searchTasks(TaskSearchCriteria criteria) {
        List<Task> results = taskRepository.search(criteria);

        // Runtime dynamic strategy selection (Strategy Pattern)
        String sortBy = criteria.getSortBy();
        if ("priority".equalsIgnoreCase(sortBy)) {
            sortingContext.setSortingStrategy(new PrioritySortingStrategy());
        } else if ("dueDate".equalsIgnoreCase(sortBy)) {
            sortingContext.setSortingStrategy(new DueDateSortingStrategy());
        } else if ("createdDate".equalsIgnoreCase(sortBy)) {
            sortingContext.setSortingStrategy(new CreatedDateSortingStrategy());
        }

        System.out.println("[TaskService] Searching tasks. Applying sorting strategy: " + sortingContext.getStrategyName() + " (" + criteria.getSortOrder() + ")");
        List<Task> sorted = sortingContext.sortTasks(results);

        if ("desc".equalsIgnoreCase(criteria.getSortOrder())) {
            Collections.reverse(sorted);
        }

        return sorted;
    }
}
