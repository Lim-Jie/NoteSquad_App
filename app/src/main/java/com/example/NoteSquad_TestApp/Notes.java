package com.example.NoteSquad_TestApp;

public class Notes {
    private String name;
    private String email;
    private String imageURL;
    private  String subject;

    public Notes(String name, String email, String imageURL,String subject) {
        this.name = name;
        this.email = email;
        this.imageURL = imageURL;
        this.subject=subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
