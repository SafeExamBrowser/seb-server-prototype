# seb-server-prototype
SEB-Server prototype used to get familiar with the technology stack and proof of concept(s)

## Getting Started

### Prerequisites

- java 8 or later
- git command line
- maven command line
- optional: docker to quick start a mariaDB server

### Installation

- Start a MariaDB server with a root user account, listening on port 6603. The easiest way to do this is by using docker and the mariadb docker image like: 

```
docker run --name mariadbtest -e MYSQL_ROOT_PASSWORD=somePW --publish 6603:3306 mariadb
```

- Go to a installation-directory of your choice and clone this repository by using git clone:

```
git clone https://github.com/SafeExamBrowser/seb-server-prototype.git
```

- Go into project directory:

```
cd seb-server-prototype
```

- Build with Maven:

```
mvn clean install
```

- If the build was successful you should find the Spring-Boot runnable jar artifact within the target folder called something like seb-server-demo-0.0.1-SNAPSHOT.jar:

```
cd target
```

- Now you can start the application like a normal runnable jar by using java command line and give the password for the MariaDB server and the applications encryption password witch is "somePW" for the prototype.

```
java -jar seb-server-demo-0.0.1-SNAPSHOT.jar org.eth.demo.sebserver.SEBServer.class --spring.datasource.username=root --spring.datasource.password=somePW --sebserver.encrypt.password=somePW
```

- If everything is fine, Spring-Boot should starting up the application on localhost and you can go to http://localhost:8080/gui to see the login page




