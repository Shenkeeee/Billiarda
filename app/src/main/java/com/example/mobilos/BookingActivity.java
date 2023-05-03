package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
    }


    public void back(View view) {
        finish();
    }


    public void newBooking(View view) {
        Intent intent = new Intent(this, NewBookingActivity.class);
        startActivity(intent);
    }

    public void history(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

}