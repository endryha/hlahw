#!/bin/bash
./down.sh
rm -rf ./data/*
rm -rf ./backups/full/*
rm -rf ./backups/wal/*
./up.sh
