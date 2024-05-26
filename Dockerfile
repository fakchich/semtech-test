FROM openjdk:17
MAINTAINER FAKCHICH
COPY data/file.csv ./
COPY target/semtech-coding-test-0.0.1.jar semtech.jar
ENTRYPOINT ["java","-jar","/semtech.jar"]
CMD ["file.csv"]