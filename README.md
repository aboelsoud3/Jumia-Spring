# Customer example

### ReadMe file
Hi every one ,
this is a simple spring-boot application to expose Rest endpoint , 
some of technologies used in this project :
* [MVN](https://maven.apache.org/guides/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring fox](http://springfox.github.io/springfox/docs/current/#introduction)
* [mockito](https://site.mockito.org/)
* [Docker](https://docs.docker.com/)

### About Project
this project expose [list] end-point to a simple 
Customer Phone Details entity , beside some other features like search and sort by { CountryName , CountryCode , phone}

### Get & Run the Project :
   in an empty folder or directory , open terminal or CMD 
   and enter the following commands in sequence :

 1. `git clone --progress -v "https://github.com/aboelsoud3/Jumia-Spring.git"`

 2.  `cd Jumia-Spring/`

 3. `./mvnw clean install` 
      "to include unit-tests"
 or 
    `./mvnw install  -DskipTests`
      "to skip unit-tests"

 4. `docker build -t jumia/example-app .`

 5. `cd docker`

 6. `docker-compose up -d`

  now a container is up & running for the application , 
  you can get phone list at :
  [phone-List](http://localhost:8080/customer/)
  and you can find end-points documentation at : 
  [customer-controller](http://localhost:8080/swagger-ui.html)
  
  7. final you can test it via postman or front-end Angular application :
   [UI](https://github.com/aboelsoud3/Jumia-Angular).
   
or simply just after step 2 "cd Jumia-Spring/" :
run bash file `./run.sh` -> that will run steps from 3 to 7.   


