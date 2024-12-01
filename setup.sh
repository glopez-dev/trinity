#spring init --dependencies=web,data-jpa,h2 --build=maven my-springboot-app
#./mvnw spring-boot:run
#./mvnw clean package

#docker build -t my-springboot-app .
#docker run -p 8080:8080 my-springboot-app

# delete_all() {
    # docker network rm $(docker network ls -q)
    # docker rm -v -f $(docker ps -qa)
    # docker rmi -f $(docker images -q)
# }

#docker-compose up --build
#TARGET=prod docker-compose up --build

#prod
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up --build

#dev
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build



