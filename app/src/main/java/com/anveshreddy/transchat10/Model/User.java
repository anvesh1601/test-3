package com.anveshreddy.transchat10.Model;

public class User {
    private String id;
    private String username;
    private String ImageURL;
    private String preferedlanguage;
    private String Email;
    private String status;
    private  String search;
    public User(String id, String username, String ImageURL,String preferedlanguage,String Email,String status,String search) {
        this.id = id;
        this.username = username;
        this.ImageURL = ImageURL;
        this.preferedlanguage = preferedlanguage;
        this.Email=Email;
        this.status=status;
        this.search=search;
    }

    public User() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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
