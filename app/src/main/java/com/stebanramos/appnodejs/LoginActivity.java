package com.stebanramos.appnodejs;

import static com.stebanramos.appnodejs.PostUser.postUserResponse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.stebanramos.appnodejs.databinding.ActivityLoginBinding;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{

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
            String urlApi = "http://167.172.245.38:3000/API/users/Authentication";

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

            if (postUserResponse.equals("ok")){
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, postUserResponse, Toast.LENGTH_LONG).show();
            }

        }
    }
}