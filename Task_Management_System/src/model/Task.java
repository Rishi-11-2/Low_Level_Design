package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private final int id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private TaskStatus status;
    private Integer assigneeId;
    private final int creatorId;
    private Integer parentTaskId;
    private List<String> tags = new ArrayList<>();
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Pattern 1: State Pattern variables
    private TaskState state;

    // Pattern 2: Composite Pattern subtask lists
    private final List<Task> subtasks = new ArrayList<>();

    // Pattern 3: Observer Pattern subscriber lists
    private final List<TaskSubscriber> subscribers = new ArrayList<>();

    public Task(int id, String title, String description, LocalDateTime dueDate, Priority priority, int creatorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = TaskStatus.TODO;
        this.assigneeId = null;
        this.creatorId = creatorId;
        this.parentTaskId = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.state = new TodoState(); // Initial state is TODO
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        String oldVal = String.valueOf(this.priority);
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
        notifySubscribers(ChangeType.PRIORITY_CHANGED, oldVal, String.valueOf(priority));
        
        // Propagate priority down to subtasks recursively (composite pattern business logic)
        updateSubtaskPriorities(priority);
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        String oldVal = String.valueOf(this.assigneeId);
        this.assigneeId = assigneeId;
        this.updatedAt = LocalDateTime.now();
        notifySubscribers(ChangeType.ASSIGNED, oldVal, String.valueOf(assigneeId));
    }

    public int getCreatorId() {
        return creatorId;
    }

    public Integer getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Integer parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // --- State Pattern Delegations ---
    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public void transitionTo(TaskStatus newStatus) {
        String oldVal = String.valueOf(this.status);
        
        // Delegate to state pattern object
        state.performTransition(this, newStatus);
        
        notifySubscribers(ChangeType.STATUS_CHANGED, oldVal, String.valueOf(newStatus));
    }

    // --- Composite Pattern Recursive Methods ---
    public List<Task> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Task subtask) {
        subtask.setParentTaskId(this.id);
        subtasks.add(subtask);
        // Ensure child has at least the parent's priority
        if (subtask.getPriority().ordinal() < this.priority.ordinal()) {
            subtask.setPriority(this.priority);
        }
    }

    public List<Task> getAllSubtasks() {
        List<Task> all = new ArrayList<>();
        for (Task child : subtasks) {
            all.add(child);
            all.addAll(child.getAllSubtasks());
        }
        return all;
    }

    public boolean hasSubtasks() {
        return !subtasks.isEmpty();
    }

    public int getSubtaskCount() {
        return getAllSubtasks().size();
    }

    private void updateSubtaskPriorities(Priority parentPriority) {
        for (Task child : subtasks) {
            if (child.getPriority().ordinal() < parentPriority.ordinal()) {
                System.out.println("[Composite Pattern] Propagating priority '" + parentPriority + "' recursively to Subtask ID: " + child.getId());
                child.setPriority(parentPriority);
            }
        }
    }

    // --- Observer Pattern Subject Methods ---
    public void attach(TaskSubscriber subscriber) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    public void detach(TaskSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers(ChangeType changeType, String oldValue, String newValue) {
        for (TaskSubscriber sub : subscribers) {
            sub.update(this.id, changeType, oldValue, newValue);
        }
    }
}
