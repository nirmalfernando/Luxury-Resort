package com.example.luxeviewresort.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("LuxeVistaSession", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean("IS_LOGGED_IN", isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false);
    }

    public void saveUserDetails(int userId, String name, String email) {
        editor.putInt("USER_ID", userId);
        editor.putString("USER_NAME", name);
        editor.putString("USER_EMAIL", email);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt("USER_ID", -1);
    }

    public String getUserName() {
        return sharedPreferences.getString("USER_NAME", "");
    }

    public String getUserEmail() {
        return sharedPreferences.getString("USER_EMAIL", "");
    }
}