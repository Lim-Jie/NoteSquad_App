package com.example.NoteSquad_TestApp;

public class Notes {
    private String noteTitle;
    private String description;
    private String imageURL;
    private  String subject;

    public Notes(String noteTitle, String description, String imageURL, String subject) {
        this.noteTitle = noteTitle;
        this.description = description;
        this.imageURL = imageURL;
        this.subject = subject;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
