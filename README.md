
## Ports

Application | Port      
 -------- |---------  
 Spring Cloud Config Server  | 8012  
 Netflix Eureka Discovery Server | 8000
 Netflix Zuul API Gateway Server | 8011
 Zipkin Distributed Tracing Server | 9411
 RabbitMq | 15672
 

## URLs

|     Application       |     URL     |          
| ------------- | ------------- |
| Album Service | http://localhost:8011/albums-ws/users/1/albums |
| User Service | GET: http://localhost:8011/users-ws/users/ae3ec5a9-c695-4123-bee0-82200541714d POST: http://localhost:8011/users-ws/users |
| H2 Console | http://localhost:8011/users-ws/h2-console  |
|Spring Cloud Config Server| http://localhost:8012/albums-ws/default http://localhost:8012/users-ws/default |
| Eureka | http://localhost:8000/ |
| Zipkin | http://localhost:9411/zipkin/ |
| Spring Cloud Bus Refresh | POST: http://localhost:8012/actuator/bus-refresh |
| RabbitMQ | http://localhost:15672 |
| Login | http://localhost:8011/users-ws/users/login |
| Config Server | http://ec2-54-160-78-153.compute-1.amazonaws.com:8012/PhotoAppApiConfigServer/default or http://localhost:8012/PhotoAppApiConfigServer/default|

## VM Argument

-Dserver.port=8001

### Installing Rabbit MQ

#### Windows
- https://www.rabbitmq.com/install-windows.html
- https://www.rabbitmq.com/which-erlang.html
- http://www.erlang.org/downloads
- Video - https://www.youtube.com/watch?v=gKzKUmtOwR4

#### Mac
- https://www.rabbitmq.com/install-homebrew.html

## Zipkin Installation

Quick Start Page
- https://zipkin.io/pages/quickstart

Downloading Zipkin Jar
- https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec

Command to run

You need to specify the absolute path to run the jarfile.
```
java -jar zipkin.jar
```

## Below is a list of Docker commands used in this video course.

Docker Commands Cheat Sheet
Here is a list of general Docker commands used in this video course: 

- http://appsdeveloperblog.com/docker-commands-cheat-sheet/



Install Docker on AWS EC2
```
sudo yum install docker 
sudo service docker start
sudo usermod -a -G docker ec2-user
```

### Run RabbitMQ Docker Container
```
docker run -d --name rabbit-name-management -p 15672:15672 -p 5672:5672 -p 5671:5671 rabbitmq:3-management
```

### To run RabbitMQ and change Default user name and password:
```
docker run -d --name rabbit-name-management -p 15672:15672 -p 5672:5672 -p 5671:5671 -e RABBITMQ_DEFAULT_USER=user –e RABBITMQ_DEFAULT_PASS=password rabbitmq:3-management
```


### Run Config Servere Docker Container
```
docker run -d -p 8012:8012 -e "spring.rabbitmq.host=172.17.0.2" kargopolov/config-server
```


### Run Eureka Docker Container
```
docker run -d -p 8010:8010 -e "spring.cloud.config.uri=http://172.31.0.133:8012" kargopolov/sk-eureka-server
```


### Run Zuul API Gateway Docker Container
```
docker run -d -e "spring.cloud.config.uri=http://99.79.172.54:8012" -e "spring.rabbitmq.host=99.79.172.54" -p 8011:8011 kargopolov/sk-zuul-api-gateway
```


### Run Elasticsearch Docker Container
```
docker run -d --name elasticsearch --network api_network -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.3.0
```


### Run Kibana Docker Container
```
docker run -d --network api-network -p 5601:5601 kibana:7.3.0
```


### Run Albums Microservice Docker Container
```
docker run -it -d -e "eureka.client.serviceUrl.defaultZone=http://test:test@99.79.99.76:8010/eureka" --network host -e "logging.file=/api-logs/albums-ws.log" -v /home/ec2-user/api-logs:/api-logs kargopolov/albums-microservice
```


### Run Logstash for Albums Microservice Docker Container
```
docker run -d --name logstash /home/ec2-user/api-logs:/api-logs kargopolov/sk-albums-microservice-logstash
```


### Run MySQL Docker Container
```
docker run –d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=sergey -e MYSQL_DATABASE=photo_app -e MYSQL_USER=sergey -e MYSQL_PASSWORD=sergey mysql:latest
```


### Run Users Microservice Docker Container
```
docker run -d -e "spring.cloud.config.uri=http://172.31.4.43:8012" -e "spring.rabbitmq.host=172.31.4.43" -e "eureka.client.serviceUrl.defaultZone=http://test:test@172.31.18.99:8010/eureka" -e "spring.datasource.url=jdbc:mysql://172.31.13.167:3306/photo_app?serverTimezone=UTC" --network host -e "logging.file=/api-logs/users-ws.log" -v /home/ec2-user/api-logs:/api-logs kargopolov/sk-users-microservice
```


### Run Logstash for Users Microservice
```
docker run -d --name users-ws-logstash /home/ec2-user/home:/api-logs kargopolov/sk-users-ws-logstash
```




 
 
 
        