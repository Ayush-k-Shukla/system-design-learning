package TaskManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskManager {
    private static TaskManager instance;
    private final Map<String, Task> tasks;

    private TaskManager() {
        tasks = new ConcurrentHashMap<>();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    // CRUD ops
    public void createTask(Task t) {
        tasks.put(t.getId(), t);
    }

    public void deleteTask(String id) {
        tasks.remove(id);
    }

    public Task getTaskById(String id) {
        if (!tasks.containsKey(id)) {
            throw new RuntimeException("Task not found: " + id);
        }
        return tasks.get(id);
    }

    public void updateTaskStatus(String id, TaskStatus status) {
        getTaskById(id).updateStatus(status);
    }

    public void updateTaskPriority(String id, TaskPriority priority) {
        getTaskById(id).updatePriority(priority);
    }

    public void assignTask(String id, User user) {
        getTaskById(id).assignUser(user);
    }

    public List<Task> getTaskByStatus(TaskStatus status) {
        return tasks.values().stream().filter(task -> task.getStatus().equals(status)).collect(Collectors.toList());
    }

    public List<Task> getTaskByPriority(TaskPriority priority) {
        return tasks.values().stream().filter(task -> task.getPriority().equals(priority)).collect(Collectors.toList());
    }

    public List<Task> getTaskByAssignee(User assignee) {
        return tasks.values().stream().filter(task -> task.getAssignee().getId().equals(assignee.getId()))
                .collect(Collectors.toList());
    }

    public List<Task> getTaskByKeyword(String keyword) {
        return tasks.values().stream()
                .filter(task -> (task.getTitle().contains(keyword) || task.getDescription().contains(keyword)))
                .collect(Collectors.toList());
    }
}
