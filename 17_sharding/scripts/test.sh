#!/bin/bash
n=$1
docker-compose down -v && docker-compose up -d

sleep 10

./insert.py "$n" > sharded.txt &
./insert.py "$n" 8432 > standalone.txt &