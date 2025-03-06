package com.example.luxeviewresort.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxeviewresort.R;
import com.example.luxeviewresort.adapters.BookingsAdapter;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.BookedRoom;
import com.example.luxeviewresort.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements BookingsAdapter.OnBookingClickListener {

    private TextView tvUserName, tvUserEmail;
    private RecyclerView rvBookings;
    private BookingsAdapter bookingsAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Button btnLogout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI Components
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        rvBookings = findViewById(R.id.rvBookings);
        btnLogout = findViewById(R.id.btnLogout);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            int userId = sessionManager.getUserId();
            String userName = sessionManager.getUserName();
            String userEmail = sessionManager.getUserEmail();

            tvUserName.setText(userName);
            tvUserEmail.setText(userEmail);

            // Load User's Booked Rooms with date and time information
            List<BookedRoom> bookings = databaseHelper.getUserBookings(userId);

            if (bookings.isEmpty()) {
                // Handle the case when there are no bookings
                // You could show a message or hide the RecyclerView
            } else {
                bookingsAdapter = new BookingsAdapter(this, bookings, this);
                rvBookings.setLayoutManager(new LinearLayoutManager(this));
                rvBookings.setAdapter(bookingsAdapter);
            }
        }

        // Logout Button Click
        btnLogout.setOnClickListener(v -> {
            sessionManager.setLogin(false);
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        // Handle Bottom Navigation Clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(this, RoomBookingActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    // Handle Booking Clicks
    @Override
    public void onBookingClick(int roomId) {
        Intent intent = new Intent(ProfileActivity.this, RoomDetailsActivity.class);
        intent.putExtra("room_id", roomId);
        startActivity(intent);
    }
}