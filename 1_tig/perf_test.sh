#!/bin/bash
set -x
ab -n 500 -c 50 -r -k http://localhost:8080/write &
ab -n 5000 -c 100 -r -k http://localhost:8080/read &
set +x