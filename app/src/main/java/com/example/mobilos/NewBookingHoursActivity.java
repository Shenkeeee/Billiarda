package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobilos.exceptions.LigmaBallzException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewBookingHoursActivity extends AppCompatActivity {

    private int yearSet;
    private int monthSet;
    private int daySet;
    private int hourStartSet;
    private int hourEndSet;
    private List<Integer> hoursSelected = new ArrayList<>();
    private int[] clicked = new int[8]; // clicked = picked
    private TextView dateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbookinghours);

        //nothings clicked yet
        Arrays.fill(clicked, 0);

        yearSet = getIntent().getIntExtra("year",2023);
        monthSet = getIntent().getIntExtra("month",5);
        daySet = getIntent().getIntExtra("day",10);

        dateText = findViewById(R.id.txtWelcome);
        dateText.setText(yearSet + ". " + monthSet + ". " + daySet +".");


    }
    public void back(View view) {
        finish();
    }

    // gets which buttons are pressed and returns with their value
    public List<Integer> getPressed() throws LigmaBallzException {
        hoursSelected = new ArrayList<>();

        boolean foundClicked = false;
        for (int i = 0; i < clicked.length; i++) {

            if(clicked[i] == 1)
            {
                //if we have found one, then we check if the prev. one is picked too
                if(foundClicked){

                    if(clicked[i-1] == 0){
                        // The list is not consistently picked
                        throw new LigmaBallzException("Please select the hours consistently. bruh");
                    }
                }

                // if this is the 1st one that we have clicked on
                foundClicked = true;
                hoursSelected.add(getHourByIndex(i));

            }
        }

        return hoursSelected;
    }

    public int getHourByIndex(int index)
    {
        return index + 14;
    }

    public void onButtonClick(View view)
    {
        // gets the tag of the button -- its id
        int btnID = Integer.parseInt(view.getTag().toString());


        //todo if red -> throw exc

        //if 1 sets to 0, if 0 sets to 1
        clicked[btnID] = 1 - clicked[btnID];

        // if selected then set it to Dark
        int colorResId = (clicked[btnID] == 1) ? R.color.yellowDark : R.color.yellow;

        // finding the button with the specified ID
        int buttonId = getResources().getIdentifier("btn" + btnID, "id", getPackageName());
        Button btn = findViewById(buttonId);
        btn.setBackgroundTintList(ContextCompat.getColorStateList(this, colorResId));
    }

    public void selectBookingHour(View view){

        try {
            //adds the selected hours to the list of hours
            getPressed();
            hourStartSet = hoursSelected.get(0);
            hourEndSet = hoursSelected.get(hoursSelected.size()-1) +1; // +1 cuz if you choose 19 as an end point that means you are staying until 20
        }
        catch (LigmaBallzException e)
        {
            Toast.makeText(this, "Please select the hours consistently.", Toast.LENGTH_SHORT).show();
            return;
        }
        catch (Exception e)
        {
            Log.e("Booking Error", "Failed to create booking", e);
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Get Firestore instance
        CollectionReference bookingsCollection = db.collection("Bookings"); // Specify the collection name

        // Create a new Booking instance
        Booking booking = new Booking(yearSet, monthSet, daySet, hourStartSet, hourEndSet);

        // Upload the booking document to Firestore
        bookingsCollection.add(booking)
                .addOnSuccessListener(documentReference -> {
                    // Document uploaded successfully
                    Toast.makeText(this, yearSet +". " + monthSet + ". " + daySet + ". \n" + hourStartSet + "-" +  hourEndSet + " Booked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewBookingHoursActivity.this, BookingActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Error uploading the document
                    Toast.makeText(this, "Failed to create booking", Toast.LENGTH_SHORT).show();
                });
    }
}