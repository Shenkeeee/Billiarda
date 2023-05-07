package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class BookingActivity extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        email = getIntent().getStringExtra("email");
    }


    public void back(View view) {
        finish();
    }


    public void newBooking(View view) {
        Intent intent = new Intent(this, NewBookingActivity.class);
        intent.putExtra("email", email);

        startActivity(intent);
    }

    public void history(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("email", email);

        startActivity(intent);
    }

    public void signout(View view) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
    }
}