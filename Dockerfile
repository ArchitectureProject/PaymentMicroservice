FROM openjdk:17-slim

ARG VERSION

EXPOSE 8082

ADD target/paymentmicroservice-$VERSION.jar app.jar

CMD ["java", "-jar", "app.jar"]