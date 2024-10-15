# Task Worker Service Overview

The Task Worker service is responsible for consuming tasks from the task queue (Kafka in this case), processing these tasks (e.g., performing some business logic, transforming data, or running background operations), and then sending the results back to the Task Aggregator service or some storage.

The Task Worker operates as a background worker process that subscribes to Kafka topics where task requests are published by the Task Producer service. When the Task Worker receives a task message from Kafka, it executes the necessary logic based on the task's content and processes the work.

# Core Responsibilities of Task Worker:

- Consume Task Messages from Kafka: Subscribes to the Kafka topic to listen for incoming tasks.
- Process Tasks: Upon receiving a message, the Task Worker parses the task and performs the required business logic.
Acknowledge Completion: After processing, the worker can either send a response back to Kafka, notify the Task Aggregator, or save results to a database.

## Design Components of the Task Worker:
- Kafka Consumer: To read messages from a Kafka topic.
- Business Logic: A function or a set of functions that perform the actual processing of each task.
- Result Handler: To either send processed results to another service or acknowledge completion.

## Example Flow:
- Task Producer sends a task to Kafka, publishing it on a specific topic (e.g., task_requests).
- Task Worker subscribes to the task_requests Kafka topic, receives the task, and processes it.
- After processing, the Task Worker might publish the result on another Kafka topic (e.g., task_responses) or store the result in a database or send it directly to the Task Aggregator via gRPC.

## Task Worker Detailed Implementation
1. Kafka Consumer:
The Task Worker uses a Kafka consumer to subscribe to a Kafka topic and consume messages. Kafka consumer groups can be used if there are multiple worker instances to distribute the load.

2. gRPC Client:
If the Task Worker needs to notify the Task Aggregator about task completion, it can use a gRPC client to send responses to the aggregator.

3. Protobuf Messages:
Since gRPC and Kafka messages both use Protobuf for message serialization, the Task Worker will deserialize incoming Protobuf messages, process them, and (optionally) send results as serialized Protobuf messages.

## Example Task Worker Code
Let’s assume that the Task Worker processes a task related to image resizing. The task will include image data, the desired size, and other parameters.

Task Worker Flow:
- Consume a task from the Kafka topic.
- Parse the Protobuf message.
- Resize the image (or any other business logic).
- Publish the result or notify the aggregator.
- Protobuf Definition (task.proto):

```proto

syntax = "proto3";

package proto;

message TaskRequest {
  string task_id = 1;
  string image_data = 2;  // Base64 encoded image data
  int32 width = 3;
  int32 height = 4;
}

message TaskResponse {
  string task_id = 1;
  string status = 2;
  string resized_image_data = 3;  // Base64 encoded resized image data
}

service TaskAggregator {
  rpc submitTask (TaskRequest) returns (TaskResponse);
}
```

## Task Worker Java Code:
The code demonstrates consuming from Kafka, processing the task (resizing an image), and publishing the result.

```java

import proto.TaskProto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.TaskGrpc;

import java.util.Collections;
import java.util.Properties;

public class TaskWorker {
    private final KafkaConsumer<String, byte[]> consumer;
    private final KafkaProducer<String, byte[]> producer;
    private final TaskGrpc.TaskAggregatorBlockingStub taskAggregatorStub;

    public TaskWorker(String kafkaBootstrapServer, String aggregatorHost, int aggregatorPort) {
        // Set up Kafka consumer properties
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", kafkaBootstrapServer);
        consumerProps.put("group.id", "task-worker-group");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        this.consumer = new KafkaConsumer<>(consumerProps);
        this.consumer.subscribe(Collections.singletonList("task_requests"));

        // Set up Kafka producer properties (for publishing results)
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", kafkaBootstrapServer);
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        this.producer = new KafkaProducer<>(producerProps);

        // Set up gRPC client for Task Aggregator
        ManagedChannel channel = ManagedChannelBuilder.forAddress(aggregatorHost, aggregatorPort)
                .usePlaintext()
                .build();
        this.taskAggregatorStub = TaskGrpc.newBlockingStub(channel);
    }

    public void start() {
        while (true) {
            // Poll for new task messages from Kafka
            ConsumerRecords<String, byte[]> records = consumer.poll(1000);
            for (ConsumerRecord<String, byte[]> record : records) {
                try {
                    // Deserialize the incoming task using Protobuf
                    TaskProto.TaskRequest taskRequest = TaskProto.TaskRequest.parseFrom(record.value());

                    // Perform business logic (e.g., resizing the image)
                    String resizedImage = resizeImage(taskRequest.getImageData(), taskRequest.getWidth(), taskRequest.getHeight());

                    // Create response Protobuf message
                    TaskProto.TaskResponse taskResponse = TaskProto.TaskResponse.newBuilder()
                            .setTaskId(taskRequest.getTaskId())
                            .setStatus("completed")
                            .setResizedImageData(resizedImage)
                            .build();

                    // Send result to Task Aggregator via gRPC
                    taskAggregatorStub.submitTask(taskRequest);

                    // Publish result to Kafka (optional)
                    producer.send(new ProducerRecord<>("task_responses", taskRequest.getTaskId(), taskResponse.toByteArray()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String resizeImage(String base64Image, int width, int height) {
        // Implement image resizing logic (not shown here)
        return base64Image; // Simulate image resizing by returning the same image data
    }

    public static void main(String[] args) {
        TaskWorker worker = new TaskWorker("localhost:9092", "localhost", 50051);
        worker.start();
    }
}
```

## Key Components:
- Kafka Consumer: The worker subscribes to the task_requests Kafka topic and polls for new messages.
- Protobuf Serialization: The Kafka messages are serialized using Protobuf, so the worker deserializes incoming messages into TaskRequest objects and then processes them.
- Image Processing: In this example, the worker performs image resizing as the business logic. It could be any kind of processing based on the task.
- gRPC Client: After processing, the worker uses the taskAggregatorStub (gRPC client) to send the response back to the Task Aggregator service.

## Configuration of the Task Worker
- Kafka Topic: The worker listens to the task_requests topic and optionally publishes results to the task_responses topic.
- Concurrency and Scaling: Multiple instances of the worker can be run in parallel to consume tasks from Kafka (using Kafka's consumer group functionality).
- gRPC Communication: The worker communicates with the Task Aggregator via gRPC for task result submission.

# Conclusion:
The Task Worker is responsible for consuming tasks from Kafka, performing some form of processing, and either returning results to the Task Aggregator via gRPC or publishing results to another Kafka topic. The worker can scale horizontally by running multiple instances to handle high volumes of tasks efficiently.