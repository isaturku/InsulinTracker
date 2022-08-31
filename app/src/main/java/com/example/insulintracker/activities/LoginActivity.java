package com.example.insulintracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.insulintracker.utilities.Auth;
import com.example.insulintracker.utilities.DB;
import com.example.insulintracker.R;
import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity {
    private EditText emailTxt;
    private EditText passTxt;
    private Button loginBtn;
    private Button registerNowBtn;
    Intent main;
    Intent register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        FirebaseApp app = FirebaseApp.initializeApp(this);
        emailTxt = findViewById(R.id.emailTxtLogin);
        passTxt = findViewById(R.id.passTxtLogin);
        loginBtn = findViewById(R.id.loginBtn);
        registerNowBtn = findViewById(R.id.registerNowBtn);
        main= new Intent(this,MainActivity.class);
        register = new Intent(this, RegisterActivity.class);
        Activity ctx = this;
        DB.init(app);
        Auth.init();
        if(Auth.getUser() != null)
            startActivity(main);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                String pass = passTxt.getText().toString();
                Auth.login(email,pass,ctx,main);
            }
        });
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(register);
            }
        });
    }
}