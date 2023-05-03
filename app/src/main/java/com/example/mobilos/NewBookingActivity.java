package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class NewBookingActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private int yearSet;
    private int monthSet;
    private int daySet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbooking);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setDate(System.currentTimeMillis(), true, true);

        Calendar calendar = Calendar.getInstance();
        yearSet = calendar.get(Calendar.YEAR);
        monthSet = calendar.get(Calendar.MONTH) + 1; // Note: Months are zero-based, so add 1
        daySet = calendar.get(Calendar.DAY_OF_MONTH);
        
        // set selected to yellow
        int colorToPicked = getResources().getColor(R.color.yellow);
        calendarView.setFocusedMonthDateColor(colorToPicked);
        
        
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                 yearSet = year;
                 monthSet = month;
                 daySet = dayOfMonth;
            }
        });
    }
    public void back(View view) {
        finish();
    }

    public void selectBooking(View view) {

        Intent intent = new Intent(NewBookingActivity.this, NewBookingHoursActivity.class);
        intent.putExtra("year", yearSet);
        intent.putExtra("month", monthSet);
        intent.putExtra("day", daySet);
        startActivity(intent);
    }
}