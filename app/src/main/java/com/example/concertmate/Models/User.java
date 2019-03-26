package com.example.concertmate.Models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String UID;
    private String username;
    private String email;
    private String imageUrl;

    public User(){}

    public User(String UID, String username, String email, String imageUrl) {
        this.UID = UID;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("UID", UID);
        result.put("username", username);
        result.put("email", email);
        result.put("imageUrl", imageUrl);

        return result;
    }
}
