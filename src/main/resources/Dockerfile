FROM eclipse-temurin

WORKDIR /app

COPY target/springapp-0.0.1-SNAPSHOT.jar springapp.jar

EXPOSE 8080

ENTRYPOINT [ "java","-jar","/app/springapp.jar" ]