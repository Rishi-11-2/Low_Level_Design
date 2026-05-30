package model;

public class TaskSubscription {
    private final int id;
    private final int userId;
    private final int taskId;
    private boolean isActive;

    public TaskSubscription(int id, int userId, int taskId) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.isActive = true;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
