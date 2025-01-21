FROM maven:3.8.3-openjdk-17
WORKDIR /app

COPY ./src ./src
COPY pom.xml ./

EXPOSE 8080

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/solar-watch-0.0.1-SNAPSHOT.jar"]