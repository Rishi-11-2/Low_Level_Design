package strategy;

import model.Task;
import java.util.List;

public interface TaskSortingStrategy {
    List<Task> sort(List<Task> tasks);
    String getStrategyName();
}
