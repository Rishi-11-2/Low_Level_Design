package model;

public class CompletedState implements TaskState {
    @Override
    public boolean canTransitionTo(TaskStatus newStatus) {
        return newStatus == TaskStatus.IN_PROGRESS; // Reopen task
    }

    @Override
    public void performTransition(Task task, TaskStatus newStatus) {
        if (!canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid state transition from COMPLETED to " + newStatus);
        }
        task.setStatus(newStatus);
        task.setState(new InProgressState());
    }

    @Override
    public String getStateName() {
        return "COMPLETED";
    }
}
