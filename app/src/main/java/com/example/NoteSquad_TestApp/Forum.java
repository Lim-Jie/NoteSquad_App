package com.example.NoteSquad_TestApp;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.Map;

public class Forum {
    private String title;
    private String description;
    private Timestamp timestamp;
    private String username;

    public Forum() {

    }

    public Forum(String title, String description, Timestamp timestamp, String username) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
    }

    public Forum(Map<String, Object> data) {
        this.title = (String) data.get("title");
        this.description = (String) data.get("description");
        this.timestamp = (Timestamp) data.get("timestamp");
        this.username = (String) data.get("username");
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public String getUsername() {
        return username;
    }
}
