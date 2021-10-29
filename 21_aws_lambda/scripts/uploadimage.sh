#!/bin/bash

IMG_DIR=$1
DELAY=$2

if [ -z "$DELAY" ]; then
  DELAY=1
fi

for path in "$IMG_DIR"/*.jpg; do
  file="$(basename -- "$path")"
  aws s3 cp "$path" s3://convertimage/jpg/"$file"
  sleep $DELAY
done
