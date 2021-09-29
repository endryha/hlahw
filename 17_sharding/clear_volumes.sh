#!/bin/bash
rm -rf ./volumes/main/*
rm -rf ./volumes/shard_1/*
rm -rf ./volumes/shard_2/*
rm -rf ./volumes/standalone/*

docker-sync stop
docker-sync clean