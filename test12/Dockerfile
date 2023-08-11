FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} test12-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","/test12-0.0.1-SNAPSHOT.jar"]