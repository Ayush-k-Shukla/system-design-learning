package TaskManager;

import java.util.Date;
import java.util.List;

public class TaskManagementSystemDemo {
    public static void run() {
        System.out.println("Running Task Management System Demo...");
        TaskManager manager = TaskManager.getInstance();
        User user1 = new User("1", "Alice", "alice@example.com");
        User user2 = new User("2", "Bob", "bob@example.com");
        Task task1 = new Task("t1", "Title1", "Desc1", TaskStatus.PENDING, TaskPriority.HIGH, new Date(), user1);
        Task task2 = new Task("t2", "Title2", "Desc2", TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, new Date(), user2);
        Task task3 = new Task("t3", "Title3", "Desc3", TaskStatus.COMPLETED, TaskPriority.LOW, new Date(), user1);

        // Create tasks
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        System.out.println("Created tasks: t1, t2, t3");

        // Get task by ID
        System.out.println("Get t1: " + manager.getTaskById("t1").getTitle());

        // Update status
        manager.updateTaskStatus("t1", TaskStatus.COMPLETED);
        System.out.println("Updated t1 status: " + manager.getTaskById("t1").getStatus());

        // Update priority
        manager.updateTaskPriority("t1", TaskPriority.LOW);
        System.out.println("Updated t1 priority: " + manager.getTaskById("t1").getPriority());

        // Assign task
        manager.assignTask("t1", user2);
        System.out.println("Assigned t1 to: " + manager.getTaskById("t1").getAssignee().getName());

        // Get by status
        List<Task> completed = manager.getTaskByStatus(TaskStatus.COMPLETED);
        System.out.println("Tasks with COMPLETED status: " + completed.size());

        // Get by priority
        List<Task> high = manager.getTaskByPriority(TaskPriority.HIGH);
        System.out.println("Tasks with HIGH priority: " + high.size());

        // Get by assignee
        List<Task> aliceTasks = manager.getTaskByAssignee(user1);
        System.out.println("Tasks assigned to Alice: " + aliceTasks.size());

        // Get by keyword
        List<Task> keywordTasks = manager.getTaskByKeyword("Title1");
        System.out.println("Tasks with keyword 'Title1': " + keywordTasks.size());

        // Delete task
        manager.deleteTask("t2");
        System.out.println("Deleted t2");
        try {
            manager.getTaskById("t2");
        } catch (Exception e) {
            System.out.println("t2 not found after deletion (expected): " + e.getMessage());
        }

        // Try to get non-existent task
        try {
            manager.getTaskById("notfound");
        } catch (Exception e) {
            System.out.println("notfound not found (expected): " + e.getMessage());
        }
    }
}
