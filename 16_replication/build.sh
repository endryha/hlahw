#!/bin/bash

docker-compose down -v
#rm -rf ./master/data/*
#rm -rf ./slave_1/data/*
#rm -rf ./slave_2/data/*
docker-compose up -d

sleep=3
pwd=111
slave_user="mydb_slave_user"
slave_pwd="mydb_slave_pwd"
slaves_num=2

echo
echo "###########################"
echo "      WAIT FOR MASTER"
echo "###########################"
until docker exec mysql_master sh -c "export MYSQL_PWD=$pwd; mysql -u root -e ';'"; do
  echo "Waiting for mysql_master database connection..."
  sleep $sleep
done

priv_stmt='GRANT REPLICATION SLAVE ON *.* TO "'$slave_user'"@"%" IDENTIFIED BY "'$slave_pwd'"; FLUSH PRIVILEGES;'
docker exec mysql_master sh -c "export MYSQL_PWD=$pwd; mysql -u root -e '$priv_stmt'"

docker-ip() {
  docker inspect --format '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' "$@"
}

MS_STATUS=$(docker exec mysql_master sh -c 'export MYSQL_PWD='$pwd'; mysql -u root -e "SHOW MASTER STATUS"')
CURRENT_LOG=$(echo $MS_STATUS | awk '{print $6}')
CURRENT_POS=$(echo $MS_STATUS | awk '{print $7}')

echo "mysql_master: log=$CURRENT_LOG, position=$CURRENT_POS"

start_slave_stmt="CHANGE MASTER TO MASTER_HOST='$(docker-ip mysql_master)',MASTER_USER='$slave_user',MASTER_PASSWORD='$slave_pwd',MASTER_LOG_FILE='$CURRENT_LOG',MASTER_LOG_POS=$CURRENT_POS; START SLAVE;"
start_slave_cmd='export MYSQL_PWD='$pwd'; mysql -u root -e "'
start_slave_cmd+="$start_slave_stmt"
start_slave_cmd+='"'

show_slave_status_cmd="export MYSQL_PWD=$pwd; mysql -u root -e 'SHOW SLAVE STATUS \G'"

for ((i = 1; i <= slaves_num; i++)); do
  echo
  echo "############################"
  echo "        START SLAVE$i"
  echo "############################"

  until docker-compose exec mysql_slave_"$i" sh -c "export MYSQL_PWD=$pwd; mysql -u root -e ';'"; do
    echo "Waiting for mysql_slave_$i database connection..."
    sleep $sleep
  done

  docker-compose exec mysql_slave_"$i" sh -c "$start_slave_cmd"
  docker-compose exec mysql_slave_"$i" sh -c "$show_slave_status_cmd" | grep "State"
done
