package TaskManager;

import java.util.Date;

public class Task {
    private final String title;
    private final String description;
    private final String id;
    private TaskStatus status;
    private TaskPriority priority;
    private final Date dueDate;
    private User assignee;

    public Task(String id,String title,String description,TaskStatus status,TaskPriority priority, Date dueDate,User asignee){
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.id = id;
        this.assignee = asignee;
    }

    // getter

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public User getAssignee() {
        return assignee;
    }

    // setters

    public synchronized void updateStatus(TaskStatus status){
        this.status = status;
    }

    public synchronized void updatePriority(TaskPriority priority){
        this.priority = priority;
    }

    public synchronized void assignUser(User user){
        this.assignee = user;
    }
}
