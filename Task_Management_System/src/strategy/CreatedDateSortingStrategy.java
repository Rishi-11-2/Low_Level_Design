package strategy;

import model.Task;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CreatedDateSortingStrategy implements TaskSortingStrategy {
    @Override
    public List<Task> sort(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "CREATED_DATE";
    }
}
