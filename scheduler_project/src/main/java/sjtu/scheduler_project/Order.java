package sjtu.scheduler_project;

public class Order {
    int id;
    int arriveTime;
    int executionTime1;
    int executionTime2;
    int deadline;
    int priority;

    public Order(int id, int arriveTime, int executionTime1, int executionTime2, int deadline, int priority) {
        this.id = id;
        this.arriveTime = arriveTime;
        this.executionTime1 = executionTime1;
        this.executionTime2 = executionTime2;
        this.deadline = deadline;
        this.priority = priority;
    }

    public int getDeadline1() {
        return deadline - executionTime2;
    }
}