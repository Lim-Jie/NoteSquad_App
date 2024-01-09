package com.example.NoteSquad_TestApp;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Forum {
    private String title;
    private String description;
    private Timestamp timestamp;
    private String username;
    private int originalLikes; // Original likes count
    private int newLikes; // New likes count
    private String forumId;
    private List<ForumComment> comments;

    public Forum() {

    }

    public Forum(String title, String description, Timestamp timestamp, String username, int likes, List<ForumComment> comments) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.username = username;
        this.originalLikes = likes;
        this.newLikes = likes;
        this.comments = comments;
    }

    public Forum(Map<String, Object> data) {
        this.title = (String) data.get("title");
        this.description = (String) data.get("description");
        this.timestamp = (Timestamp) data.get("timestamp");
        this.username = (String) data.get("userName");
        Object likesObject = data.get("likes");
        this.originalLikes = (likesObject instanceof Long) ? ((Long) likesObject).intValue() : 0;
        this.newLikes = this.originalLikes;
        Object commentsObject = data.get("comments");
        if (commentsObject instanceof List) {
            this.comments = (List<ForumComment>) commentsObject;
        } else {
            this.comments = null;
        }
    }

    public List<ForumComment> getComments() {
        return comments;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getForumId() {
        return forumId;
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
    public int getOriginalLikes() {
        return originalLikes;
    }

    public int getNewLikes() {
        return newLikes;
    }

    public void setNewLikes(int newLikes) {
        this.newLikes = newLikes;
    }
    public void setOriginalLikes(int newLikes) {
        this.newLikes = newLikes;
    }
}
