package com.waqqasjabbar.dtq.task_producer;

import com.waqqasjabbar.dtq.common.Common.*;

import io.grpc.stub.StreamObserver;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.UUID;


public class TaskProducerService extends TaskProducerGrpc.TaskProducerImplBase {

    private final KafkaProducer<String, Task> kafkaProducer;
    private final String taskQueueTopic;

    public TaskProducerService(KafkaProducer<String, Task> kafkaProducer, String taskQueueTopic) {
        this.kafkaProducer = kafkaProducer;
        this.taskQueueTopic = taskQueueTopic;
    }

    @Override
    public void submitTask(TaskRequest request, StreamObserver<TaskResponse> responseObserver) {
        // Generate a unique task ID and timestamp
        String taskId = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();

        // Create the Task object
        Task task = Task.newBuilder()
                .setTaskId(taskId)
                .setTaskType(request.getTaskType())
                .setPayload(request.getPayload())
                .setTimestamp(timestamp)
                .build();

        // Publish the task to Kafka
        ProducerRecord<String, Task> record = new ProducerRecord<>(taskQueueTopic, taskId, task);
        kafkaProducer.send(record);

        // Send a response to the client
        TaskResponse response = TaskResponse.newBuilder()
                .setTaskId(taskId)
                .setStatus("submitted")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}