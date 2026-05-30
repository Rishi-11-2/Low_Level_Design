package strategy;

import model.Task;
import java.util.List;

public class TaskSortingContext {
    private TaskSortingStrategy strategy;

    public TaskSortingContext() {
        this.strategy = new PrioritySortingStrategy(); // Default sorting
    }

    public void setSortingStrategy(TaskSortingStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Task> sortTasks(List<Task> tasks) {
        return strategy.sort(tasks);
    }

    public String getStrategyName() {
        return strategy.getStrategyName();
    }
}
