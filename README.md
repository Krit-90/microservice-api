# Microservice-api 
REST API for providing information about staff-organisational structure of company and manage it.

##Technologies / Frameworks
+ Java 11
+ Spring Boot Web Application
+ Liquibase
+ Eureka
+ Feign
+ Hystrix
+ JaCoCo

## Getting Started

### Installation
1. Clone the repo

`git clone https://git.epam.com/gleb_abdulaev/department-service.git`

2.Install

Each command in a different terminal

`\Eureka>mvn spring-boot:run`

`\department-service\department-impl-module>mvn spring-boot:run`

`\employee-service\employee-impl-module>mvn spring-boot:run`

`\zuul>mvn spring-boot:run`
###Deployment
use the jvm parameter `-Dspring.profiles.active` to configure the environment.

####Spring Profiles
there are two general environment profiles:
+ `test`
_configures logback to use file logging and some test settings._
+ `default`
_configures logback to use file logging and some production settings._

**Example config of server with test data**

`-Dspring.profiles.active=test`
