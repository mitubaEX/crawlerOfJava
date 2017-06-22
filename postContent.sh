

./solr-5.5.0/bin/solr delete -c $1 -p $2
./solr-5.5.0/bin/solr create -c $1 -p $2
./solr-5.5.0/bin/post -c $1 -p $2 ./json/"$3".json

sed -i -e 's/multiValued="true"/multiValued="false"/g' ./solr-5.5.0/server/solr/$1/conf/managed-schema
./solr-5.5.0/bin/solr restart -m 12g -p $2
