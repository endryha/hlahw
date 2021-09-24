#!/bin/bash
pwd=111
slaves_num=2
statement=$1

if [[ ! $statement@L =~ "slave" ]]; then
  echo mysql_master
  docker exec mysql_master sh -c "export MYSQL_PWD=$pwd; mysql -u root mydb -e '$statement'"
fi

if [[ ! $statement@L =~ "master" ]]; then
  for ((i = 1; i <= slaves_num; i++)); do
    echo "mysql_slave_$i"
    docker exec mysql_slave_"$i" sh -c "export MYSQL_PWD=$pwd; mysql -u root mydb -e '$statement'"
  done
fi
