urls_file=./input/urls.txt

stats_file=./output/stats.txt

concurrency=$1
duration=$2
pause=1

echo "Concurrency: $concurrency"
echo "Duration: $duration"

echo "Generating url files"
./genUrlFiles.sh 1000

echo "Start stress test"
sleep $pause

siege -i -c"$concurrency" -t"$duration" -f $urls_file 2>$stats_file

echo "Result"
cat $stats_file
