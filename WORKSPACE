load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "rules_proto_grpc_java",
    sha256 = "a7c45374bc654863e8dfbac6b260f2a52d16b8d879aec583dfde403e8c2c3737",
    strip_prefix = "rules_proto_grpc_java-5.0.1",
    urls = ["https://github.com/rules-proto-grpc/rules_proto_grpc/releases/download/5.0.1/rules_proto_grpc_java-5.0.1.tar.gz"],
)

RULES_JVM_EXTERNAL_TAG = "4.5"

RULES_JVM_EXTERNAL_SHA = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "junit:junit:4.13.2",
        "io.grpc:grpc-stub:1.68.0",
        "io.grpc:grpc-protobuf:1.68.0",
        "io.grpc:grpc-netty:1.68.0",
        "com.google.protobuf:protobuf-java:4.28.2",
        "org.apache.kafka:kafka-clients:3.8.0",
        "io.github.cdimascio:java-dotenv:5.2.2",
    ],
    maven_install_json = "//:maven_install.json",
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()
