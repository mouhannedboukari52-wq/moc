package com.dailytracker.app.models;

public class User {
    private String uid;
    private String name;
    private String passwordHash;

    public User() {}

    public User(String uid, String name, String passwordHash) {
        this.uid = uid;
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
