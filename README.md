# Async Queue 

The app adds tasks to queue and processes it asynchronously. You can change configs in src/main/resources/application-prod.properties:
- tasks-count - tasks that can be process in the same time
- isAsync - tasks process one by one or simultaneously (the same as tasks-count=1)

Yet you can see how it works when start many tasks simultaneously in TaskManagerIntegrationTest or test API in CheckFactorialIntegrationTest  

Used technology:
- Spark - for handle web requests,
- Guice - for dependency injection,
- Jackson - for parse request body

## How to start it?

Be sure you have JDK installed

### Package app with maven wrapper
    ./mvnw clean package

### Start the app
    java -jar target/async-queue-1.0-SNAPSHOT.jar

### You can start tests separately
    ./mvnw test

## Demo factorial task (SlowFactorial.java) endpoints

After start jar file you can send HTTP requests to add tasks to queue.

### Add demo factorial task: 
`POST http://localhost:7000/add-slow-factorial-task -body 5`

    curl -i -H 'Accept: application/json' -d "5" http://localhost:4567/add-slow-factorial-task

### Check factorial task result 
`POST /check-task`

    curl -i -H 'Accept: application/json' -d "5" http://localhost:4567/check-task

