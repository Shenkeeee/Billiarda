package com.example.mobilos;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Calendar;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    Button btnDelete;
    Button btnModify;
    private List<Booking> bookingList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnModifyClickListener onModifyClickListener;

    public BookingAdapter(List<Booking> bookingList, OnDeleteClickListener onDeleteClickListener, OnModifyClickListener onModifyClickListener) {
        this.bookingList = bookingList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onModifyClickListener = onModifyClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
    public interface OnModifyClickListener {
        void onModifyClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.txtDate.setText(booking.getYear() +". " + booking.getMonth() +". " + booking.getDay());
        holder.txtTime.setText(booking.getStartHour() +":00-" + booking.getEndHour() + ":00");


        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        // Check if the date of the booking is less than the current date
        if (booking.getYear() < currentYear || (booking.getYear() == currentYear && booking.getMonth() < currentMonth) || (booking.getYear() == currentYear && booking.getMonth() == currentMonth &&  booking.getDay() <= currentDayOfMonth)) {
            // Hide the modify button
            btnModify.setVisibility(View.GONE);
        } else {
            // Show the modify button
            btnModify.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnModify = itemView.findViewById(R.id.btnModify);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Call the onDeleteClick method of the listener
                        onDeleteClickListener.onDeleteClick(position);
                    }
                }
            });

            btnModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Call the onDeleteClick method of the listener
                        onModifyClickListener.onModifyClick(position);
                    }
                }
            });
        }
    }
}
