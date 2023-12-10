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

public class SignUp extends AppCompatActivity {
    private EditText AdminID,Name, Phone, AdminPassword1;
    private Button signUpButton ,goToLogin;
    private FirebaseAuth firebaseAuth;
    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Admin admin = new Admin();

        AdminID = findViewById(R.id.AdminID);
        AdminPassword1 = findViewById(R.id.AdminPassword1);
        Name =findViewById(R.id.registerName);
        Phone =findViewById(R.id.registerPhone);;
        signUpButton = findViewById(R.id.signUpButton);

        firebaseAuth = FirebaseAuth.getInstance();
        goToLogin = findViewById(R.id.gotoLogin);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkField(AdminID) && checkField(AdminPassword1) && checkField(Name) && checkField(Phone)) {

                    admin.SignUpAdmin(SignUp.this,AdminID.getText().toString(), AdminPassword1.getText().toString(),Name.getText().toString());


                } else {

                    Toast.makeText(SignUp.this, "Please enter ID number and password", Toast.LENGTH_SHORT).show();

                }
            }
        });

        checkField(AdminID);
        checkField(AdminPassword1);
        checkField(Name);
        checkField(Phone);
    }
    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

}