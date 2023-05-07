package com.example.mobilos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.checkerframework.checker.nullness.qual.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private String email;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        email = getIntent().getStringExtra("email");

        db = FirebaseFirestore.getInstance();
        CollectionReference bookingsCollection = db.collection("Bookings");

        // Query the bookings collection to fetch only the bookings with the provided email
        Query query = bookingsCollection.whereEqualTo("email", email);
//        Toast.makeText(this, email + " is listed", Toast.LENGTH_SHORT).show();

        // Retrieve the bookings matching the query
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                bookingList = queryDocumentSnapshots.toObjects(Booking.class);
                setupRecyclerView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                Log.e("HistoryActivity", "Failed to retrieve bookings: " + e.getMessage());
                Toast.makeText(HistoryActivity.this, "Failed to retrieve bookings", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(bookingList,
                new BookingAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteBooking(position);
            }
        }, new BookingAdapter.OnModifyClickListener() {
            @Override
            public void onModifyClick(int position) {
                modifyBooking(position);
            }
        });
        recyclerView.setAdapter(bookingAdapter);
    }


    public void back(View view) {
        finish();
    }

    public void modifyBooking(int position) {
//        // Get the booking to be modified
//        Booking oldBooking = bookingList.get(position);
//
//        Intent intent = new Intent(HistoryActivity.this, NewBookingActivity.class);
//        intent.putExtra("pos", position);
//
//        intent.putExtra("modifEmail", oldBooking.getEmail());
//        intent.putExtra("modifYear", oldBooking.getYear());
//        intent.putExtra("modifMonth", oldBooking.getMonth());
//        intent.putExtra("modifDay", oldBooking.getDay());
//        intent.putExtra("modifHourStart", oldBooking.getStartHour());
//        intent.putExtra("modifHourEnd", oldBooking.getEndHour());
//
//
//        intent.putExtra("bookingList", new ArrayList<>(bookingList));
//
//        startActivity(intent);

        Toast.makeText(HistoryActivity.this, "Modification succesful", Toast.LENGTH_SHORT).show();


    }

    // NOT used by newbookinghoursactivity
    public void modifyBooking(int position, Booking newBooking) {
        Booking oldBooking = bookingList.get(position);

        // Update the booking in the Firestore database
        db.collection("Bookings")
                .whereEqualTo("year", oldBooking.getYear())
                .whereEqualTo("month", oldBooking.getMonth())
                .whereEqualTo("day", oldBooking.getDay())
                .whereEqualTo("startHour", oldBooking.getStartHour())
                .whereEqualTo("endHour", oldBooking.getEndHour())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            db.collection("Bookings").document(documentId)
                                    .set(newBooking)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Update the booking in the RecyclerView
                                            bookingList.set(position, newBooking);
                                            bookingAdapter.notifyItemChanged(position);
                                            Toast.makeText(HistoryActivity.this, "Booking updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("HistoryActivity", "Failed to update booking: " + e.getMessage());
                                            Toast.makeText(HistoryActivity.this, "Failed to update booking", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("HistoryActivity", "Failed to retrieve booking: " + e.getMessage());
                        Toast.makeText(HistoryActivity.this, "Failed to retrieve booking", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void deleteBooking(int position) {
        Booking booking = bookingList.get(position);

        db.collection("Bookings")
                .whereEqualTo("year", booking.getYear())
                .whereEqualTo("month", booking.getMonth())
                .whereEqualTo("day", booking.getDay())
                .whereEqualTo("startHour", booking.getStartHour())
                .whereEqualTo("endHour", booking.getEndHour())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            db.collection("Bookings").document(documentId)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(HistoryActivity.this, "Booking deleted", Toast.LENGTH_SHORT).show();
                                            bookingList.remove(position);
                                            bookingAdapter.notifyItemRemoved(position);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("HistoryActivity", "Failed to delete booking: " + e.getMessage());
                                            Toast.makeText(HistoryActivity.this, "Failed to delete booking", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }



}
