java_binary(
    name = "task_producer",
    srcs = [
        "TaskProducerApp.java",
        "TaskProducerService.java",
    ],
    main_class = "com.waqqasjabbar.dtq.taskproducer.TaskProducerApp",
    deps = [
        "//proto:java_task_producer_grpc",
        "//proto:java_task_producer_proto",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)
