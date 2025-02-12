package com.example.luxeviewresort.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.adapters.RoomAdapter;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.Room;
import com.example.luxeviewresort.utils.SessionManager;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private TextView tvUserName, tvUserEmail;
    private RecyclerView rvBookings;
    private RoomAdapter roomAdapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI Components
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        rvBookings = findViewById(R.id.rvBookings);
        btnLogout = findViewById(R.id.btnLogout);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            int userId = sessionManager.getUserId();
            String userName = sessionManager.getUserName();
            String userEmail = sessionManager.getUserEmail();

            tvUserName.setText(userName);
            tvUserEmail.setText(userEmail);

            // Load User's Booked Rooms
            List<Room> bookings = databaseHelper.getUserBookings(userId);
            roomAdapter = new RoomAdapter(this, bookings, this);
            rvBookings.setLayoutManager(new LinearLayoutManager(this));
            rvBookings.setAdapter(roomAdapter);
        }

        // Logout Button Click
        btnLogout.setOnClickListener(v -> {
            sessionManager.setLogin(false);
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    // Handle Room Clicks
    @Override
    public void onRoomClick(int roomId) {
        Intent intent = new Intent(ProfileActivity.this, RoomDetailsActivity.class);
        intent.putExtra("room_id", roomId);
        startActivity(intent);
    }
}