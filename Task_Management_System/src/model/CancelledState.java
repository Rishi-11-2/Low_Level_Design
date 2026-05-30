package model;

public class CancelledState implements TaskState {
    @Override
    public boolean canTransitionTo(TaskStatus newStatus) {
        return newStatus == TaskStatus.TODO; // Reactivate task
    }

    @Override
    public void performTransition(Task task, TaskStatus newStatus) {
        if (!canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid state transition from CANCELLED to " + newStatus);
        }
        task.setStatus(newStatus);
        task.setState(new TodoState());
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}
