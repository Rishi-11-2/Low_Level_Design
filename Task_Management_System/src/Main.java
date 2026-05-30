import controller.*;
import model.*;
import repository.*;
import service.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("       TASK MANAGEMENT SYSTEM LOW LEVEL DESIGN BOOT           ");
        System.out.println("==============================================================");

        // 1. Initialize Repositories
        TaskRepository taskRepository = new TaskRepository();
        UserRepository userRepository = new UserRepository();
        CommentRepository commentRepository = new CommentRepository();
        TaskChangeLogRepository changeLogRepository = new TaskChangeLogRepository();
        TaskSubscriptionRepository subscriptionRepository = new TaskSubscriptionRepository();

        // 2. Initialize Services
        TaskNotificationService notificationService = new TaskNotificationService(
                subscriptionRepository, changeLogRepository, userRepository
        );
        TaskService taskService = new TaskService(taskRepository, notificationService);
        TaskStateService taskStateService = new TaskStateService(taskRepository, notificationService);
        TaskAssignmentService taskAssignmentService = new TaskAssignmentService(
                taskRepository, userRepository, notificationService
        );

        // 3. Initialize Controllers
        TaskController taskController = new TaskController(taskService);
        TaskStateController taskStateController = new TaskStateController(taskStateService);
        TaskAssignmentController taskAssignmentController = new TaskAssignmentController(taskAssignmentService);
        TaskNotificationController notificationController = new TaskNotificationController(notificationService);

        // 4. Setup Users
        User alice = new User(1, "alice_admin", "alice@example.com", "ADMIN");
        User bob = new User(2, "bob_dev", "bob@example.com", "USER");
        User charlie = new User(3, "charlie_qa", "charlie@example.com", "USER");
        userRepository.save(alice);
        userRepository.save(bob);
        userRepository.save(charlie);

        // 5. Task Creation & Auto-Subscription Flow
        System.out.println("\n--- Flow 1: Core Task Creation & Notifications ---");
        Task mainTask = taskController.createTask(
                101, "Deploy LLD Java System", "Deliver production-ready LLD code",
                LocalDateTime.now().plusDays(5), Priority.HIGH, alice.getId()
        );

        // 6. Composite Pattern: Adding Subtasks
        System.out.println("\n--- Flow 2: Adding Subtasks (Composite Pattern) ---");
        Task subtask1 = new Task(102, "Write Unit Tests", "Write Junit cases", LocalDateTime.now().plusDays(3), Priority.LOW, alice.getId());
        Task subtask2 = new Task(103, "Compile Source Codes", "Run Javac builds", LocalDateTime.now().plusDays(2), Priority.MEDIUM, alice.getId());
        
        taskController.addSubtask(mainTask.getId(), subtask1); // Links under 101, child priority scales to HIGH!
        taskController.addSubtask(mainTask.getId(), subtask2); // Links under 101

        System.out.println("\n[Composite Stats] Parent Task ID " + mainTask.getId() + " has subtasks: " + mainTask.hasSubtasks());
        System.out.println("[Composite Stats] Total subtask count: " + mainTask.getSubtaskCount());
        for (Task child : mainTask.getSubtasks()) {
            System.out.println("  - Subtask ID: " + child.getId() + " Title: '" + child.getTitle() + "' Priority inherited: " + child.getPriority());
        }

        // 7. Task Assignment Flow
        System.out.println("\n--- Flow 3: Task Assignment ---");
        taskAssignmentController.assignTask(subtask1.getId(), bob.getId(), alice.getId());

        // 8. State Pattern Workflow & Transition Safeties
        System.out.println("\n--- Flow 4: State Machine Status Transitions ---");
        // Bob starts working (TODO -> IN_PROGRESS)
        taskStateController.updateTaskStatus(subtask1.getId(), TaskStatus.IN_PROGRESS, bob.getId());
        
        // Bob submits for review (IN_PROGRESS -> REVIEW)
        taskStateController.updateTaskStatus(subtask1.getId(), TaskStatus.REVIEW, bob.getId());

        try {
            System.out.println("[Client] Attempting illegal transition: REVIEW status -> CANCELLED status");
            taskStateController.updateTaskStatus(subtask1.getId(), TaskStatus.CANCELLED, bob.getId());
        } catch (IllegalStateException e) {
            System.out.println("[Client] BLOCKED: Illegal transition prevented! Message: " + e.getMessage());
        }

        // Complete the task (REVIEW -> COMPLETED)
        taskStateController.updateTaskStatus(subtask1.getId(), TaskStatus.COMPLETED, alice.getId());

        // 9. Strategy Pattern Sorting Flow
        System.out.println("\n--- Flow 5: Dynamic Sorting & Queries (Strategy Pattern) ---");
        TaskSearchCriteria criteria = new TaskSearchCriteria()
                .creatorId(alice.getId())
                .sortBy("dueDate")
                .sortOrder("asc");

        List<Task> sortedList = taskController.searchTasks(criteria);
        System.out.println("Sorted search results:");
        for (Task t : sortedList) {
            System.out.println("  - Task ID: " + t.getId() + " Title: '" + t.getTitle() + "' Due: " + t.getDueDate().toLocalDate() + " Priority: " + t.getPriority());
        }

        // 10. Audit Trail Verification
        System.out.println("\n--- Flow 6: Verification of Audit Change logs ---");
        List<TaskChangeLog> logs = notificationController.getTaskHistory(subtask1.getId());
        System.out.println("Audit trail history for Task 102:");
        for (TaskChangeLog log : logs) {
            System.out.println("  - Time: " + log.getTimestamp() + " Actor ID: " + log.getUserId() + " Event: " + log.getChangeType() + " change: '" + log.getOldValue() + "' -> '" + log.getNewValue() + "'");
        }

        System.out.println("\n==============================================================");
        System.out.println("       TASK MANAGEMENT LOW LEVEL SYSTEM SIMULATION COMPLETE   ");
        System.out.println("==============================================================");
    }
}
