#!/usr/bin/env /usr/local/opt/python@3.9/bin/python3.9
import pymysql
import mysql_utils
from faker import Faker
import time
import sys


if __name__ == "__main__":
    total = 1000

    if len(sys.argv) == 2:
        total = int(sys.argv[1])

    start_time = time.monotonic()

    # Open database connection
    db = pymysql.connect(host="localhost", user="hla",
                         password="P@ssw0rd", database="hla")

    mysql_utils.check_connection(db)

    fake = Faker(["en_US"], use_weighting=False)

    cursor = db.cursor()

    print("Insert   ", total, "records")

    sql = "INSERT INTO user (first_name, last_name, birthdate, address, job, phone, email) " \
          "VALUES (%s, %s, %s, %s, %s, %s, %s)"

    for i in range(total):
        user = (fake.first_name(), fake.last_name(), str(fake.date()), fake.address().replace('\n', ', '), fake.job(),
                fake.phone_number(), fake.email())

        cursor.execute(sql, user)
        db.commit()

    db.close()

    elapsed = round(time.monotonic() - start_time, 2)

    print()
    print("Inserted ", total, " records in", elapsed, "sec")
