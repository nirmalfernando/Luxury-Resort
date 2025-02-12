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

public class HomeActivity extends AppCompatActivity {

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

        rvRooms = findViewById(R.id.rvRooms);
        rvServices = findViewById(R.id.rvServices);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        List<Room> rooms = databaseHelper.getAllRooms();
        roomAdapter = new RoomAdapter(this, rooms);
        rvRooms.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRooms.setAdapter(roomAdapter);

        List<Service> services = databaseHelper.getAllServices();
        serviceAdapter = new ServiceAdapter(this, services);
        rvServices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvServices.setAdapter(serviceAdapter);

        btnProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        btnLogout.setOnClickListener(v -> {
            sessionManager.setLogin(false);
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });
    }
}