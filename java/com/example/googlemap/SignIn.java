package com.example.googlemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.googlemap.Models.Admin;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private EditText AdminId, AdminPassword;
    private Button signInButton ,  gotoRegister;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        AdminId = findViewById(R.id.AdminID);
        AdminPassword = findViewById(R.id.AdminPassword);
        signInButton = findViewById(R.id.signInButton);
        gotoRegister = findViewById(R.id.gotoRegister);

        firebaseAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String idNumber = AdminId.getText().toString();
                String password = AdminPassword.getText().toString();

                if (idNumber.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignIn.this, "Please enter ID number and password", Toast.LENGTH_SHORT).show();

                } else {
                    Admin.SignInAdmin(SignIn.this, idNumber, password);

                }
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();

            }
        });
    }


}
