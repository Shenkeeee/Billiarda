package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class NewBookingActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private int yearSet;
    private int monthSet;
    private int daySet;

//    private int position;
//    private String modifEmail;
//    private int modifYear;
//    private int modifMonth;
//    private int modifDay;
//    private int modifHourStart;
//    private int modifHourEnd;
//    private List<Booking> bookingList;



    private String email;


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
        email = getIntent().getStringExtra("email");

//        modifEmail = getIntent().getStringExtra("modifEmail");
//        if(modifEmail != null && !Objects.equals(modifEmail, ""))
//        {
//            //position in the lis that we are modifying
//            position = getIntent().getIntExtra("pos",0);
//
//            modifYear = getIntent().getIntExtra("modifYear",0);
//            modifMonth = getIntent().getIntExtra("modifMonth",0);
//            modifDay = getIntent().getIntExtra("modifDay",0);
//            modifHourStart = getIntent().getIntExtra("modifHourStart",0);
//            modifHourEnd = getIntent().getIntExtra("modifHourEnd",0);
//            modifYear = getIntent().getIntExtra("modifYear",0);
//            bookingList = (ArrayList<Booking>) getIntent().getSerializableExtra("bookingList");
//
//
//
//            // Parse the date strings and set the date
//            calendar = Calendar.getInstance();
//            calendar.set(Calendar.YEAR, modifYear);
//            calendar.set(Calendar.MONTH, modifMonth);
//            calendar.set(Calendar.DAY_OF_MONTH, modifDay);
//            long timeInMillis = calendar.getTimeInMillis();
//            calendarView.setDate(timeInMillis, true, true);
//        }


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

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (yearSet < currentYear || (yearSet == currentYear && monthSet < currentMonth) || (yearSet == currentYear && monthSet == currentMonth && daySet <= currentDayOfMonth)) {
            Toast.makeText(this, "The first available date is tomorrow. Please re-select.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(NewBookingActivity.this, NewBookingHoursActivity.class);
        intent.putExtra("year", yearSet);
        intent.putExtra("month", monthSet);
        intent.putExtra("day", daySet);
        intent.putExtra("email", email);
//
//        //modifyables
//        intent.putExtra("pos", position);
//        intent.putExtra("modifEmail", modifEmail);
//        intent.putExtra("modifYear", modifYear);
//        intent.putExtra("modifMonth", modifMonth);
//        intent.putExtra("modifDay", modifDay);
//        intent.putExtra("modifHourStart", modifHourStart);
//        intent.putExtra("modifHourEnd", modifHourEnd);
//        intent.putExtra("bookingList", new ArrayList<>(bookingList));


        startActivity(intent);
    }
}