package controller;

import model.TaskStatus;
import service.TaskStateService;

public class TaskStateController {
    private final TaskStateService taskStateService;

    public TaskStateController(TaskStateService taskStateService) {
        this.taskStateService = taskStateService;
    }

    public void updateTaskStatus(int taskId, TaskStatus newStatus, int actorUserId) {
        taskStateService.updateTaskStatus(taskId, newStatus, actorUserId);
    }
}
