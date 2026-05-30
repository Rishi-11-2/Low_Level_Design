# Task Management System LLD Java Implementation Walkthrough

We have implemented the Low-Level Design (LLD) for the Task Management System in Java under `/Users/rishi/Projects/LLD/Task_Management_System/src/`. The design adheres to the tiered enterprise architecture (Client -> Controller -> Service -> Repository -> Domain) and includes the State, Composite, Observer, and Strategy design patterns as specified in `Task_Management_System_Design.pdf`.

---

## 1. Package Structure Created
All source files are organized under `/Users/rishi/Projects/LLD/Task_Management_System/src/`:

```
src/
├── Main.java                        (System bootstrap & end-to-end simulation runner)
├── model/
│   ├── Priority.java                (Enum: LOW, MEDIUM, HIGH)
│   ├── TaskStatus.java              (Enum: TODO, IN_PROGRESS, REVIEW, COMPLETED, CANCELLED)
│   ├── ChangeType.java              (Enum: CREATED, ASSIGNED, STATUS_CHANGED, COMMENT_ADDED)
│   ├── User.java                    (Domain model)
│   ├── Comment.java                 (Domain model)
│   ├── TaskChangeLog.java           (Domain model for audit trails)
│   ├── TaskSubscription.java        (Domain model mapping users to tasks)
│   ├── DateRange.java               (Utility DTO for searching dates)
│   ├── TaskSearchCriteria.java      (Utility DTO for filtering and sorting searches)
│   ├── TaskState.java               (State Pattern interface)
│   ├── TodoState.java               (State Pattern: TODO concrete state)
│   ├── InProgressState.java         (State Pattern: IN_PROGRESS concrete state)
│   ├── ReviewState.java             (State Pattern: REVIEW concrete state)
│   ├── CompletedState.java          (State Pattern: COMPLETED concrete state)
│   ├── CancelledState.java          (State Pattern: CANCELLED concrete state)
│   ├── Task.java                    (Context utilizing State, Composite, and Observer Subject patterns)
│   ├── TaskSubscriber.java          (Observer interface)
│   ├── EmailSubscriber.java         (Concrete observer class)
│   └── MobileAppSubscriber.java     (Concrete observer class)
├── strategy/
│   ├── TaskSortingStrategy.java     (Strategy interface)
│   ├── PrioritySortingStrategy.java (Concrete strategy sorting by priority)
│   ├── DueDateSortingStrategy.java  (Concrete strategy sorting by due date)
│   ├── CreatedDateSortingStrategy.java (Concrete strategy sorting by creation date)
│   └── TaskSortingContext.java      (Strategy pattern context manager)
├── repository/
│   ├── TaskRepository.java
│   ├── UserRepository.java
│   ├── CommentRepository.java
│   ├── TaskChangeLogRepository.java
│   └── TaskSubscriptionRepository.java
├── service/
│   ├── TaskService.java             (Manages core tasks, subtasks, and searching)
│   ├── TaskStateService.java        (Manages state transitions and historical change logs)
│   ├── TaskAssignmentService.java   (Handles assignments and unassignments)
│   └── TaskNotificationService.java  (Handles subscriptions and observer broadcasts)
└── controller/
    ├── TaskController.java
    ├── TaskStateController.java
    ├── TaskAssignmentController.java
    └── TaskNotificationController.java
```

---

## 2. Key Accomplishments & Design Patterns Used

1. **State Design Pattern**:
   - Manages task status transitions cleanly: `TODO -> IN_PROGRESS -> REVIEW -> COMPLETED / CANCELLED`.
   - Prevents invalid transitions (e.g., transitioning from `REVIEW` to `CANCELLED`) at the class level by raising exceptions.
2. **Composite Design Pattern**:
   - `Task` supports nested subtask structures, acting as both a single unit and a parent collection of subtasks.
   - Adding a subtask automatically handles recursive priority scaling: if a subtask priority is lower than its parent, it scales up to match the parent.
