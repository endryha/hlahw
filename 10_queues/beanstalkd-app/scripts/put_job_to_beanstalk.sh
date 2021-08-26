#!/bin/bash

data='{
   "priority": 0,
   "delay": 0,
   "timeToRun": 0,
   "payload": "'"$(uuidgen)"'"
 }'

curl -X POST "http://localhost:8080/api/put" -H "accept: */*" -H "Content-Type: application/json" -d "$data"
