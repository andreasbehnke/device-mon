# syntax=docker/dockerfile:1.3
# **** build stage ****
FROM amazoncorretto:21 as build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
# running maven wrapper with cached local repository
RUN --mount=type=cache,target=/root/.m2 ./mvnw install
# explode jar for using multiple layers in next step
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# **** runtime stage ****
FROM amazoncorretto:21
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","org.network.devicemon.DevicemonApplication"]
