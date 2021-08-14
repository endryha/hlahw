#!/usr/bin/env /usr/local/opt/python@3.9/bin/python3.9
import pymysql
import mysql_utils
from faker import Faker
import time


if __name__ == "__main__":
    total = 1000
    batch_size = 10

    if len(sys.argv) == 3:
        total = int(sys.argv[1])
        batch_size = int(sys.argv[2])

    start_time = time.monotonic()

    # Open database connection
    db = pymysql.connect(host="localhost", user="hla",
                         password="P@ssw0rd", database="hla")

    mysql_utils.check_connection(db)

    fake = Faker(["en_US"], use_weighting=False)

    cursor = db.cursor()

    print("Insert   ", total, "records in batches of", batch_size)

    sql = "INSERT INTO user (first_name, last_name, birthdate, address, job, phone, email) " \
          "VALUES (%s, %s, %s, %s, %s, %s, %s)"

    j = 0
    users = []
    for i in range(total):
        user = (fake.first_name(), fake.last_name(), str(fake.date()), fake.address().replace('\n', ', '), fake.job(),
                fake.phone_number(), fake.email())
        users.append(user)
        j = j + 1

        if j >= batch_size or i == total:
            cursor.executemany(sql, users)
            db.commit()

            print("Progress ", i + 1, end='\r')

            j = 0
            users = []

    db.close()

    elapsed = round(time.monotonic() - start_time, 2)

    print()
    print("Inserted ", total, " records in", elapsed, "sec")
