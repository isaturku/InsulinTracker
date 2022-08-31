package com.example.insulintracker.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    private static FirebaseAuth mAuth;

    public static void init(){
        mAuth = FirebaseAuth.getInstance();
    }

    public static void createAcc(String email, String pass,Activity ctx, Intent intent){
        final boolean[] success = {false};
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                            ctx.startActivity(intent);
                    }
                });
    }
    public static void login(String email, String pass, Activity ctx, Intent intent){
        final boolean[] success = {false};
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            ctx.startActivity(intent);
                    }
                });
    }

    public static void logout(){
        mAuth.signOut();
    }
    public static FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }
}
