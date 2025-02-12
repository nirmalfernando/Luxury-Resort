package com.example.luxeviewresort;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.luxeviewresort.activities.HomeActivity;
import com.example.luxeviewresort.activities.LoginActivity;
import com.example.luxeviewresort.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class)); // Navigate to Home
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class)); // Navigate to Login
        }

        finish(); // Close MainActivity so user can't go back to it
    }
}