# Bonita marketplace project

This project allows to store the representation of a maven artifact on an elasticsearch, 
and to perform some search on the existing artifacts.  

## Running the application in dev mode

First, ensure that an elastic cluster is running on your localhost. 
Easiest way to start one: 
```
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.8.1
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.8.1

```

You can then run your application in dev mode that enables live coding using:

```
./mvnw quarkus:dev
```

The (amazing) UI is available at http://localhost:8080/index.html
