# TODO Web Service

This is a simple JSON web service that allows you to create, read, update, and delete todos.

You need a JSON client to interact with the web service. cURL examples are provided below.

## Prerequisites

- Java 17

## Installing

Clone the repository:

```
git clone
```

## Building the project

Run the following command :

```
./gradlew build
```

## Running the web service

```
./gradlew run
```

## Connecting to the web service

Visit http://localhost:8080/ in your browser. There you will see a list of all the todos.

You can now use the following endpoints to interact with the Todo web service:

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
curl -X GET http://localhost:8080/todos/1
```

#### Create

```bash
curl -X POST -H "Content-Type: application/json" -d '{"title": "My todo", "completed":false}' http://localhost:8080/todos
```

#### Update

```bash
curl -X PUT -H "Content-Type: application/json" -d '{"title": "My todotodo", "completed": true}' http://localhost:8080/todos/16
```
#### DELETE

```bash
curl -X DELETE http://localhost:8080/todos/33
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