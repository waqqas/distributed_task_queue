load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_proto_grpc_java//:defs.bzl", "java_grpc_library", "java_proto_library")

proto_library(
    name = "common_proto",
    srcs = ["common.proto"],
)

java_proto_library(
    name = "java_common_proto",
    protos = [":common_proto"],
    visibility = ["//visibility:public"],
)

proto_library(
    name = "task_producer_proto",
    srcs = ["task_producer.proto"],
    deps = [":common_proto"],
)

java_proto_library(
    name = "java_task_producer_proto",
    protos = [":task_producer_proto"],
    visibility = ["//services/task_producer/src/com/waqqasjabbar/dtq/task_producer:__subpackages__"],
    deps = [":java_common_proto"],
)

java_grpc_library(
    name = "java_task_producer_grpc",
    protos = [":task_producer_proto"],
    visibility = ["//services/task_producer/src/com/waqqasjabbar/dtq/task_producer:__subpackages__"],
    deps = [
        ":java_common_proto",
        ":java_task_producer_proto",
    ],
)

proto_library(
    name = "task_worker_proto",
    srcs = ["task_worker.proto"],
    deps = [":common_proto"],
)

java_proto_library(
    name = "java_task_worker_proto",
    protos = [":task_worker_proto"],
    visibility = ["//services/task_worker/src/com/waqqasjabbar/dtq/task_worker:__subpackages__"],
    deps = [":java_common_proto"],
)

java_grpc_library(
    name = "java_task_worker_grpc",
    protos = [":task_worker_proto"],
    visibility = ["//services/task_worker/src/com/waqqasjabbar/dtq/task_worker:__subpackages__"],
    deps = [
        ":java_common_proto",
        ":java_task_worker_proto",
    ],
)
