# **Recipe Application API**

## Introduction
@TODO Add introduction

## Description
@TODO Add description
NOTE: Application can be packaged both as jar and war (default packaging is jar , to avoid unnecessary additional web server usage. In case of jar packaging spring boot included tomcat web server will be used.) 


## Requirements
1. Java 17
2. MySQL 8.x
3. Maven 4.x
4. Log directory
5. Image directory


## Recommended modules
All dependencies are included in pom.xml<br/><br/>

## Installation
### Before start:

Create and configure database (tables will be created/updated by application during startup) ,  other settings by updating application*.properties files.

<br/>

Build use maven profile. Profile can be set during build or on application starting process 
``` bash
$ mvn clean compile -f pom.xml -P <profile>
or
$ mvn clean compile -f pom.xml

$ mvn package
```

#### For Details
[https://maven.apache.org](https://maven.apache.org)

<br/>

``` bash
   Output of the mvn package command: <artifactId_from_pom-version_from_pom>.jar 
   is in the maven default output directory (PROJECT_ROOT/target)
```
 <br/>

Run as a spring boot application<br/>
``` bash
java -jar artifactId_from_pom-version_from_pom>.jar --spring.profiles.active=<profile>
```

## Shutdown
Graceful shutdown supported. Also, shutdown possible by <url>/actuator/shutdown (POST). Request is secured and available for auth ROLE_ROOT user ONLY. 

