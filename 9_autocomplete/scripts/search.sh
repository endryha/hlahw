input=$2
field=$1
es=http:/localhost:9200

index=quotes_ngram
limit=10


echo "$index"

data='{
       "from": 0,
       "size": '"$limit"',
       "query": {
         "match": {
           "'"$field"'": {
             "query": "'"$input"'",
             "operator": "and"
           }
         }
       }
     }'

curl -s -X GET -H "Content-Type: application/json" "$es/$index/_search" -d "$data" | jq .hits.hits[]._source."$field"

index=quotes_sayt
echo "$index"

data='{
         "from": 0,
         "size": '"$limit"',
         "query": {
             "multi_match": {
                 "query": "'"$input"'",
                 "type": "phrase_prefix",
                 "fields": [
                     "'"$field"'",
                     "'"$field"'._2gram",
                     "'"$field"'._3gram"
                 ]
             }
         }
     }'

curl -s -X GET -H "Content-Type: application/json" "$es/$index/_search" -d "$data" | jq .hits.hits[]._source."$field"

index=quotes_suggester
echo "$index"

data='{
  "from": 0,
  "size": '"$limit"',
  "_source": ["suggest"],
  "suggest": {
    "'"$field"'_suggest": {
      "prefix": "'"$input"'",
      "completion": {
        "field": "'"$field"'.suggest",
        "skip_duplicates": true,
        "fuzzy": {
          "fuzziness": 1
        }
      }
    }
  }
}'

curl -s -X GET -H "Content-Type: application/json" "$es/$index/_search" -d "$data" | jq .suggest."$field"_suggest[].options[].text