package sjtu.scheduler_project;

public class Task {
    int id;
    int arriveTime;
    int executionTime;
    int deadline;
    int priority;
    int startTime;

    public Task(int id, int arriveTime, int executionTime, int deadline, int priority) {
        this.id = id;
        this.arriveTime = arriveTime;
        this.executionTime = executionTime;
        this.deadline = deadline;
        this.priority = priority;
        startTime = -1;
    }
}