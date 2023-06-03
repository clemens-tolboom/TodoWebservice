package com.build2be;

public class Todo {
    private int id;
    private String title = "";
    private boolean completed = false;

    // Constructor, getters, and setters
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}