package com.stebanramos.appnodejs.ui;

import static com.stebanramos.appnodejs.PostUser.postUserResponse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.stebanramos.appnodejs.PostUser;
import com.stebanramos.appnodejs.databinding.ActivityLoginBinding;
import com.stebanramos.appnodejs.utilies.AsyncResponse;
import com.stebanramos.appnodejs.utilies.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.i("d_funciones","LoginActivity onCreate()");
    }

    public void login(View view) {
        Log.i("d_funciones","LoginActivity login()");
        try {
            if (binding.userName.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()) {

                if (binding.userName.getText().toString().isEmpty()) {
                    binding.userName.setError("Campo vacio");
                } else {
                    binding.password.setError("Campo vacio");
                }

            } else {
                String sUsername = binding.userName.getText().toString();
                String sPassword = binding.password.getText().toString();

                authentication(sUsername, sPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void authentication(String sUsername, String sPassword) {
        Log.i("d_funciones","LoginActivity authentication()");
        try {
            String urlApi = "http://10.0.2.2:3000/users/Auth";

            JSONObject data = new JSONObject();

            data.put("username", sUsername);
            data.put("password", sPassword);

            PostUser postUser = new PostUser();
            postUser.delegate = this;
            postUser.execute(urlApi, data.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAccount(View view) {
        Log.i("d_funciones","LoginActivity createAccount()");
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String output) {
        Log.i("d_funciones","LoginActivity processFinish()");
        Log.i("d_funciones","LoginActivity processFinish() postUserResponse " + postUserResponse);

        if (output.equals("ERROR")){
            Toast.makeText(this, postUserResponse, Toast.LENGTH_LONG).show();
        }

        if (output.equals("FINISHED")){
            try {
                JSONObject jsonObject = new JSONObject(postUserResponse);
                if (jsonObject != null && !jsonObject.isNull("token")){
                    Preferences.set_str(this, "auth_token",jsonObject.get("token").toString());
                    Intent intent;
                    if (jsonObject.get("rol").equals("user")){
                        intent = new Intent(this, MenuActivity.class);
                    }else{
                        intent = new Intent(this, AdminActivity.class);
                    }

                    startActivity(intent);
                }else {
                    Toast.makeText(this, postUserResponse, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}