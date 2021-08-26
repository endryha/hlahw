urls_file=urls.txt

if [ -f "$urls_file" ]; then
  rm "$urls_file"
fi

touch $urls_file

for ((i = 0; i < $1; i++)); do
  data='{"priority": 0, "delay": 0, "timeToRun": 0, "payload": "'"$(uuidgen)"'"}'
  echo "http://localhost:8080/api/put POST $data" >>$urls_file
done
