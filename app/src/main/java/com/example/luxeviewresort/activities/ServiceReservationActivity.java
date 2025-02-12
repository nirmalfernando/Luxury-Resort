package com.example.luxeviewresort.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.adapters.ServiceAdapter;
import com.example.luxeviewresort.database.DatabaseHelper;
import com.example.luxeviewresort.models.Service;
import com.example.luxeviewresort.utils.SessionManager;
import java.util.Calendar;
import java.util.List;

public class ServiceReservationActivity extends AppCompatActivity implements ServiceAdapter.OnServiceClickListener {

    private RecyclerView rvServices;
    private DatabaseHelper databaseHelper;
    private ServiceAdapter serviceAdapter;
    private SessionManager sessionManager;
    private EditText etSelectedDate, etSelectedTime;
    private Button btnConfirmBooking;
    private int selectedServiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_reservation);

        rvServices = findViewById(R.id.rvServices);
        etSelectedDate = findViewById(R.id.etSelectedDate);
        etSelectedTime = findViewById(R.id.etSelectedTime);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        List<Service> serviceList = databaseHelper.getAllServices();
        if (serviceList.isEmpty()) {
            Toast.makeText(this, "No services available", Toast.LENGTH_SHORT).show();
        } else {
            serviceAdapter = new ServiceAdapter(this, serviceList, this);
            rvServices.setLayoutManager(new LinearLayoutManager(this));
            rvServices.setAdapter(serviceAdapter);
        }

        etSelectedDate.setOnClickListener(v -> showDatePicker());
        etSelectedTime.setOnClickListener(v -> showTimePicker());

        btnConfirmBooking.setOnClickListener(v -> {
            if (selectedServiceId == 0 || etSelectedDate.getText().toString().isEmpty() || etSelectedTime.getText().toString().isEmpty()) {
                Toast.makeText(ServiceReservationActivity.this, "Please select a service, date, and time", Toast.LENGTH_SHORT).show();
            } else {
                int userId = sessionManager.getUserId();
                boolean isBooked = databaseHelper.bookService(userId, selectedServiceId, etSelectedDate.getText().toString(), etSelectedTime.getText().toString());

                if (isBooked) {
                    Toast.makeText(ServiceReservationActivity.this, "Service booked successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ServiceReservationActivity.this, "Booking failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onServiceClick(int serviceId) {
        selectedServiceId = serviceId;
        Toast.makeText(this, "Selected Service ID: " + serviceId, Toast.LENGTH_SHORT).show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> etSelectedDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> etSelectedTime.setText(hourOfDay + ":" + minute),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
}