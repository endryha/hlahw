#!/bin/bash
docker-compose rm -f -s && docker-compose up --scale cdn-node=4 --remove-orphans -d