package com.example.NoteSquad_TestApp;

import com.google.firebase.Timestamp;

import java.util.Map;

public class ForumComment {
    private String username;
    private String commentText;
    private Timestamp timestamp;

    public ForumComment() {
    }

    public ForumComment(String username, String commentText, Timestamp timestamp) {
        this.username = username;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public ForumComment(Map<String, Object> data) {
        this.username = (String) data.get("username");;
        this.commentText = (String) data.get("commentText");;
        this.timestamp = (Timestamp) data.get("timestamp");;
    }

    public void setForumCommentUsername(String username) {
        this.username = username;
    }

    public String getForumCommentUsername() {
        return username;
    }

    public void setForumCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getForumCommentText() {
        return commentText;
    }

    public void setForumCommentTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getForumCommentTimestamp() {
        return timestamp;
    }

}
