package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmailL, txtPasswordL;
    private Button btnLogin;



    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        txtEmailL = findViewById(R.id.txtEmailL);
        txtPasswordL = findViewById(R.id.txtPasswordL);
        btnLogin = findViewById(R.id.btnLogin);

        // Retrieve email and password values from extras -- for autofiling
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        // Set email and password values in EditText fields
        if (email != null) {
            txtEmailL.setText(email);
        }

        if (password != null) {
            txtPasswordL.setText(password);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmailL.getText().toString().trim();
                String password = txtPasswordL.getText().toString().trim();

                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, BookingActivity.class);
                        intent.putExtra("email", email); // Pass the email as an extra
                        startActivity(intent);
                        finish();
                    } else {
                        // Login failed
                        Toast.makeText(LoginActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();

                        // Shake animation on the button
                        Button loginButton = findViewById(R.id.btnLogin);
                        Animation shakeAnimation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake_animation);
                        loginButton.startAnimation(shakeAnimation);
                    }
                })
                .addOnFailureListener(this, e -> {
                    // Handle failure and display a more specific error message
                    Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void back(View view) {
        finish();
    }
}
