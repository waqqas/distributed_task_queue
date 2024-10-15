java_binary(
    name = "task_worker",
    srcs = [
        "TaskWorkerApp.java",
        "TaskWorkerService.java",
    ],
    main_class = "com.waqqasjabbar.dtq.taskworker.TaskWorkerApp",
    deps = [
        "//proto:java_task_worker_grpc",
        "//proto:java_task_worker_proto",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:org_apache_kafka_kafka_clients",
    ],
)
