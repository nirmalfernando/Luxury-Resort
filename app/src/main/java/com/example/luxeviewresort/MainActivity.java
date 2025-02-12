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

        // Check login status and navigate accordingly
        Intent intent;
        if (sessionManager.isLoggedIn()) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }

        // Add flags to clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
