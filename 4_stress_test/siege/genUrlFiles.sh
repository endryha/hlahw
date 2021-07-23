urls_file=./input/urls.txt

if [ -f "$urls_file" ]; then
  rm "$urls_file"
fi

touch $urls_file

for ((i = 0; i < $1; i++)); do
  uuid=$(uuidgen)
  n=$RANDOM
  echo "http://localhost:8082/write POST uuid=$uuid&value=$n" >>$urls_file
  echo "http://localhost:8082/get?uuid=$uuid" >>$urls_file
  echo "http://localhost:8082/top10" >>$urls_file
done
