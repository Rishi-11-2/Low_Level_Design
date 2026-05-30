package repository;

import model.Task;
import model.TaskSearchCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskRepository {
    private final Map<Integer, Task> taskMap = new ConcurrentHashMap<>();

    public Task save(Task task) {
        taskMap.put(task.getId(), task);
        return task;
    }

    public Task findById(int taskId) {
        return taskMap.get(taskId);
    }

    public List<Task> findByAssignee(int assigneeId) {
        return taskMap.values().stream()
                .filter(t -> t.getAssigneeId() != null && t.getAssigneeId() == assigneeId)
                .collect(Collectors.toList());
    }

    public List<Task> findByParentTask(int parentTaskId) {
        return taskMap.values().stream()
                .filter(t -> t.getParentTaskId() != null && t.getParentTaskId() == parentTaskId)
                .collect(Collectors.toList());
    }

    public List<Task> findAll() {
        return new ArrayList<>(taskMap.values());
    }

    public List<Task> search(TaskSearchCriteria criteria) {
        Stream<Task> stream = taskMap.values().stream();

        if (criteria.getAssigneeId() != null) {
            stream = stream.filter(t -> t.getAssigneeId() != null && t.getAssigneeId().equals(criteria.getAssigneeId()));
        }
        if (criteria.getCreatorId() != null) {
            stream = stream.filter(t -> t.getCreatorId() == criteria.getCreatorId());
        }
        if (criteria.getPriority() != null) {
            stream = stream.filter(t -> t.getPriority() == criteria.getPriority());
        }
        if (criteria.getStatus() != null) {
            stream = stream.filter(t -> t.getStatus() == criteria.getStatus());
        }
        if (criteria.getDueDateRange() != null) {
            stream = stream.filter(t -> t.getDueDate() != null && criteria.getDueDateRange().isWithin(t.getDueDate()));
        }
        if (criteria.getTags() != null && !criteria.getTags().isEmpty()) {
            stream = stream.filter(t -> t.getTags().containsAll(criteria.getTags()));
        }
        if (criteria.getHasSubtasks() != null) {
            stream = stream.filter(t -> t.hasSubtasks() == criteria.getHasSubtasks());
        }

        return stream.collect(Collectors.toList());
    }
}
