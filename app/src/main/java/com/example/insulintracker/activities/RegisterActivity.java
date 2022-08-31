package com.example.insulintracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.insulintracker.utilities.Auth;
import com.example.insulintracker.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailTxt;
    private EditText passText;
    private EditText confirmPassText;
    private Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailTxt = findViewById(R.id.emailTxtRegister);
        passText = findViewById(R.id.passTxtRegister);
        confirmPassText = findViewById(R.id.passConfirmTxt);
        registerBtn = findViewById(R.id.registerBtn);
        Intent intent = new Intent(this, MainActivity.class);
        Activity ctx = this;
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passText.getText().toString();
                String confirmPass = confirmPassText.getText().toString();
                if(pass.equals(confirmPass)){
                    String email = emailTxt.getText().toString();
                    Auth.createAcc(email,pass,ctx,intent);
                }
            }
        });
    }
}