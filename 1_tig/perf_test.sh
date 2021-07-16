#!/bin/bash
set -x
ab -n 100 -c 10 -r -k http://localhost:8081/write &
ab -n 1000 -c 100 -r -k http://localhost:8081/read &
set +x