write_urls_file=./input/write_urls.txt
read_urls_file=./input/read_urls.txt

write_stats_file=./output/write_stats.txt
read_stats_file=./output/read_stats.txt

concurrency=$1
duration=$2
pause=3

echo "Concurrency: $concurrency"
echo "Duration: $duration"

echo "Generating url files"
./genUrlFiles.sh 1000

echo "Start write stress test"
sleep $pause

siege -c"$concurrency" -t"$duration" -f $write_urls_file 2>$write_stats_file

sleep $pause
echo "Start read stress test"
sleep $pause

siege -c"$concurrency" -t"$duration" -f $read_urls_file 2>$read_stats_file

echo "Write Stats"
cat $write_stats_file

echo "Read Stats"
cat $read_stats_file
