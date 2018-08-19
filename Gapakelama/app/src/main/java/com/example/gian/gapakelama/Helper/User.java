package com.example.gian.gapakelama.Helper;

/**
 * Created by gian on 26/02/2018.
 */

public class User {

    private String username, email, name;

    public User(String username, String email, String name) {
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
