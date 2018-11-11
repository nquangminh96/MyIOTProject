package com.example.quangminh.myiotproject.Model;

public class User {
    private String username;
    private String password;
    private String idHome;
    private String name;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdHome() {
        return idHome;
    }

    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String username,String password, String idHome,  String name) {

        this.password = password;
        this.idHome = idHome;
        this.username = username;
        this.name = name;
    }

    public User() {
    }
}
