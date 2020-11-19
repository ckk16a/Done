package com.example.done.Model;

public class ToDoModel {
    private int id, status;
    private String task;
    private String description;
    private String event;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getEvent() {return event;}

    public void setEvent(String event) {this.event = event;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}
}
