package com.example.luxeviewresort.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.adapters.RoomAdapter;
import com.example.luxeviewresort.adapters.ServiceAdapter;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.Room;
import com.example.luxeviewresort.models.Service;
import com.example.luxeviewresort.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener, ServiceAdapter.OnServiceClickListener {

    private RecyclerView rvRooms, rvServices;
    private RoomAdapter roomAdapter;
    private ServiceAdapter serviceAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI Components
        rvRooms = findViewById(R.id.rvRooms);
        rvServices = findViewById(R.id.rvServices);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        int userId = sessionManager.getUserId();
        Log.d("Home Activity UserId", "UserId: " +userId);

        // Load Rooms
        List<Room> rooms = databaseHelper.getAllRooms2();
        roomAdapter = new RoomAdapter(this, rooms, this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRooms.setAdapter(roomAdapter);

        // Load Services
        List<Service> services = databaseHelper.getAllServices();
        serviceAdapter = new ServiceAdapter(this, services, this);
        rvServices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvServices.setAdapter(serviceAdapter);

        // Handle Bottom Navigation Clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(this, RoomBookingActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    // Handle Room Clicks
    @Override
    public void onRoomClick(int roomId) {
        Intent intent = new Intent(HomeActivity.this, RoomDetailsActivity.class);
        intent.putExtra("room_id", roomId);
        startActivity(intent);
    }

    // Handle Service Clicks
    @Override
    public void onServiceClick(int serviceId) {
        Intent intent = new Intent(HomeActivity.this, ServiceReservationActivity.class);
        intent.putExtra("service_id", serviceId);
        startActivity(intent);
    }
}