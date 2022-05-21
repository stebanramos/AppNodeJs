package com.stebanramos.appnodejs.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.stebanramos.appnodejs.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("d_funciones","SplashActivity onCreate()");

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}