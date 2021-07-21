file=urls.txt

if [ -f "$file" ]; then
  rm "$file"
fi

touch $file

for ((i = 0; i < $1; i++)); do
  echo "http://localhost:8082/write?uuid=$(uuidgen)&value=$RANDOM" >>$file
done
