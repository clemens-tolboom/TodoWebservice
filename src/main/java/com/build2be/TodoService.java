package com.build2be;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoService {
    private Connection connection;

    final static Logger logger = LoggerFactory.getLogger(TodoService.class);
    /**
     * Constructor
     */
    public TodoService() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./database/todo.db");
            createTable();
        } catch (Exception e) {
            logger.error("Starting TodoWebService", e);
            System.exit(2);
        }
    }

    private void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS todos (id INTEGER PRIMARY KEY AUTO_INCREMENT, title TEXT, completed INTEGER)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            // Insert some data
            stmt.execute("INSERT INTO todos (title, completed) VALUES ('Hallo', 0), ('Welt', 1), ('!', 0)");
        }
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();
        String query = "SELECT * FROM todos";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Todo todo = new Todo();
                todo.setId(rs.getInt("id"));
                todo.setTitle(rs.getString("title"));
                todo.setCompleted(rs.getBoolean("completed"));
                todos.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    public Todo getTodoById(int id) {
        Todo todo = null;
        String query = "SELECT * FROM todos WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    todo = new Todo();
                    todo.setId(rs.getInt("id"));
                    todo.setTitle(rs.getString("title"));
                    todo.setCompleted(rs.getBoolean("completed"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todo;
    }

    public void addTodo(Todo todo) {
        String query = "INSERT INTO todos (title, completed) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, todo.getTitle());
            pstmt.setBoolean(2, todo.isCompleted());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTodo(Todo todo) {
        String query = "UPDATE todos SET title = ?, completed = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, todo.getTitle());
            pstmt.setBoolean(2, todo.isCompleted());
            pstmt.setInt(3, todo.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTodo(int id) {
        String query = "DELETE FROM todos WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
