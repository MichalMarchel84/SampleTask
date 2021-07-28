#Sample Task

##Technology
As task description forbids using frameworks, application was created
using servlet technology. It is configured to use MySQL database, 
however, it can be altered easily by modifying context.xml and pom.xml. 
As it was mentioned that front-end is not to be prepared, output is generated
in JSON format. Lombok @Data annotation was used to hide 
getters/setters/constructors at BlogEntry and User entities (for better readability).

##Conception
I assumed that - as the task is only meant for evaluation purposes - cases 
which are not covered by description (or unclear) can be implemented according 
to my own conception without prior consultation. Therefore, I made the following
assumptions:

- Security breach caused by passing user credentials plain text with GET method
  is intentional (for simplicity) and does not require any action (like changing method
  to POST). Consequently, passwords are stored in database without hashing.
  
- Database schema is to be used "as is" without modifications (like applying UNIQUE constraint 
  to "username" column in "user" table).
  
- As the reason for introducing "readonly" column in "user" table is unclear to me, I introduced
  the following scenario: column "readonly" determines user's permission to create/delete blog 
  entries while column "permission" specifies some other (not yet specified) rights - i.e., a user with
  permission=superuser and readonly=yes may be authorized to perform some operations (like 
  blocking/unblocking other users) but won't be able to change blog content.
  
##Structure

Project structure is typical for Maven project. Program core is organized as follows:

- Controller package contains servlets (one servlet in this case) managed by Tomcat. Servlets handles incoming
  Http requests.

- Service package contains services with business logic. Servlets use them to perform specific
  operations. Services are introduced as interfaces and their implementations to facilitate eventual changes 
  in the future.

- Dao package contains classes for communication with database (data access object). Services use them to
  perform operations on database.

- Model package contains classes that represent table entries (entities) and generally, data transfer objects.

- Utils package is for utility classes