## Prerequisites
Make sure you have the following software installed on your machine before proceeding with the setup:

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)

## Setup

Create JAVA_HOME and add the 'bin' directory of maven to the PATH in the environment variables
Use `mvn -Dmaven.test.skip=true clean install` to install the dependencies:

```shell script
mvn -Dmaven.test.skip=true clean install
```

Install packages.

```shell script
mvn install
```

## OR

```shell script
mvn clean package -DskipTests
```

Run the docker. The endpoint will be available 
```shell script
docker-compose -f docker-compose.yml up -d
```
To stop docker
```shell scrip
docker-compose down --remove-orphans
```

## Documents
Postman collection: ./docs/Payments.postman_collection.json
