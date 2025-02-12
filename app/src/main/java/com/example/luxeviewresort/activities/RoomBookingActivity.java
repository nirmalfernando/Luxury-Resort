package com.example.luxeviewresort.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.adapters.RoomAdapter;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.Room;
import java.util.List;

public class RoomBookingActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private RecyclerView rvRooms;
    private DatabaseHelper databaseHelper;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking);

        rvRooms = findViewById(R.id.rvRooms);
        databaseHelper = new DatabaseHelper(this);

        List<Room> roomList = databaseHelper.getAllRooms();
        if (roomList.isEmpty()) {
            Toast.makeText(this, "No rooms available", Toast.LENGTH_SHORT).show();
        } else {
            roomAdapter = new RoomAdapter(this, roomList, this);
            rvRooms.setLayoutManager(new LinearLayoutManager(this));
            rvRooms.setAdapter(roomAdapter);
        }
    }

    @Override
    public void onRoomClick(int roomId) {
        Intent intent = new Intent(RoomBookingActivity.this, RoomDetailsActivity.class);
        intent.putExtra("room_id", roomId);
        startActivity(intent);
    }
}