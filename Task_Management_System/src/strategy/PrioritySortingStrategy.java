package strategy;

import model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrioritySortingStrategy implements TaskSortingStrategy {
    @Override
    public List<Task> sort(List<Task> tasks) {
        return tasks.stream()
                .sorted((t1, t2) -> Integer.compare(t2.getPriority().ordinal(), t1.getPriority().ordinal()))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "PRIORITY";
    }
}
