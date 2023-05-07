package com.example.mobilos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.mobilos.exceptions.LigmaBallzException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NewBookingHoursActivity extends AppCompatActivity {

    private int yearSet;
    private int monthSet;
    private int daySet;
    private int hourStartSet;
    private int hourEndSet;

    //for modif
//
//    private int position;
//    private String modifEmail;
//    private int modifYear;
//    private int modifMonth;
//    private int modifDay;
//    private int modifHourStart;
//    private int modifHourEnd;
//    private List<Booking> bookingList;



    private List<Integer> hoursSelected = new ArrayList<>();
    private int[] clicked = new int[8]; // clicked = picked
    private TextView dateText;
    private static final String CHANNEL_ID = "booking_channel";
    private static int NOTIFICATION_ID = 1;
    private String email;


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

//        // are we modifying?
//        modifEmail = getIntent().getStringExtra("modifEmail");
//        if(modifEmail != null && !Objects.equals(modifEmail, ""))
//        {
//            position = getIntent().getIntExtra("pos",0);
//            modifYear = getIntent().getIntExtra("modifYear",0);
//            modifMonth = getIntent().getIntExtra("modifMonth",0);
//            modifDay = getIntent().getIntExtra("modifDay",0);
//            modifHourStart = getIntent().getIntExtra("modifHourStart",0);
//            modifHourEnd = getIntent().getIntExtra("modifHourEnd",0);
//            modifYear = getIntent().getIntExtra("modifYear",0);
//            bookingList = (ArrayList<Booking>) getIntent().getSerializableExtra("bookingList");
//
//        }

        email = getIntent().getStringExtra("email");

    }
    public void back(View view) {
        finish();
    }

    private void sendNotification(String message) {
        // Create the notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Booking Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Booking done")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        NOTIFICATION_ID++;
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
        Booking booking = new Booking(email, yearSet, monthSet, daySet, hourStartSet, hourEndSet);

//        // if we are modifying
//        if(modifEmail != null && !Objects.equals(modifEmail, "")){
//            Intent intent = getIntent();
//
////            already set
////            ArrayList<Booking> bookingList = intent.getParcelableArrayListExtra("bookingList");
//            int position = intent.getIntExtra("position", -1);
//            if (position != -1) {
//                bookingList.set(position, booking);
//                db.collection("Bookings")
//                        .whereEqualTo("year", booking.getYear())
//                        .whereEqualTo("month", booking.getMonth())
//                        .whereEqualTo("day", booking.getDay())
//                        .whereEqualTo("startHour", booking.getStartHour())
//                        .whereEqualTo("endHour", booking.getEndHour())
//                        .get()
//                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                if (!queryDocumentSnapshots.isEmpty()) {
//                                    // There should be only one document that matches the query
//                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
//                                    String documentId = documentSnapshot.getId();
//
//                                    // Update the document with the new values
//                                    db.collection("Bookings")
//                                            .document(documentId)
//                                            .update("year", booking.getYear(),
//                                                    "month", booking.getMonth(),
//                                                    "day", booking.getDay(),
//                                                    "startHour", booking.getStartHour(),
//                                                    "endHour", booking.getEndHour(),
//                                                    "email", booking.getEmail())
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    // Document updated successfully
//                                                    Toast.makeText(NewBookingHoursActivity.this, "Booking updated successfully!", Toast.LENGTH_SHORT).show();
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.e("NewBookingHoursActivity", "Error updating booking: " + e.getMessage());
//                                                    Toast.makeText(NewBookingHoursActivity.this, "Error updating booking", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                } else {
//                                    // No documents match the query
//                                    Toast.makeText(NewBookingHoursActivity.this, "No booking found to update!", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e("NewBookingHoursActivity", "Error retrieving booking: " + e.getMessage());
//                                Toast.makeText(NewBookingHoursActivity.this, "Error retrieving booking", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//            } else {
//                Toast.makeText(this, "Failed to modify booking", Toast.LENGTH_SHORT).show();
//            }
//        }



        // Upload the booking document to Firestore
        bookingsCollection.add(booking)
                .addOnSuccessListener(documentReference -> {
                    // Document uploaded successfully
                    Toast.makeText(this, yearSet +". " + monthSet + ". " + daySet + ". \n" + hourStartSet + "-" +  hourEndSet + " Booked!", Toast.LENGTH_SHORT).show();
                    sendNotification("Booking succesful! Thank you for choosing us!"); // Send notification to the user
                    Intent intent = new Intent(NewBookingHoursActivity.this, BookingActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Error uploading the document
                    Toast.makeText(this, "Failed to create booking", Toast.LENGTH_SHORT).show();
                });
    }
}