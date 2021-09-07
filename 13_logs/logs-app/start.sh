#!/bin/bash
#./mvnw spring-boot:build-image
docker kill logsapp
docker run -d --rm --name "logsapp" --log-driver syslog --log-opt syslog-address=tcp://localhost:5000 --log-opt tag="{{.ID}} {{.ImageName}}" docker.io/library/logs:0.0.1-SNAPSHOT