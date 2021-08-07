import pymysql
from faker import Faker

N = 1000_000
BATCH_SIZE = 1000


def check_connection():
    global cursor
    cursor = db.cursor()
    cursor.execute("SELECT VERSION()")
    data = cursor.fetchone()
    print("Database version : %s " % data)


fake = Faker(["en_US"], use_weighting=False)

# Open database connection
db = pymysql.connect(host="localhost", user="hla",
                     password="P@ssw0rd", database="hla")
cursor = db.cursor()

check_connection()

print("Insert %s users", N)

sql = "INSERT INTO user (first_name, last_name, birthdate, address, job, phone, email) " \
      "VALUES (%s, %s, %s, %s, %s, %s, %s)"

j = 0
users = []
for i in range(N):
    user = (fake.first_name(), fake.last_name(), str(fake.date()), fake.address().replace('\n', ', '), fake.job(),
            fake.phone_number(), fake.email())
    users.append(user)
    j = j + 1

    if j >= BATCH_SIZE:
        cursor.executemany(sql, users)
        db.commit()
        users=[]

db.close()
