package repository;

import model.TaskChangeLog;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskChangeLogRepository {
    private final Map<Integer, TaskChangeLog> logMap = new ConcurrentHashMap<>();

    public TaskChangeLog save(TaskChangeLog log) {
        logMap.put(log.getId(), log);
        return log;
    }

    public List<TaskChangeLog> findByTaskId(int taskId) {
        return logMap.values().stream()
                .filter(l -> l.getTaskId() == taskId)
                .collect(Collectors.toList());
    }
}
