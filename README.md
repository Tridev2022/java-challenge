### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### Your experience in Java

Please let us know more about your Java experience in a few sentences. For example:

- I have 3 years experience in Java and I started to use Spring Boot from last year
- I'm a beginner and just recently learned Spring Boot
- I know Spring Boot very well and have been using it for many years

#### 
#### 
#### My experience in Java
1. I have around 8 years of experience in Java and more than 5 years of experience in spring boot.
2. I have good experience in SQL and back-end technology. Also, some experience in front end (HTML, CSS, JS).
3. I'm a quick learner and flexible developer, so that I can easily adopt with any technology in any situation. 

#### What are the changes
1. Added three different packages model, security and exception.
2. Added new DTO class for Employee object to exchange the data between presentation layer and Data Access layer so that the this hides the entity object from presentation layer.
3. Added custom exception class and its handling mechanism.
4. Added logic for basic Auth with in-memory implementation.
5. Added caching mechanism for API calls so that it reduce the frequent DB call.
6. Added JUnit test cases for controller and service layers.

#### What could be done in the future improvement
1. We can add proper logging mechanism by adding logback.xml file and its log rotation.
2. Can be improve on Authentication and Authorization of the application, like token based authentication.
3. To support the multiple language we can improve on localization logic, so that it can helps to make the application more user friendly.
4. We can achieve full ORM feature by using hibernate, JPA, and proper mapping.
5. Can be implement second level cache and proper caching and its eviction mechanism (like LRU, LFU etc).
6. Can be implement multiple data source to improve the performance of the application by fetching the data from MASTER/SLAVE concept for lazy query (reporting query) and real time query.
7. I have fixed some blocker issues using SonarLint, in the future we could continue sonarlint to improve the code quality by fixing/avoiding the major and minor issues.

