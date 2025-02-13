package com.example.luxeviewresort.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.adapters.RoomAdapter;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.Room;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class RoomBookingActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private RecyclerView rvRooms;
    private DatabaseHelper databaseHelper;
    private RoomAdapter roomAdapter;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking);

        rvRooms = findViewById(R.id.rvAvailableRooms);
        databaseHelper = new DatabaseHelper(this);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(RoomBookingActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Optional: Close the current activity
        });

        List<Room> roomList = databaseHelper.getAllRooms2();
        if (roomList.isEmpty()) {
            Toast.makeText(this, "No rooms available", Toast.LENGTH_SHORT).show();
        } else {
            roomAdapter = new RoomAdapter(this, roomList, this);
            rvRooms.setLayoutManager(new LinearLayoutManager(this));
            rvRooms.setAdapter(roomAdapter);
        }

        // Handle Bottom Navigation Clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_bookings) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    public void onRoomClick(int roomId) {
        Intent intent = new Intent(RoomBookingActivity.this, RoomDetailsActivity.class);
        intent.putExtra("room_id", roomId);
        startActivity(intent);
    }
}