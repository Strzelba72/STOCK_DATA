#!/bin/bash

source ./env-setup.sh

kafka-topics.sh --delete --bootstrap-server ${CLUSTER_NAME}-w-1:9092 --topic ${TOPIC_NAME}
kafka-topics.sh --delete --bootstrap-server ${CLUSTER_NAME}-w-1:9092 --topic ${ANOMALY_TOPIC_NAME}

docker exec -i mymysql mysql -ustreamuser -pstream stock <<EOF
DROP TABLE IF EXISTS stock_data;
EOF
docker stop mymysql && docker rm mymysql && docker rmi mysql:debian

cd ~/
rm -rf ./*
