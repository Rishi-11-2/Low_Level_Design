package model;

public class InProgressState implements TaskState {
    @Override
    public boolean canTransitionTo(TaskStatus newStatus) {
        return newStatus == TaskStatus.REVIEW || newStatus == TaskStatus.CANCELLED;
    }

    @Override
    public void performTransition(Task task, TaskStatus newStatus) {
        if (!canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid state transition from IN_PROGRESS to " + newStatus);
        }
        task.setStatus(newStatus);
        if (newStatus == TaskStatus.REVIEW) {
            task.setState(new ReviewState());
        } else {
            task.setState(new CancelledState());
        }
    }

    @Override
    public String getStateName() {
        return "IN_PROGRESS";
    }
}
