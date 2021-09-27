rm -rf ./shard_1/data/*
rm -rf ./shard_2/data/*
rm -rf ./shard_3/data/*

docker-sync stop
docker-sync clean