3. **Observer Design Pattern**:
   - `Task` implements the observer subject interface.
   - `TaskSubscriber` provides the observer interface, implemented by `EmailSubscriber` and `MobileAppSubscriber`.
   - Creating tasks, assigning users, and updating statuses automatically notifies all subscribed users via their respective email and push notification channels.
4. **Strategy Design Pattern**:
   - `TaskSortingStrategy` defines the search result sorting strategy.
   - Supports pluggable sorting strategies based on priority, due date, and creation date.
5. **Comprehensive Audit Logs**:
   - Every state change, assignment, and comment addition is recorded in the `TaskChangeLog` with timelines and actors, keeping a robust history of every task.

---

## 3. Verification & Execution Output

The implementation has been successfully compiled and verified:

### Compilation Command
```bash
javac -d out src/model/*.java src/strategy/*.java src/repository/*.java src/service/*.java src/controller/*.java src/Main.java
```

### Run Command
```bash
java -cp out Main
```

### Execution Log
```
==============================================================
       TASK MANAGEMENT SYSTEM LOW LEVEL DESIGN BOOT           
==============================================================

--- Flow 1: Core Task Creation & Notifications ---
[TaskService] Created Task: 'Deploy LLD Java System' (id=101) priority=HIGH
[TaskNotificationService] User ID 1 subscribed to updates for Task ID: 101

>>> [Notification Engine] Broadcaster: Task ID 101 modified. Event: CREATED
[Email Notification] Sending update alert to: alice@example.com for Task ID: 101. Event: CREATED. Details: 'NONE' -> 'Deploy LLD Java System'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-ALICE_ADMIN for Task ID: 101. Event: CREATED
<<< [Notification Engine] Broadcast complete.

--- Flow 2: Adding Subtasks (Composite Pattern) ---
[Composite Pattern] Linked Subtask ID 102 under Parent Task ID 101
[TaskNotificationService] User ID 1 subscribed to updates for Task ID: 102

>>> [Notification Engine] Broadcaster: Task ID 102 modified. Event: CREATED
[Email Notification] Sending update alert to: alice@example.com for Task ID: 102. Event: CREATED. Details: 'NONE' -> 'Write Unit Tests'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-ALICE_ADMIN for Task ID: 102. Event: CREATED
<<< [Notification Engine] Broadcast complete.
[Composite Pattern] Linked Subtask ID 103 under Parent Task ID 101
[TaskNotificationService] User ID 1 subscribed to updates for Task ID: 103

>>> [Notification Engine] Broadcaster: Task ID 103 modified. Event: CREATED
[Email Notification] Sending update alert to: alice@example.com for Task ID: 103. Event: CREATED. Details: 'NONE' -> 'Compile Source Codes'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-ALICE_ADMIN for Task ID: 103. Event: CREATED
<<< [Notification Engine] Broadcast complete.

[Composite Stats] Parent Task ID 101 has subtasks: true
[Composite Stats] Total subtask count: 2
  - Subtask ID: 102 Title: 'Write Unit Tests' Priority inherited: HIGH
  - Subtask ID: 103 Title: 'Compile Source Codes' Priority inherited: HIGH

--- Flow 3: Task Assignment ---
[TaskAssignmentService] Assigning Task ID: 102 to User: bob_dev
[TaskNotificationService] User ID 2 subscribed to updates for Task ID: 102

>>> [Notification Engine] Broadcaster: Task ID 102 modified. Event: ASSIGNED
[Email Notification] Sending update alert to: alice@example.com for Task ID: 102. Event: ASSIGNED. Details: 'null' -> 'bob_dev'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-ALICE_ADMIN for Task ID: 102. Event: ASSIGNED
[Email Notification] Sending update alert to: bob@example.com for Task ID: 102. Event: ASSIGNED. Details: 'null' -> 'bob_dev'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-BOB_DEV for Task ID: 102. Event: ASSIGNED
<<< [Notification Engine] Broadcast complete.

--- Flow 4: State Machine Status Transitions ---
[TaskStateService] Request to update status of Task ID 102 to IN_PROGRESS

>>> [Notification Engine] Broadcaster: Task ID 102 modified. Event: STATUS_CHANGED
[Email Notification] Sending update alert to: alice@example.com for Task ID: 102. Event: STATUS_CHANGED. Details: 'TODO' -> 'IN_PROGRESS'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-ALICE_ADMIN for Task ID: 102. Event: STATUS_CHANGED
[Email Notification] Sending update alert to: bob@example.com for Task ID: 102. Event: STATUS_CHANGED. Details: 'TODO' -> 'IN_PROGRESS'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-BOB_DEV for Task ID: 102. Event: STATUS_CHANGED
<<< [Notification Engine] Broadcast complete.
[TaskStateService] Request to update status of Task ID 102 to REVIEW

>>> [Notification Engine] Broadcaster: Task ID 102 modified. Event: STATUS_CHANGED
[Email Notification] Sending update alert to: alice@example.com for Task ID: 102. Event: STATUS_CHANGED. Details: 'IN_PROGRESS' -> 'REVIEW'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-ALICE_ADMIN for Task ID: 102. Event: STATUS_CHANGED
[Email Notification] Sending update alert to: bob@example.com for Task ID: 102. Event: STATUS_CHANGED. Details: 'IN_PROGRESS' -> 'REVIEW'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-BOB_DEV for Task ID: 102. Event: STATUS_CHANGED
<<< [Notification Engine] Broadcast complete.
[Client] Attempting illegal transition: REVIEW status -> CANCELLED status
[TaskStateService] Request to update status of Task ID 102 to CANCELLED
[Client] BLOCKED: Illegal transition prevented! Message: Invalid state transition from REVIEW to CANCELLED
[TaskStateService] Request to update status of Task ID 102 to COMPLETED

>>> [Notification Engine] Broadcaster: Task ID 102 modified. Event: STATUS_CHANGED
[Email Notification] Sending update alert to: alice@example.com for Task ID: 102. Event: STATUS_CHANGED. Details: 'REVIEW' -> 'COMPLETED'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-ALICE_ADMIN for Task ID: 102. Event: STATUS_CHANGED
[Email Notification] Sending update alert to: bob@example.com for Task ID: 102. Event: STATUS_CHANGED. Details: 'REVIEW' -> 'COMPLETED'
[Push Notification] Dispatched mobile alert to device token: DEVICE-TOKEN-BOB_DEV for Task ID: 102. Event: STATUS_CHANGED
<<< [Notification Engine] Broadcast complete.

--- Flow 5: Dynamic Sorting & Queries (Strategy Pattern) ---
[TaskService] Searching tasks. Applying sorting strategy: DUE_DATE (asc)
Sorted search results:
  - Task ID: 103 Title: 'Compile Source Codes' Due: 2026-06-01 Priority: HIGH
  - Task ID: 102 Title: 'Write Unit Tests' Due: 2026-06-02 Priority: HIGH
  - Task ID: 101 Title: 'Deploy LLD Java System' Due: 2026-06-04 Priority: HIGH

--- Flow 6: Verification of Audit Change logs ---
Audit trail history for Task 102:
  - Time: 2026-05-30T18:06:54.836017 Actor ID: 1 Event: CREATED change: 'NONE' -> 'Write Unit Tests'
  - Time: 2026-05-30T18:06:54.839743 Actor ID: 1 Event: ASSIGNED change: 'null' -> 'bob_dev'
  - Time: 2026-05-30T18:06:54.840065 Actor ID: 2 Event: STATUS_CHANGED change: 'TODO' -> 'IN_PROGRESS'
  - Time: 2026-05-30T18:06:54.840243 Actor ID: 2 Event: STATUS_CHANGED change: 'IN_PROGRESS' -> 'REVIEW'
  - Time: 2026-05-30T18:06:54.840525 Actor ID: 1 Event: STATUS_CHANGED change: 'REVIEW' -> 'COMPLETED'

==============================================================
       TASK MANAGEMENT LOW LEVEL SYSTEM SIMULATION COMPLETE   
==============================================================
```
