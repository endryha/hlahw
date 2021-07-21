write_urls_file=./input/write_urls.txt
read_urls_file=./input/read_urls.txt

if [ -f "$write_urls_file" ]; then
  rm "$write_urls_file"
fi

if [ -f "$read_urls_file" ]; then
  rm "$read_urls_file"
fi

touch $write_urls_file
touch $read_urls_file

for ((i = 0; i < $1; i++)); do
  uuid=$(uuidgen)
  n=$RANDOM
  echo "http://localhost:8082/write POST uuid=$uuid&value=$n" >>$write_urls_file
  echo "http://localhost:8082/get?uuid=$uuid" >>$read_urls_file
done
