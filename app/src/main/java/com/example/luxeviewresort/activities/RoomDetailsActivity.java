package com.example.luxeviewresort.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.Room;
import com.example.luxeviewresort.utils.SessionManager;

public class RoomDetailsActivity extends AppCompatActivity {

    private TextView tvRoomTitle, tvRoomPrice, tvRoomDescription;
    private Button btnBookRoom;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private int roomId;
    private ImageView roomImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_details);

        tvRoomTitle = findViewById(R.id.roomTitle);
        tvRoomPrice = findViewById(R.id.roomPrice);
        tvRoomDescription = findViewById(R.id.roomDescription);
        btnBookRoom = findViewById(R.id.btnBookRoom);
        roomImage = findViewById(R.id.roomImage);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Get Room ID from Intent
        roomId = getIntent().getIntExtra("room_id", -1);
        Room room = databaseHelper.getRoomById(roomId);

        if (room != null) {
            tvRoomTitle.setText(room.getName());
            tvRoomPrice.setText("$" + room.getPrice() + " per night");
            tvRoomDescription.setText("This luxurious suite includes all modern amenities.");

            // Load image
            Bitmap imageBitmap = room.getImage();
            if (imageBitmap != null) {
                roomImage.setImageBitmap(imageBitmap);
            } else {
                roomImage.setImageResource(R.drawable.room1); // Set default placeholder
            }
        }

        btnBookRoom.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                int userId = sessionManager.getUserId();
                boolean isBooked = databaseHelper.bookRoom(userId, roomId);

                if (isBooked) {
                    Toast.makeText(RoomDetailsActivity.this, "Room booked successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RoomDetailsActivity.this, ProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(RoomDetailsActivity.this, "Booking failed!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RoomDetailsActivity.this, "Please log in first!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}