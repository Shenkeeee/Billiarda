package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {


    public static final String S = "S";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Add this code to your Application class or main activity's onCreate method
        FirebaseApp.initializeApp(this);

    }

    public void quit(View view) {
        finish();
        System.exit(0);
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
}