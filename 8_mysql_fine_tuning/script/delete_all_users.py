#!/usr/bin/env /usr/local/opt/python@3.9/bin/python3.9
import pymysql
import mysql_utils
import time

batch_size = 10

start_time = time.monotonic()

db = pymysql.connect(host="localhost", user="hla",
                     password="P@ssw0rd", database="hla")

mysql_utils.check_connection(db)

if len(sys.argv) == 2:
    batch_size = int(sys.argv[1])

cursor = db.cursor()

print("Delete all records in batches of", batch_size)

total = 0
rowcount = -1
while rowcount != 0:
    cursor.execute("DELETE FROM user LIMIT " + str(batch_size))
    total = total + cursor.rowcount
    rowcount = cursor.rowcount
    db.commit()
    print("Progress ", total, end='\r')

elapsed = round(time.monotonic() - start_time, 2)

print()
print("Deleted  ", total, "records in", elapsed, "sec")

db.close()
