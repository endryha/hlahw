count=$1
for i in $(seq $count); do
    echo "INSERT INTO users (name, age) VALUES ('$(faker name)', $(( ( RANDOM % 80 )  + 18 )));"
done