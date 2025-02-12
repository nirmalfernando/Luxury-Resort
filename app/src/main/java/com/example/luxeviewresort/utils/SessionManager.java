package com.example.luxeviewresort.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "LuxeVistaSession";
    private static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final String KEY_USER_ROLE = "USER_ROLE"; // New: To store Admin/User role

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Set login status
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Save user details
    public void saveUserDetails(int userId, String name, String email, String role) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ROLE, role); // Save user role
        editor.apply();
    }

    // Get User ID
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    // Get User Name
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    // Get User Email
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
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
}