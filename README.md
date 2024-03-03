# dev-cards
-------------
An easy tool to use flash cards for developers. The goal is to create flashcards of concepts and codes related to DS and System Design and randomly open them to remember for each topic. 
This is backend Repository written using spring.

# Setting Up the Project
--------------

1. clone the repo main branch, create a Mysql Schema with any name and import the schema_dump.sql.
2. use the schema name and replace it in the application.properties, with the username and password of the user which has access to the schema. 
3. build the project using java 17 so that all the dependencies are downloaded
4. Run the DevApiApplication as a Springboot Application.

# Working 
---------------
1. use create a user to create a User with Password and username and role(which is by default gueat) the application is running.
2. Use the login API to generate a Auth token for the user.
3. Use the Auth token and the user Id in the header.
4. call any postman collection APIs after the above steps are done.
5. Some APIs can run without the auth token(which can be used by guest users also).   
