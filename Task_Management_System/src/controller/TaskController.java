package controller;

import model.Priority;
import model.Task;
import model.TaskSearchCriteria;
import service.TaskService;
import java.time.LocalDateTime;
import java.util.List;

public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public Task createTask(int id, String title, String description, LocalDateTime dueDate, Priority priority, int creatorId) {
        return taskService.createTask(id, title, description, dueDate, priority, creatorId);
    }

    public Task updateTask(int taskId, String title, String description, LocalDateTime dueDate, Priority priority, int actorUserId) {
        return taskService.updateTask(taskId, title, description, dueDate, priority, actorUserId);
    }

    public void deleteTask(int taskId) {
        taskService.deleteTask(taskId);
    }

    public List<Task> searchTasks(TaskSearchCriteria criteria) {
        return taskService.searchTasks(criteria);
    }

    public Task addSubtask(int parentTaskId, Task subtask) {
        return taskService.addSubtask(parentTaskId, subtask);
    }
}
