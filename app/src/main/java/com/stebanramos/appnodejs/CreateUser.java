package com.stebanramos.appnodejs;

import android.content.Context;

import org.json.JSONObject;

public class CreateUser {

    private Context context;
    private Users user;


    public CreateUser(Context context, Users user) {
        this.context = context;
        this.user = user;
    }

    public void postUser(){
        try {
            String urlApi = "http://167.172.245.38:3000/API/users/AddUser";

            JSONObject data = new JSONObject();

            data.put("first_name", user.getFirstName());
            data.put("last_name", user.getLastName());
            data.put("email", user.getEmail());
            data.put("username", user.getUserName());
            data.put("password", user.getPassword());

            PostUser postUser = new PostUser();
            postUser.delegate = (AsyncResponse) context;
            postUser.execute(urlApi, data.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
