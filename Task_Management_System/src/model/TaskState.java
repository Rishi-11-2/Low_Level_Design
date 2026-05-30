package model;

public interface TaskState {
    boolean canTransitionTo(TaskStatus newStatus);
    void performTransition(Task task, TaskStatus newStatus);
    String getStateName();
}
