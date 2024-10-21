package com.waqqasjabbar.dtq.task_producer;


import org.apache.kafka.clients.producer.KafkaProducer;
import io.github.cdimascio.dotenv.Dotenv;
import com.waqqasjabbar.dtq.common.Task;

public class TaskProducerApp {

    public static void main(String[] args) {

        // Load environment variables from .env file
        try {
            Dotenv dotenv = Dotenv.load();
            String kafkaHost = dotenv.get("KAFKA_HOST");
            String kafkaPort = dotenv.get("KAFKA_PORT");
            String kafkaBootstrapServers = kafkaHost + ":" + kafkaPort;
            // String grpcHost = dotenv.get("GRPC_HOST");
            // int grpcPort = Integer.parseInt(dotenv.get("GRPC_PORT"));

            KafkaProducer<String, Task> kafkaProducer = new KafkaProducer<String, Task> (kafkaBootstrapServers);

            TaskProducerService producer = new TaskProducerService(kafkaProducer, "");
            producer.start();

            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String userInput;
            do {
                System.out.print("Enter a task (type 'quit' to exit): ");
                userInput = scanner.nextLine();
                if (!userInput.equalsIgnoreCase("quit")) {
                    producer.sendTask(userInput);
                }
            } while (!userInput.equalsIgnoreCase("quit"));
            scanner.close();
        } catch (Exception e) {
            System.err.println("Error loading environment variables: " + e.getMessage());
            System.exit(1);
        }

    }
}
