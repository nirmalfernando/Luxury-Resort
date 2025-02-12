package com.example.luxeviewresort;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.luxeviewresort.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Directly start LoginActivity
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish(); // Close MainActivity so user can't go back to it
    }
}