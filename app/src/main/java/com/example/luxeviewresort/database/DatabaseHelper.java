package com.example.luxeviewresort.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.luxeviewresort.models.Room;
import com.example.luxeviewresort.models.Service;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LuxeVista.db";
    private static final int DATABASE_VERSION = 2; // Increment version when modifying DB schema

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ROOMS = "rooms";
    private static final String TABLE_SERVICES = "services";
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String TABLE_RESERVATIONS = "reservations";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)";
        String createRoomsTable = "CREATE TABLE " + TABLE_ROOMS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, image TEXT)";
        String createServicesTable = "CREATE TABLE " + TABLE_SERVICES + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL)";
        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, room_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(room_id) REFERENCES rooms(id) ON DELETE CASCADE)";

        String createReservationsTable = "CREATE TABLE " + TABLE_RESERVATIONS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, service_id INTEGER, date TEXT, time TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(service_id) REFERENCES services(id) ON DELETE CASCADE)";

        db.execSQL(createUserTable);
        db.execSQL(createRoomsTable);
        db.execSQL(createServicesTable);
        db.execSQL(createBookingsTable);
        db.execSQL(createReservationsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
            String createReservationsTable = "CREATE TABLE " + TABLE_RESERVATIONS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, service_id INTEGER, date TEXT, time TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(service_id) REFERENCES services(id) ON DELETE CASCADE)";
            db.execSQL(createReservationsTable);
        }
    }

    // Insert User
    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Check User Login
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email=? AND password=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Get User ID by Email
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_USERS + " WHERE email=?", new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    // Insert a Room
    public boolean insertRoom(String name, double price, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image", image);
        long result = db.insert(TABLE_ROOMS, null, values);
        return result != -1;
    }

    // Get All Rooms
    public List<Room> getAllRooms() {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ROOMS, null);

        if (cursor.moveToFirst()) {
            do {
                roomList.add(new Room(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomList;
    }

    // Get Room by ID
    public Room getRoomById(int roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ROOMS + " WHERE id=?", new String[]{String.valueOf(roomId)});

        if (cursor.moveToFirst()) {
            Room room = new Room(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2));
            cursor.close();
            return room;
        }
        cursor.close();
        return null;
    }


    // Book a Room
    public boolean bookRoom(int userId, int roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("room_id", roomId);
        long result = db.insert(TABLE_BOOKINGS, null, values);
        return result != -1;
    }

    // Get User's Booked Rooms
    public List<Room> getUserBookings(int userId) {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT rooms.id, rooms.name, rooms.price FROM rooms INNER JOIN bookings ON rooms.id = bookings.room_id WHERE bookings.user_id=?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                roomList.add(new Room(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomList;
    }

    // Insert a Service
    public boolean insertService(String name, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        long result = db.insert(TABLE_SERVICES, null, values);
        return result != -1;
    }

    // Get All Services
    public List<Service> getAllServices() {
        List<Service> serviceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SERVICES, null);

        if (cursor.moveToFirst()) {
            do {
                serviceList.add(new Service(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return serviceList;
    }

    // Book a Service
    public boolean bookService(int userId, int serviceId, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("service_id", serviceId);
        values.put("date", date);
        values.put("time", time);
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        return result != -1;
    }

    // Get User's Booked Services
    public List<Service> getUserReservations(int userId) {
        List<Service> serviceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT services.id, services.name, services.price FROM services INNER JOIN reservations ON services.id = reservations.service_id WHERE reservations.user_id=?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                serviceList.add(new Service(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return serviceList;
    }
}