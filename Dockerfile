FROM ubuntu:18.04
RUN apt-get update
RUN apt-get install default-jre -y
WORKDIR /
ADD build/libs/Blockchain-1.0-SNAPSHOT.jar Node.jar
ADD config.json config.json
CMD java -jar Node.jar