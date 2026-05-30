package model;

public class ReviewState implements TaskState {
    @Override
    public boolean canTransitionTo(TaskStatus newStatus) {
        return newStatus == TaskStatus.COMPLETED || newStatus == TaskStatus.IN_PROGRESS;
    }

    @Override
    public void performTransition(Task task, TaskStatus newStatus) {
        if (!canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid state transition from REVIEW to " + newStatus);
        }
        task.setStatus(newStatus);
        if (newStatus == TaskStatus.COMPLETED) {
            task.setState(new CompletedState());
        } else {
            task.setState(new InProgressState());
        }
    }

    @Override
    public String getStateName() {
        return "REVIEW";
    }
}
