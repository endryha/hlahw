echo "Delete index $1"
curl -X DELETE -s -H "Content-Type: application/json" "http:/localhost:9200/$1" > /dev/null
echo "Create index $1 from definition:"
cat "$2"
echo
curl -X PUT -H "Content-Type: application/json" -d "@$2" "http:/localhost:9200/$1"
echo