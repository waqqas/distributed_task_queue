package com.waqqasjabbar.dtq.task_worker;

public class TaskWorkerApp {
    public static void main(String[] args) {
        System.out.println("Hello World from Task Worker!");
        TaskWorkerService worker = new TaskWorkerService("localhost:9092", "localhost", 50051);
        worker.start();

    }
}
