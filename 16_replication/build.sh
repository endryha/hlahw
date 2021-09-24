#!/bin/bash
docker-sync stop
docker-sync clean

rm -rf ./master/data/*
rm -rf ./slave_1/data/*
rm -rf ./slave_2/data/*

docker-sync start

docker-compose down
docker-compose up -d

./init_replication.sh