urls_file=urls.txt

if [ -f "$urls_file" ]; then
  rm "$urls_file"
fi

touch $urls_file

for ((i = 0; i < $1; i++)); do
  echo "http://localhost:8081/api/put POST payload=$(uuidgen)" >>$urls_file
done
