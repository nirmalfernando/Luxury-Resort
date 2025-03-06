package com.example.luxeviewresort.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxeviewresort.R;
import com.example.luxeviewresort.models.BookedRoom;

import java.util.List;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingViewHolder> {

    private Context context;
    private List<BookedRoom> bookingsList;
    private OnBookingClickListener onBookingClickListener;

    public interface OnBookingClickListener {
        void onBookingClick(int roomId);
    }

    public BookingsAdapter(Context context, List<BookedRoom> bookingsList, OnBookingClickListener listener) {
        this.context = context;
        this.bookingsList = bookingsList;
        this.onBookingClickListener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookedRoom booking = bookingsList.get(position);
        holder.roomTitle.setText(booking.getName());
        holder.roomPrice.setText("$" + booking.getPrice() + " per night");
        holder.bookingDate.setText("Check-in: " + booking.getBookingDate());
        holder.bookingTime.setText("Time: " + booking.getBookingTime());

        // You can set an onClick listener if needed
        holder.btnDetails.setOnClickListener(v ->
                onBookingClickListener.onBookingClick(booking.getId())
        );
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView roomTitle, roomPrice, bookingDate, bookingTime;
        Button btnDetails;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            roomTitle = itemView.findViewById(R.id.bookingRoomTitle);
            roomPrice = itemView.findViewById(R.id.bookingRoomPrice);
            bookingDate = itemView.findViewById(R.id.bookingDate);
            bookingTime = itemView.findViewById(R.id.bookingTime);
            btnDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}