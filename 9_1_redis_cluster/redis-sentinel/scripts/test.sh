#!/bin/bash

key=$1

while :; do
  curl -X POST -H "Content-Type: application/json" "http://127.0.0.1:9081/api/set" -d '{"key":"'"$key"'","value":"'"$(uuidgen)"'"}'
  sleep 1
  curl -X GET -H "Content-Type: application/json" "http://127.0.0.1:9081/api/get?key=$key"
  echo ""
  sleep 1
done
