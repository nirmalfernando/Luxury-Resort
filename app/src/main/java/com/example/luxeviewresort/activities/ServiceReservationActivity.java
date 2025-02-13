package com.example.luxeviewresort.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.adapters.ServiceAdapter;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.Service;
import com.example.luxeviewresort.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServiceReservationActivity extends AppCompatActivity implements ServiceAdapter.OnServiceClickListener {

    private RecyclerView rvServices;
    private DatabaseHelper databaseHelper;
    private ServiceAdapter serviceAdapter;
    private SessionManager sessionManager;
    private EditText etSelectedDate, etSelectedTime;
    private Button btnConfirmBooking;
    private int selectedServiceId;

    // Date formatting
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

    // Minimum advance booking time (in hours)
    private static final int MIN_ADVANCE_BOOKING_HOURS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_reservation);

        initializeViews();
        setupDatabase();
        loadServices();
        setupListeners();
    }

    private void initializeViews() {
        rvServices = findViewById(R.id.rvServices);
        etSelectedDate = findViewById(R.id.etSelectedDate);
        etSelectedTime = findViewById(R.id.etSelectedTime);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        // Make EditTexts non-editable
        etSelectedDate.setFocusable(false);
        etSelectedTime.setFocusable(false);
    }

    private void setupDatabase() {
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
    }

    private void loadServices() {
        List<Service> serviceList = databaseHelper.getAllServices();
        if (serviceList.isEmpty()) {
            Toast.makeText(this, "No services available at the moment", Toast.LENGTH_SHORT).show();
            btnConfirmBooking.setEnabled(false);
        } else {
            serviceAdapter = new ServiceAdapter(this, serviceList, this);
            rvServices.setLayoutManager(new LinearLayoutManager(this));
            rvServices.setAdapter(serviceAdapter);
        }
    }

    private void setupListeners() {
        etSelectedDate.setOnClickListener(v -> showDatePicker());
        etSelectedTime.setOnClickListener(v -> showTimePicker());
        btnConfirmBooking.setOnClickListener(v -> validateAndBook());
    }

    private void validateAndBook() {
        if (!isValidBooking()) {
            return;
        }

        try {
            int userId = sessionManager.getUserId();
            String date = etSelectedDate.getText().toString();
            String time = etSelectedTime.getText().toString();

            boolean isBooked = databaseHelper.bookService(userId, selectedServiceId, date, time);
            if (isBooked) {
                Toast.makeText(this, "Service booked successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidBooking() {
        if (selectedServiceId == 0) {
            Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etSelectedDate.getText().toString().isEmpty() || etSelectedTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select both date and time", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            // Validate selected date and time
            Calendar selectedDateTime = Calendar.getInstance();
            selectedDateTime.setTime(dateFormatter.parse(etSelectedDate.getText().toString()));
            String[] timeParts = etSelectedTime.getText().toString().split(":");
            selectedDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
            selectedDateTime.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));

            Calendar minDateTime = Calendar.getInstance();
            minDateTime.add(Calendar.HOUR_OF_DAY, MIN_ADVANCE_BOOKING_HOURS);

            if (selectedDateTime.before(minDateTime)) {
                Toast.makeText(this,
                        "Please book at least " + MIN_ADVANCE_BOOKING_HOURS + " hours in advance",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date or time format", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onServiceClick(int serviceId) {
        selectedServiceId = serviceId;
        Toast.makeText(this, "Service selected", Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, MIN_ADVANCE_BOOKING_HOURS);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    etSelectedDate.setText(dateFormatter.format(selectedDate.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        // Set maximum date to 3 months from now
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 3);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    Calendar selectedTime = Calendar.getInstance();
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedTime.set(Calendar.MINUTE, minute);
                    etSelectedTime.setText(timeFormatter.format(selectedTime.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this)
        );

        timePickerDialog.show();
    }
}