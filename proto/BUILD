load("@rules_proto_grpc_java//:defs.bzl", "java_proto_library")
load("@rules_proto_grpc_java//:defs.bzl", "java_grpc_library")

proto_library(
    name = "task_producer_proto",
    srcs = ["task_producer.proto"],
)

java_proto_library(
    name = "java_task_producer_proto",
    protos = [":task_producer_proto"],
    visibility = ["//visibility:public"],
    deps = [
    ],
)

java_grpc_library(
    name = "java_task_producer_grpc",
    protos = [":task_producer_proto"],
    visibility = ["//visibility:public"],
    deps = [
    ],
)
