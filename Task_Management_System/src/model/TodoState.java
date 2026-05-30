package model;

public class TodoState implements TaskState {
    @Override
    public boolean canTransitionTo(TaskStatus newStatus) {
        return newStatus == TaskStatus.IN_PROGRESS || newStatus == TaskStatus.CANCELLED;
    }

    @Override
    public void performTransition(Task task, TaskStatus newStatus) {
        if (!canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid state transition from TODO to " + newStatus);
        }
        task.setStatus(newStatus);
        if (newStatus == TaskStatus.IN_PROGRESS) {
            task.setState(new InProgressState());
        } else {
            task.setState(new CancelledState());
        }
    }

    @Override
    public String getStateName() {
        return "TODO";
    }
}
