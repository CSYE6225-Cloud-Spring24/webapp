# webapp
# CSYE6225-Cloud Computing

## Web application built with Spring Boot

### Technologies Used : Java Springboot, MySQL

### Build Instructions: 
1. Clone this repository into the local system 
2. Open the CLI 
> mvn clean install mvn spring-boot:run


### Assignment 1:
Implemented an endpoint /healthz that will do the following when called:
Check if the application has connectivity to the database.
Return HTTP 200 OK if the connection is successful.
Return HTTP 503 Service Unavailable if the connection is unsuccessful.


### Assignment 2

Added User API's to Create, Update and Get User details.

Created a organization called 'CSYE6225-Cloud-Spring24' and made a repo called webapp. Forked the webapp from organization into my personal workspace and pushed my springboot application.

### Assignment 3

1. Implemented Integration tests for the /v1/user endpoint with a new GitHub Actions workflow. 
2. Test 1 - Created an account, and using the GET call, validated account exists.
3. Test 2 - Updated the account and using the GET call, validated the account was updated.

### Assignment 4 - Building Custom Application Images using Packer

Created Custom Image in Google Cloud Platform using Packer
