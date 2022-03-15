package com.stebanramos.appnodejs;

import static com.stebanramos.appnodejs.PostUser.postUserResponse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.stebanramos.appnodejs.databinding.ActivityLoginBinding;
import com.stebanramos.appnodejs.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity implements AsyncResponse {

    private ActivityRegisterBinding binding;
    CreateUser createUser;
    Users user;

    String firstName, lastName, email, userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.i("d_funciones", "RegisterActivity onCreate()");
    }

    public void createUser(View view) {
        try {
            Log.i("d_funciones", "RegisterActivity createUser()");

            if (getFormUser() != null){

                user = new Users(getFormUser());
                createUser = new CreateUser(this, user);
                createUser.postUser();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Users getFormUser() {
        Log.i("d_funciones", "RegisterActivity getFormUser()");
        Users fUser = null;
        try {
            if (binding.firstName.getText().toString().isEmpty()
                    || binding.lastName.getText().toString().isEmpty()
                    || binding.email.getText().toString().isEmpty()
                    || binding.userName.getText().toString().isEmpty()
                    || binding.password.getText().toString().isEmpty()
                    || binding.passwordAgain.getText().toString().isEmpty()) {

                if (binding.firstName.getText().toString().isEmpty()) {
                    binding.firstName.setError("Campo vacio");
                }
                if (binding.lastName.getText().toString().isEmpty()) {
                    binding.lastName.setError("Campo vacio");
                }
                if (binding.email.getText().toString().isEmpty()) {
                    binding.email.setError("Campo vacio");
                }
                if (binding.userName.getText().toString().isEmpty()) {
                    binding.userName.setError("Campo vacio");
                }
                if (binding.password.getText().toString().isEmpty()) {
                    binding.password.setError("Campo vacio");
                }
                if (binding.passwordAgain.getText().toString().isEmpty()) {
                    binding.passwordAgain.setError("Campo vacio");
                }
            } else {
                Log.i("d_funciones", "RegisterActivity getFormUser() password " + binding.password.getText());
                Log.i("d_funciones", "RegisterActivity getFormUser() passwordAgain " + binding.passwordAgain.getText());

                password = binding.password.getText().toString();
                String passwordAgain = binding.passwordAgain.getText().toString();

                Log.i("d_funciones", "RegisterActivity getFormUser() password " + password);
                Log.i("d_funciones", "RegisterActivity getFormUser() passwordAgain " + passwordAgain);

                if (password.equals(passwordAgain)) {
                    firstName = binding.firstName.getText().toString();
                    lastName = binding.lastName.getText().toString();
                    email = binding.email.getText().toString();
                    userName = binding.userName.getText().toString();
                    fUser = new Users(firstName, lastName, email, userName, password);
                } else {
                    binding.passwordAgain.setText("");
                    binding.passwordAgain.setError("Password invalida");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fUser;

    }

    @Override
    public void processFinish(String output) {
        Log.i("d_funciones", "RegisterActivity processFinish()");

        if (output.equals("ERROR")) {
            Toast.makeText(this, postUserResponse, Toast.LENGTH_LONG).show();
        }

        if (output.equals("FINISHED")) {
            Toast.makeText(this, postUserResponse, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}