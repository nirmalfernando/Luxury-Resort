package com.example.luxeviewresort.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener, ServiceAdapter.OnServiceClickListener {

    private RecyclerView rvRooms, rvServices;
    private RoomAdapter roomAdapter;
    private ServiceAdapter serviceAdapter;
    private DatabaseHelper databaseHelper;
    private Button btnProfile, btnLogout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI Components
        rvRooms = findViewById(R.id.rvRooms);
        rvServices = findViewById(R.id.rvServices);
        btnProfile = findViewById(R.id.nav_profile);
        btnLogout = findViewById(R.id.btnLogout);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Load Rooms
        List<Room> rooms = databaseHelper.getAllRooms();
        roomAdapter = new RoomAdapter(this, rooms, this);  // ✅ Pass 'this' as Room Click Listener
        rvRooms.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRooms.setAdapter(roomAdapter);

        // Load Services
        List<Service> services = databaseHelper.getAllServices();
        serviceAdapter = new ServiceAdapter(this, services, this); // ✅ Pass 'this' as Service Click Listener
        rvServices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvServices.setAdapter(serviceAdapter);

        // Profile Button Click
        btnProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        // Logout Button Click
        btnLogout.setOnClickListener(v -> {
            sessionManager.setLogin(false);
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
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