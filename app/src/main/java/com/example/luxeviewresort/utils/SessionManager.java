package com.example.luxeviewresort.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import java.nio.charset.StandardCharsets;

public class SessionManager {
    private static final String PREF_NAME = "LuxeVistaSession";
    private static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final String KEY_USER_ROLE = "USER_ROLE"; // "admin" or "user"
    private static final String KEY_SESSION_EXPIRY = "SESSION_EXPIRY"; // New: Session Expiry

    private static final long SESSION_DURATION = 86400000; // 24 hours in milliseconds

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Set login status with session expiry
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        if (isLoggedIn) {
            editor.putLong(KEY_SESSION_EXPIRY, System.currentTimeMillis() + SESSION_DURATION);
        } else {
            editor.remove(KEY_SESSION_EXPIRY);
        }
        editor.apply();
    }

    // Check if user is logged in and session is active
    public boolean isLoggedIn() {
        long expiryTime = sharedPreferences.getLong(KEY_SESSION_EXPIRY, 0);
        if (System.currentTimeMillis() > expiryTime) {
            logout(); // Auto logout if session expired
            return false;
        }
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Save user details securely (Base64 encoding)
    public void saveUserDetails(int userId, String name, String email) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, encode(name));
        editor.putString(KEY_USER_EMAIL, encode(email));
        editor.putLong(KEY_SESSION_EXPIRY, System.currentTimeMillis() + SESSION_DURATION);
        editor.apply();
    }

    // Save user by email
    public void saveUserEmail (String email) {
        editor.putString(KEY_USER_EMAIL, encode(email));
        editor.apply();
    }

    // Get User ID
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    // Get User Name (Decoded)
    public String getUserName() {
        return decode(sharedPreferences.getString(KEY_USER_NAME, ""));
    }

    // Get User Email (Decoded)
    public String getUserEmail() {
        return decode(sharedPreferences.getString(KEY_USER_EMAIL, ""));
    }

    // Get User Role
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "user"); // Default to "user"
    }

    // Check if user exists in session
    public boolean hasUser() {
        return sharedPreferences.contains(KEY_USER_ID);
    }

    // Logout function
    public void logout() {
        editor.clear();
        editor.apply();
    }

    // Encode sensitive data to Base64
    private String encode(String data) {
        return Base64.encodeToString(data.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
    }

    // Decode Base64 encoded data
    private String decode(String data) {
        return new String(Base64.decode(data, Base64.NO_WRAP), StandardCharsets.UTF_8);
    }
}