#!/bin/bash

export CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
export BUCKET_NAME="pbds-24-ms" 
export TOPIC_NAME="stock-data"
export GROUP_NAME="stock-data-group"
export ANOMALY_TOPIC_NAME="stock-anomalies"
export INPUT_DIR="stock-data"
export SLEEP_TIME="10"
export HEADER_LENGTH="1"
export STREAM_DIR_DATA="gs://$BUCKET_NAME/tmp/stock-data"
export STATIC_DATA="gs://$BUCKET_NAME/tmp/static-data/symbols_valid_meta.csv"
export HADOOP_CONF_DIR=/etc/hadoop/conf
export HADOOP_CLASSPATH=`hadoop classpath`

