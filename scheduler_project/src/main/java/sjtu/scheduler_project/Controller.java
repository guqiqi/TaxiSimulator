package sjtu.scheduler_project;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class Controller {
    @PostMapping("/controller")
    public ArrayList<Integer> controller(@RequestBody Map<String, Object> map) {
        List<LinkedHashMap> orders = (List<LinkedHashMap>) map.get("orders");
        Order[] orders1 = new Order[orders.size()];
        for (int i = 0; i < orders.size(); i++) {
            orders1[i] = new Order((Integer) orders.get(i).get("id"),
                    (Integer) orders.get(i).get("arriveTime"), (Integer) orders.get(i).get("executionTime1"),
                    (Integer) orders.get(i).get("executionTime2"), (Integer) orders.get(i).get("deadline"), (Integer) orders.get(i).get("priority"));
        }
        return new Controller().fifo(orders1, 100);
    }

    public ArrayList<Integer> fifo(Order[] orders, int end) {
        int time = 0;
        ArrayList<Integer> schedule = new ArrayList<>();
        ArrayList<Task> queue = new ArrayList<>();
        Boolean[] state = new Boolean[orders.length];
        Arrays.fill(state, Boolean.FALSE);

        while (time < end) {
            // add tasks
            for (int i = 0; i < orders.length; i++) {
                Order order = orders[i];
                if (!state[i]) {
                    if (order.arriveTime == time)
                        queue.add(new Task(order.id, time, order.executionTime1, order.deadline, order.priority));
                }
            }
            // execute task
            // finish task and to finish or add the next task
            if (!queue.isEmpty()) {
                Task task = queue.get(0);
                schedule.add(task.id);
                if (task.startTime == -1)
                    task.startTime = time;
                if (task.startTime + task.executionTime == time + 1) {
                    if (!state[task.id - 1]) {
                        state[task.id - 1] = true;
                        Order order = orders[task.id - 1];
                        queue.add(new Task(-1 * order.id, time, order.executionTime2, order.deadline, order.priority));
                    }

                    queue.remove(0);
                }
            } else
                schedule.add(0);

            time++;
        }
        return schedule;
    }

    public ArrayList<Integer> priority_based(Order[] orders, int end) {
        int time = 0;
        ArrayList<Integer> schedule = new ArrayList<>();
        ArrayList<Task> queue = new ArrayList<>();
        Boolean[] state = new Boolean[orders.length];
        Arrays.fill(state, Boolean.FALSE);

        while (time < end) {
            // add tasks
            for (int i = 0; i < orders.length; i++) {
                Order order = orders[i];
                if (!state[i]) {
                    if (order.arriveTime == time) {
                        addTaskWithPriority(queue, new Task(order.id, time, order.executionTime1,
                                order.deadline - order.executionTime2, order.priority));
                    }
                }
            }
            // execute task
            // finish task and to finish or add the next task
            if (!queue.isEmpty()) {
                Task task = queue.get(0);
                schedule.add(task.id);
                if (task.startTime == -1)
                    task.startTime = time;
                if (task.startTime + task.executionTime == time + 1) {
                    if (!state[task.id - 1]) {
                        state[task.id - 1] = true;
                        Order order = orders[task.id - 1];
                        addTaskWithPriority(queue, new Task(-1 * order.id, time, order.executionTime2,
                                order.deadline, order.priority));
                    }

                    queue.remove(0);
                }
            } else
                schedule.add(0);

            time++;
        }
        return schedule;
    }

    public void addTaskWithPriority(ArrayList<Task> queue, Task task) {
        int j = 0;
        while (j < queue.size() && queue.get(j).priority >= task.priority)
            j++;

        if (j == queue.size())
            queue.add(task);
        else if (queue.get(j).startTime == -1)
            queue.add(j, task);
        else
            queue.add(j + 1, task);

    }

    public ArrayList<Integer> edf(Order[] orders, int end) {
        int time = 0;
        ArrayList<Integer> schedule = new ArrayList<>();
        ArrayList<Task> queue = new ArrayList<>();
        Boolean[] state = new Boolean[orders.length];
        Arrays.fill(state, Boolean.FALSE);

        while (time < end) {
            // add tasks
            for (int i = 0; i < orders.length; i++) {
                Order order = orders[i];
                if (!state[i]) {
                    if (order.arriveTime == time) {
                        addTaskWithEDF(queue, new Task(order.id, time, order.executionTime1,
                                order.deadline - order.executionTime2, order.priority));
                    }
                }
            }
            // execute task
            // finish task and to finish or add the next task
            if (!queue.isEmpty()) {
                Task task = queue.get(0);
                schedule.add(task.id);
                if (task.startTime == -1)
                    task.startTime = time;
                if (task.startTime + task.executionTime == time + 1) {
                    if (!state[task.id - 1]) {
                        state[task.id - 1] = true;
                        Order order = orders[task.id - 1];
                        addTaskWithEDF(queue, new Task(-1 * order.id, time, order.executionTime2,
                                order.deadline, order.priority));
                    }

                    queue.remove(0);
                }
            } else
                schedule.add(0);

            time++;
        }
        return schedule;
    }

    public void addTaskWithEDF(ArrayList<Task> queue, Task task) {
        int j = 0;
        while (j < queue.size() && queue.get(j).deadline <= task.deadline)
            j++;

        if (j == queue.size())
            queue.add(task);
        else if (queue.get(j).startTime == -1)
            queue.add(j, task);
        else
            queue.add(j + 1, task);

    }


    public ArrayList<Integer> sjf(Order[] orders, int end) {
        int time = 0;
        ArrayList<Integer> schedule = new ArrayList<>();
        ArrayList<Task> queue = new ArrayList<>();
        Boolean[] state = new Boolean[orders.length];
        Arrays.fill(state, Boolean.FALSE);

        while (time < end) {
            // add tasks
            for (int i = 0; i < orders.length; i++) {
                Order order = orders[i];
                if (!state[i]) {
                    if (order.arriveTime == time) {
                        addTaskWithSJF(queue, new Task(order.id, time, order.executionTime1,
                                order.deadline - order.executionTime2, order.priority));
                    }
                }
            }
            // execute task
            // finish task and to finish or add the next task
            if (!queue.isEmpty()) {
                Task task = queue.get(0);
                schedule.add(task.id);
                if (task.startTime == -1)
                    task.startTime = time;
                if (task.startTime + task.executionTime == time + 1) {
                    if (!state[task.id - 1]) {
                        state[task.id - 1] = true;
                        Order order = orders[task.id - 1];
                        addTaskWithSJF(queue, new Task(-1 * order.id, time, order.executionTime2,
                                order.deadline, order.priority));
                    }

                    queue.remove(0);
                }
            } else
                schedule.add(0);

            time++;
        }
        return schedule;
    }


    public void addTaskWithSJF(ArrayList<Task> queue, Task task) {
        int j = 0;
        while (j < queue.size() && queue.get(j).executionTime <= task.executionTime)
            j++;

        if (j == queue.size())
            queue.add(task);
        else if (queue.get(j).startTime == -1)
            queue.add(j, task);
        else
            queue.add(j + 1, task);

    }

}
