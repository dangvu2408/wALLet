package com.example.walletapp.Model;

public class UserModel {
    String username;
    String password;
    String fullname;
    String dateofbirth;
    String gender;

    public UserModel(String username, String password, String fullname, String dateofbirth, String gender) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
