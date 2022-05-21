package com.stebanramos.appnodejs.models;

import android.util.Log;

public class Users {

    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;


    public Users(String firstName, String lastName, String email, String userName, String password) {
        Log.i("d_funciones","Users Users()");

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public Users(Users formUser) {

        this.firstName = formUser.getFirstName();
        this.lastName = formUser.getLastName();
        this.email = formUser.getEmail();
        this.userName = formUser.getUserName();
        this.password = formUser.getPassword();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
