package com.example.NoteSquad_TestApp;

import java.util.List;

public class Notes {
    private String noteTitle;
    private String noteDescription;
    private String imageUrl;
    private boolean isPublic;
    private String subjectTitle;
    private String userEmail;
    private boolean isFlagged;
    private int upvotes;
    private int downvotes;
    private List<String> upvotedBy;
    private List<String> downvotedBy;



    public Notes() {
        // Default constructor required for Firestore
    }

    public Notes(String noteTitle, String noteDescription, String imageUrl, boolean isPublic, String subjectTitle, String userEmail, boolean isFlagged, int upvotes, int downvotes,List<String> upvotedBy,List<String> downvotedBy) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.imageUrl = imageUrl;
        this.isPublic = isPublic;
        this.subjectTitle = subjectTitle;
        this.userEmail = userEmail;
        this.isFlagged = isFlagged;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.upvotedBy=upvotedBy;
    this.downvotedBy=downvotedBy;
        }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<String>  getUpvotedBy() {
        return upvotedBy;
    }

    public void setUpvotedBy(List<String> upvotedBy) {
        this.upvotedBy = upvotedBy;
    }

    public List<String> getDownvotedBy() {
        return downvotedBy;
    }

    public void setDownvotedBy(List<String> downvotedBy) {
        this.downvotedBy = downvotedBy;
    }
}
