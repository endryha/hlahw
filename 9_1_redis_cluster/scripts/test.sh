#!/bin/bash

host=127.0.0.1
port=$1
key=$2
sleep=1

while :; do
  uuid=$(uuidgen)
  curl -X POST -H "Content-Type: application/json" "http://$host:$port/api/set" -d '{"key":"'"$key"'","value":"'"$uuid"'"}'
  sleep $sleep
  curl -X GET -H "Content-Type: application/json" "http://$host:$port/api/get?key=$key"
  echo ""
  sleep $sleep
done
