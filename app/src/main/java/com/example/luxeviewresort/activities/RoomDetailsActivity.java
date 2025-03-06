package com.example.luxeviewresort.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luxeviewresort.R;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.Room;
import com.example.luxeviewresort.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RoomDetailsActivity extends AppCompatActivity {

    private TextView tvRoomTitle, tvRoomPrice, tvRoomDescription;
    private Button btnBookRoom;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private int roomId;
    private ImageView roomImage;
    private TextInputEditText etCheckInDate, etCheckInTime;
    private String selectedDate = "";
    private String selectedTime = "";
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_details);

        // Initialize views
        tvRoomTitle = findViewById(R.id.roomTitle);
        tvRoomPrice = findViewById(R.id.roomPrice);
        tvRoomDescription = findViewById(R.id.roomDescription);
        btnBookRoom = findViewById(R.id.btnBookRoom);
        roomImage = findViewById(R.id.roomImage);
        etCheckInDate = findViewById(R.id.etCheckInDate);
        etCheckInTime = findViewById(R.id.etCheckInTime);

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

        // Set up date picker
        etCheckInDate.setOnClickListener(v -> showDatePickerDialog());

        // Set up time picker
        etCheckInTime.setOnClickListener(v -> showTimePickerDialog());

        btnBookRoom.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                // Validate date and time selection
                if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
                    Toast.makeText(RoomDetailsActivity.this, "Please select check-in date and time",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int userId = sessionManager.getUserId();
                // Add date and time to booking
                boolean isBooked = databaseHelper.bookRoom(userId, roomId, selectedDate, selectedTime);

                if (isBooked) {
                    Toast.makeText(RoomDetailsActivity.this, "Room booked successfully!",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RoomDetailsActivity.this, ProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(RoomDetailsActivity.this, "Booking failed!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RoomDetailsActivity.this, "Please log in first!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Show date picker dialog
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateField();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    // Show time picker dialog
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    updateTimeField();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    // Update date field after selection
    private void updateDateField() {
        selectedDate = dateFormat.format(calendar.getTime());
        etCheckInDate.setText(selectedDate);
    }

    // Update time field after selection
    private void updateTimeField() {
        selectedTime = timeFormat.format(calendar.getTime());
        etCheckInTime.setText(selectedTime);
    }
}