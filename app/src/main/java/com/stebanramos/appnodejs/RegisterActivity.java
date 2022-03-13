package com.stebanramos.appnodejs;

import static com.stebanramos.appnodejs.PostUser.postUserResponse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements AsyncResponse{

    CreateUser createUser;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }

    public void createUser(View view){
        user = new Users("Steban", "Ramos Ramos", "Steban@gmail.com", "stebanjr", "123");
        createUser = new CreateUser(this, user);

        createUser.postUser();
    }

    @Override
    public void processFinish(String output) {
        if (output.equals("ERROR")){
            Toast.makeText(this, postUserResponse, Toast.LENGTH_LONG).show();
        }

        if (output.equals("FINISHED")){
            Toast.makeText(this, postUserResponse, Toast.LENGTH_LONG).show();
        }
    }
}