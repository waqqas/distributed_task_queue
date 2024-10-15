package com.waqqasjabbar.dtq.task_worker;


import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.waqqasjabbar.dtq.common.Common.*;


public class TaskWorkerService {
    private final KafkaConsumer<String, byte[]> consumer;
    private final KafkaProducer<String, byte[]> producer;
    // private final TaskGrpc.TaskAggregatorBlockingStub taskAggregatorStub;

    public TaskWorkerService(String kafkaBootstrapServer, String aggregatorHost, int aggregatorPort) {
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
        // ManagedChannel channel = ManagedChannelBuilder.forAddress(aggregatorHost, aggregatorPort)
        //         .usePlaintext()
        //         .build();
        // this.taskAggregatorStub = TaskGrpc.newBlockingStub(channel);
    }

    public void start() {
        while (true) {
            // Poll for new task messages from Kafka
            ConsumerRecords<String, byte[]> records = consumer.poll(1000);
            for (ConsumerRecord<String, byte[]> record : records) {
                try {
                    // Deserialize the incoming task using Protobuf
                    TaskRequest taskRequest = TaskRequest.parseFrom(record.value());

                    // Perform business logic (e.g., resizing the image)
                    // String resizedImage = resizeImage(taskRequest.getImageData(), taskRequest.getWidth(), taskRequest.getHeight());

                    // Create response Protobuf message
                    // TaskResponse taskResponse = TaskResponse.newBuilder()
                    //         .setTaskId(taskRequest.getTaskId())
                    //         .setStatus("completed")
                    //         // .setResizedImageData(resizedImage)
                    //         .build();

                    // Send result to Task Aggregator via gRPC
                    // taskAggregatorStub.submitTask(taskRequest);

                    // Publish result to Kafka (optional)
                    // producer.send(new ProducerRecord<>("task_responses", taskRequest.getTaskId(), taskResponse.toByteArray()));
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

}