Overview
The distributed task queue system will have:
1. A Task Producer that generates tasks and publishes them to a Kafka topic.
2. Worker services that consume tasks, process them, and report results back to the Result Aggregator using gRPC.
3. A Result Aggregator service that receives task results and stores or logs them.
