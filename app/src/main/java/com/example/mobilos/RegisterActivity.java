package com.example.mobilos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {



    private EditText txtEmail, txtPassword, txtPasswordConf;
    private Button btnRegisterAction;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        int xd = getIntent().getIntExtra("S",0);

        if(xd != 99)
        {
            finish();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtEmailL);
        txtPasswordConf = findViewById(R.id.txtPasswordL);
        btnRegisterAction = findViewById(R.id.btnLogin);

        btnRegisterAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String passwordConf = txtPasswordConf.getText().toString().trim();

                if (!Objects.equals(passwordConf, password)) {
                    Toast.makeText(RegisterActivity.this, "The two passwords do not match.", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "The password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "Please use a valid email adress.", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(email, password);
                }

            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        Toast.makeText(RegisterActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed
                        Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
              .addOnFailureListener(this, e -> {
                // Handle failure and display a more specific error message
                Toast.makeText(RegisterActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    public void back(View view) {
        finish();
    }

    public void register(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}