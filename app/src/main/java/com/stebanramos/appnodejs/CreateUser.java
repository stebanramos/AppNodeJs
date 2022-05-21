package com.stebanramos.appnodejs;

import android.content.Context;
import android.util.Log;

import com.stebanramos.appnodejs.models.Users;
import com.stebanramos.appnodejs.utilies.AsyncResponse;

import org.json.JSONObject;

public class CreateUser {

    private Context context;
    private Users user;


    public CreateUser(Context context, Users user) {
        Log.i("d_funciones","CreateUser CreateUser()");
        this.context = context;
        this.user = user;
    }

    public void postUser(){
        Log.i("d_funciones","CreateUser postUser()");
        try {
            String urlApi = "http://10.0.2.2:3000/users/Add";

            JSONObject data = new JSONObject();

            data.put("first_name", user.getFirstName());
            data.put("last_name", user.getLastName());
            data.put("email", user.getEmail());
            data.put("rol", "user");
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
