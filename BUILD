load("@rules_proto//proto:defs.bzl", "proto_library")

proto_library(
    name = "proto",
    srcs = glob(["proto/**/*.proto"]),
    visibility = ["//visibility:public"],
)

load("@com_github_bazelbuild_buildtools//buildifier:def.bzl", "buildifier")

buildifier(
    name = "buildifier",
)
