FROM openjdk:8

COPY C:\\Users\\sedatcans\\IdeaProjects\\to-do-list-challenge\\target\\to-do-list-challenge-0.0.1-SNAPSHOT.jar spring-boot.jar

CMD java -jar spring-boot.jar