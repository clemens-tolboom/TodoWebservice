# TODO Web Service

This is a simple JSON web service that allows you to create, read, update, and delete todos.

You need a JSON client to interact with the web service. cURL examples are provided below.

## Prerequisites

- Java 17

## Get sources

Clone the repository from https://github.com/clemens-tolboom/TodoWebservice

## Building the project

Run the following command :

```
./gradlew build
```

## Running the web service

```
./gradlew run
```
## Persistence

The [h2](https://h2database.com/html/main.html) database is located in `./database/todo.db.mv.db`

## Connecting to the web service

Visit http://localhost:8080/ in your browser. There you will see a list of all the todos.

You can use the following endpoints to interact with the Todo web service using your own client:

- GET /todos: Get all todos
- GET /todos/:id: Get a todo by ID
- POST /todos: Create a new todo
- PUT /todos/:id: Update a todo
- DELETE /todos/:id: Delete a todo

### Using cURL

You can also use cURL to interact with the web service.

#### List

```bash
curl -X GET http://localhost:8080/todos
```

#### Read

```bash
ID=17
curl -X GET http://localhost:8080/todos/$ID
```

#### Create

```bash
curl -X POST -H "Content-Type: application/json" -d '{"title": "My todo", "completed":false}' http://localhost:8080/todos
```

#### Update

```bash
ID=12
curl -X PUT -H "Content-Type: application/json" -d '{"title": "My todotodo", "completed": true}' http://localhost:8080/todos/$ID
```
#### DELETE

```bash
ID=123
curl -X DELETE http://localhost:8080/todos/$ID
```

## Caveats

### Database

- The database "The file is locked:" when running two instances of the application at the same time.
- 
### CVEs

- The used database H2 has a CVE-2022-45868

### Runtime errors

```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```

is covered by adding hard dependency on `slf4j-simple:1.7.25`.