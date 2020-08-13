package com.anveshreddy.transchat10.Model;

public class User {
    private String id;
    private String username;
    private String ImageURL;
    private String preferedlanguage;
    public User(String id, String username, String ImageURL,String preferedlanguage) {
        this.id = id;
        this.username = username;
        this.ImageURL = ImageURL;
        this.preferedlanguage = preferedlanguage;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getPreferedlanguage() {
        return preferedlanguage;
    }

    public void setPreferedlanguage(String preferedlanguage) {
        this.preferedlanguage = preferedlanguage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
