package com.tgl.timesupver1;

import java.io.Serializable;

public class User implements Serializable {
    private String Name;
    private String Email;
    private String Password;
    private String School;
    private String FirebaseID;

    public User() {
    }

    public User(String name, String email, String password, String school) {
        this.Name = name;
        this.Email = email;
        this.Password = password;
        this.School = school;
    }

    public String getFirebaseID() {
        return FirebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        FirebaseID = firebaseID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }
}
