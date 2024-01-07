package com.example.NoteSquad_TestApp;

import java.util.ArrayList;

public class NotePage {
    String subject;
    ArrayList<Notes> notesList;



    public NotePage(String subject, ArrayList<Notes> notesList) {
        this.subject = subject;
        this.notesList = notesList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<Notes> getNotesList() {
        return notesList;
    }

    public void setNotesList(ArrayList<Notes> notesList) {
        this.notesList = notesList;
    }
}
