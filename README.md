To build app and start in docker container:

./gradlew build
docker build -t temp-app . \
docker run -p 8080:8080 temp-app
