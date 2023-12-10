package com.example.googlemap.Models;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.googlemap.Globals;
import com.example.googlemap.HomeActivity;
import com.example.googlemap.SignIn;
import com.example.googlemap.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Admin {
    String email, password;
    private String name;
    private String phone;


    public Admin() {

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private static FirebaseAuth firebaseAuth;


    public void SignUpAdmin(SignUp context, String idNumber1, String password1, String name) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String email = idNumber1 + "@example.com"; // Concatenate the ID number with a domain to form the email address

        firebaseAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Admin admin = new Admin();
                            admin.setEmail(email);
                            admin.setName(name);
                            admin.setPassword(password1);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("Admin")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(admin);
                            Globals.user = admin;

                            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, SignIn.class));
                            context.finish();
                        } else {
                            Toast.makeText(context, "Registration failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public static void SignInAdmin(SignIn context, String idNumber, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        String email = idNumber + "@example.com"; // Concatenate the ID number with a domain to form the email address

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, HomeActivity.class));


                        } else {
                            Toast.makeText(context, "Sign in failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}