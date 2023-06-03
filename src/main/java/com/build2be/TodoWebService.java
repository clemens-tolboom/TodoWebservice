package com.build2be;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static spark.Spark.*;

public class TodoWebService {
    private TodoService todoService;
    private Gson gson;

    public TodoWebService(TodoService todoService) {
        this.todoService = todoService;
        this.gson = new Gson();
    }

    /**
     * Parse an integer from a string.
     *
     * @param s the string to parse
     * @return the integer or -1 if the string is not a valid integer
     */
    private int parseID(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String invalidIdMessage(spark.Request req, spark.Response res, String id) {
        res.status(400); // Bad Request
        res.type("application/json");
        return gson.toJson(String.format("Invalid id: %s", req.params("id")));
    }

    /**
     * Start the web service
     */
    public void start() {
        int the_port = 8080;
        try {
            port(the_port);
        } catch (IllegalStateException e) {
            System.err.println(String.format("Port %d is already in use", the_port));
            System.exit(1);
        }

        logger.info(String.format("Listening on http://localhost:%d/", the_port));

        after((req, res) -> {
            logger.info(res.status() + ": " + req.ip() + " " + req.requestMethod() + " " + req.url());
        });

        get("/", (req, res) -> {
            res.redirect("/todos");
            return null;
        });

        get("/todos", (req, res) -> {
            List<Todo> todos = todoService.getAllTodos();
            res.status(200); // OK
            res.type("application/json");
            return gson.toJson(todos);
        });

        get("/todos/:id", (req, res) -> {
            int id = parseID(req.params("id"));
            if (id == -1) {
                invalidIdMessage(req, res, req.params("id"));
            }

            Todo todo = todoService.getTodoById(id);
            if (todo != null) {
                res.status(200); // OK
                res.type("application/json");
                return gson.toJson(todo);
            } else {
                res.status(404); // Not Found
                res.type("application/json");
                return "Todo not found";
            }
        });

        post("/todos", (req, res) -> {
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoService.addTodo(todo);

            res.status(201); // Created
            return "Todo created";
        });

        put("/todos/:id", (req, res) -> {
            int id = parseID(req.params("id"));
            if (id <= 0) {
                invalidIdMessage(req, res, req.params("id"));
            }

            Todo todo = gson.fromJson(req.body(), Todo.class);
            todo.setId(id);
            todoService.updateTodo(todo);

            // No content
            res.status(204); // No content

            return "Todo updated";
        });

        delete("/todos/:id", (req, res) -> {
            int id = parseID(req.params("id"));
            if (id <= 0) {
                invalidIdMessage(req, res, req.params("id"));
            }
            Todo todo = todoService.getTodoById(id);
            if (todo == null) {
                res.status(404); // Not Found
                res.type("application/json");
                return "Todo not found";
            }
            todoService.deleteTodo(id);
            res.status(204); // No content
            return "Todo deleted";
        });

        exception(Exception.class, (e, req, res) -> {
            logger.error("Exception: " + e.getMessage());
            res.status(500); // Internal Server Error
            res.type("application/json");
            res.body(gson.toJson(e.getMessage()));
        });
    }

    final static Logger logger = LoggerFactory.getLogger(TodoWebService.class);

    public static void main(String[] args) {
        logger.debug("Starting TodoWebService");
        TodoService todoService = new TodoService();
        TodoWebService todoWebService = new TodoWebService(todoService);

        todoWebService.start();
    }
}
