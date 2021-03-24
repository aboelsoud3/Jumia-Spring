FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD ./target/example-0.0.1-SNAPSHOT.jar /app/example.jar
COPY ./sample.db /sample.db
ENTRYPOINT ["java","-jar","/app/example.jar"]
