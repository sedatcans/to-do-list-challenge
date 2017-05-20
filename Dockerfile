FROM openjdk:8

COPY ./target/to-do-list-challenge-0.0.1-SNAPSHOT.jar spring-boot.jar

CMD java -jar spring-boot.jar