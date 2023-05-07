package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {


    public static final String S = "S";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnContact = findViewById(R.id.btnContact);

        animateButton(btnLogin, -200);
        animateButton(btnRegister, 200);
        animateButton(btnContact, -200);
    }

    public void quit(View view) {
        finish();
        finishAffinity();
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);

//        SECRET KEY TO NOT BE ACCESSED FROM OUTSIDE
        intent.putExtra(S,99);
        startActivity(intent);
    }

    public void contact(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);

    }

    private void animateButton(Button button, float distance) {
        TranslateAnimation animation = new TranslateAnimation(0, distance, 0, 0);
        animation.setDuration(500);
        animation.setInterpolator(this, android.R.anim.accelerate_decelerate_interpolator);
        button.startAnimation(animation);
    }
}