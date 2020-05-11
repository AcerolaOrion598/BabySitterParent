package com.djaphar.babysitterparent.SupportClasses.ApiClasses;

public class LoginModel {

    private String username, password, second_password;

    public LoginModel(String username, String password, String second_password) {
        this.username = username;
        this.password = password;
        this.second_password = second_password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSecondPassword() {
        return second_password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecondPassword(String second_password) {
        this.second_password = second_password;
    }
}
