Docker Container With Spring Boot RateLimiter
=====================================================

TOC
---

- [1 Dockerfile](#1-dockerfile)
- [2 Building The Image](#2-building-the-image)
- [3 Running And Testing The Docker Container](#3-running-and-testing-the-docker-container)
- [4 Stopping The Docker Container](#5-stopping-the-docker-container)
- [5 Cheat Sheet](#6-cheat-sheet)

1 Dockerfile
------------
With the **Dockerfile** we can define, configure and initialize our image and container. In **Dockerfile** we can
define;

1. Which image we are going to use. We need a docker image that has the OpenJDK and thus we are going to use Alpine;

```
	FROM openjdk:8-jdk-alpine
```

2. You can use the ARG command inside a Dockerfile to define the name of a parameter and its default value;

```
	ARG JAR_FILE=target/RateLimiter-0.0.1-SNAPSHOT.jar
```

3. The working directory our app is going to run. The name of the working directory will be **app**;

```
	WORKDIR /opt/app
```

4. The files we need to copy into our image. We will need only the **Uber Jar** file so let's copy it into the working
   dir;

```
	COPY ${JAR_FILE} app.jar
```

5. The port number(s) that we need to expose to reach out from the container. Spring Boot default port is 8080;

```
	EXPOSE 8080
```

6. The commands that we need to run as the container goes live. And we need to add simply "java -jar <jar_file>.jar"
   format;

```
	ENTRYPOINT ["java","-jar","app.jar"]
```

Notify that the CMD(command) parameters are separated with comma.

Then our **Dockerfile** will be as below;

```
# For Java 8, try this
FROM openjdk:8-jdk-alpine

# For Java 11, try this
#FROM openjdk:8-jdk-alpine

# Refer to Maven build -> finalName
ARG JAR_FILE=target/RateLimiter-0.0.1-SNAPSHOT.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/RateLimiter-0.0.1-SNAPSHOT.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
```

Now our Dockerfile is all
set, we can directly build the image.

[Go back to TOC](#toc)

2 Building The Image
--------------------
Before building the image, we need to create the **Uber Jar** (aka **Fat Jar**) file, to do so, just clean install with maven as below;

```
	mvn clean install
```

Now our **Uber Jar** file is created under the target folder with the format: "target/<final_name>.jar" (or .war based on package in .pom file)

To build the image, we will use **docker build** command and tag it. The last parameter will be the directory, by using dot ("."),
we point to the current directory. So you must run this command on the top level directory of this repository.

```
	docker build -t spring-boot:1.0 .
```

After the image is built, you can check it with the ```docker image ls``` command. An example is as below;

```
	REPOSITORY    TAG       IMAGE ID       CREATED          SIZE
        spring-boot   1.0       89c4941a0ca9   27 minutes ago   125MB

```

[Go back to TOC](#toc)

3 Running And Testing The Docker Container
------------------------------------------
Now we have our docker image ready however, we need to run a container based on our image. We can simply run our docker container
as below;

```
	docker run -d -p 8080:8080 -t spring-boot:1.0
```

The format is simple;

```
	docker run -p <external-port>:<internal-port> <image-name>
```

The parameter **p** stands for the **port**. **External port** is the port that will be available outside of the docker container.
The **internal port** is the port that will be available inside the docker container, which is **8080** because the default port of
Spring Boot is set to **8080**. So outside of the Docker Container, we will use the port number **80**, and it is mapped to **8080**
and will reach to the Spring Boot application.

[Go back to TOC](#toc)

4 Stopping The Docker Container
-------------------------------
To stop the docker container, we are going to use this command;

```
	docker stop <container_id>
```

It is as simple as that. Remember that when we ran the docker container, we have the Spring Boot output. I'm using windows while
writing this repository and if you hit ctrl+c, even if you are out of the scope of the logging output of Spring Boot, the container
is still alive. To stop it, you have to use the command above. A successful example is as below;

```
E:\RateLimiter>docker container ls
CONTAINER ID        IMAGE                         COMMAND                  CREATED             STATUS              PORTS                  NAMES
ee674eb6bf6c        spring-boot:1.0               "java -jar app.jar"   35 minutes ago      Up 35 minutes       0.0.0.0:8080->8080/tcp   serene_lichterman

E:\RateLimiter>docker container stop ee674eb6bf6c
ee674eb6bf6c

E:\RateLimiter>docker container ls
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES

```

[Go back to TOC](#toc)

5 Cheat Sheet
-------------
1. Maven install to create the fat jar

```
	mvn clean install
```

2. Docker build

```
	docker build -t spring-boot:1.0 .
```

3. Docker run

```
	docker run -d -p 8080:8080 -t spring-boot:1.0
```

4. Get the container id

```
	docker container ls
```

5. Get into the app

```
	docker exec -it <container_id> /bin/bash
```

6. To stop the container

```
	docker container stop <container_id>
```

[Go back to TOC](#toc)