FROM amazoncorretto:21.0.5-alpine

ADD server/target/*.jar /app.jar
ENV TIMEZONE=Europe/Belgrade
ENV JAVA_MEM_OPTS="-XX:MaxRAMPercentage=70 -XX:InitialRAMPercentage=70"

ENTRYPOINT [ "sh", "-c", "java --enable-preview \
$JAVA_OPTS $JAVA_MEM_OPTS \
-Duser.timezone=${TIMEZONE} \
-XX:+UseContainerSupport \
-XshowSettings:vm \
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5019 \
-jar /app.jar" ]