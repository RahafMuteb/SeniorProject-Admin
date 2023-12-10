package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googlemap.Models.Admin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Admin")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Globals.user = snapshot.getValue(Admin.class);
                                    Intent intent = new Intent(SplashScreen.this , SignIn.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
                else
                {
                    Intent intent = new Intent(SplashScreen.this , SignIn.class);
                    startActivity(intent);
                    finish();
                }

            }
        },2500);
    }
}