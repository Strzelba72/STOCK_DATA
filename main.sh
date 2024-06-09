#!/bin/bash

echo "Setting up environment variables"
source ./setup.sh
echo "Environment variables set up"

echo "HDFS setup"
hadoop fs -copyToLocal $STREAM_DIR_DATA
hadoop fs -copyToLocal $STATIC_DATA
echo "HDFS setup complete"

echo "Setting up Kafka topics"
./topic-setup.sh
echo "Kafka topics set up"