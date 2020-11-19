package com.example.done.Model;

public class EventModel {
    private int id, status;
    private String event;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String task) {
        this.event = task;
    }
}
