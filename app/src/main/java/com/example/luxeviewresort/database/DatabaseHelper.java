package com.example.luxeviewresort.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.luxeviewresort.R;
import com.example.luxeviewresort.models.BookedRoom;
import com.example.luxeviewresort.models.Room;
import com.example.luxeviewresort.models.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LuxeVista.db";
    private static final int DATABASE_VERSION = 11; // Increment version when modifying DB schema
    private final Context context;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ROOMS = "rooms";
    private static final String TABLE_SERVICES = "services";
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String TABLE_RESERVATIONS = "reservations";

    // Maximum image dimensions to prevent memory issues
    private static final int MAX_IMAGE_DIMENSION = 800;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)";
        String createRoomsTable = "CREATE TABLE " + TABLE_ROOMS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, image BLOB)";
        String createServicesTable = "CREATE TABLE " + TABLE_SERVICES + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL)";
        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, room_id INTEGER, " +
                "date TEXT, time TEXT, " + // Add these two fields
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

        populateRooms(db);
        populateServices(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);

        onCreate(db); // Recreate tables
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

    // Get User Details by Email
    public Pair<Integer, String> getUserIdAndName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name FROM " + TABLE_USERS + " WHERE email=?", new String[]{email});
        Pair<Integer, String> user = null;

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            String name = cursor.getString(1);
            user = new Pair<>(userId, name);
        }

        cursor.close();
        return user;  // Returns null if no user is found
    }

    // Helper method to resize bitmap
    private Bitmap resizeBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calculate new dimensions while maintaining aspect ratio
        float scale = Math.min(
                (float) MAX_IMAGE_DIMENSION / width,
                (float) MAX_IMAGE_DIMENSION / height
        );

        // Only resize if the image is larger than our maximum dimension
        if (scale < 1) {
            int newWidth = Math.round(width * scale);
            int newHeight = Math.round(height * scale);
            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }

        return bitmap;
    }

    // Convert Bitmap to Byte Array
    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;

        // Resize bitmap if necessary
        Bitmap resizedBitmap = resizeBitmap(bitmap);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Compress with lower quality to reduce size
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

        // Recycle the resized bitmap if it's different from the original
        if (resizedBitmap != bitmap) {
            resizedBitmap.recycle();
        }

        return outputStream.toByteArray();
    }


    // Convert Byte Array to Bitmap
    private Bitmap getByteArrayAsBitmap(byte[] imageData) {
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }

    // Populate Default Rooms
    private void populateRooms(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ROOMS, null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            ContentValues values = new ContentValues();

            // Load different images for each room
            Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.room1);
            byte[] imageBytes1 = getBitmapAsByteArray(bitmap1);

            Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.room2);
            byte[] imageBytes2 = getBitmapAsByteArray(bitmap2);

            Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.room3);
            byte[] imageBytes3 = getBitmapAsByteArray(bitmap3);

            Bitmap bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.room4);
            byte[] imageBytes4 = getBitmapAsByteArray(bitmap4);

            Bitmap bitmap5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.room5);
            byte[] imageBytes5 = getBitmapAsByteArray(bitmap5);

            values.put("name", "Deluxe Ocean View");
            values.put("price", 200);
            values.put("image", imageBytes1);
            db.insert(TABLE_ROOMS, null, values);

            values.clear();
            values.put("name", "Mountain Retreat");
            values.put("price", 180);
            values.put("image", imageBytes2);
            db.insert(TABLE_ROOMS, null, values);

            values.clear();
            values.put("name", "Garden Paradise");
            values.put("price", 190);
            values.put("image", imageBytes3);
            db.insert(TABLE_ROOMS, null, values);

            values.clear();
            values.put("name", "Luxury Penthouse");
            values.put("price", 350);
            values.put("image", imageBytes4);
            db.insert(TABLE_ROOMS, null, values);

            values.clear();
            values.put("name", "Cozy Cabin");
            values.put("price", 160);
            values.put("image", imageBytes5);
            db.insert(TABLE_ROOMS, null, values);
        }
        cursor.close();
    }

    // Populate Default Services
    private void populateServices(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_SERVICES, null);
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            ContentValues values = new ContentValues();

            values.put("name", "Spa Treatment");
            values.put("price", 50);
            db.insert(TABLE_SERVICES, null, values);

            values.clear();
            values.put("name", "Private Dining");
            values.put("price", 80);
            db.insert(TABLE_SERVICES, null, values);

            values.clear();
            values.put("name", "Reserve In-House Services");
            values.put("price", 100);
            db.insert(TABLE_SERVICES, null, values);

            values.clear();
            values.put("name", "Poolside Cabanas");
            values.put("price", 120);
            db.insert(TABLE_SERVICES, null, values);

            values.clear();
            values.put("name", "Guided Beach Tours");
            values.put("price", 150);
            db.insert(TABLE_SERVICES, null, values);
        }
        cursor.close();
    }

    // Insert a Room
    public boolean insertRoom(String name, double price, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("price", price);
        values.put("image", getBitmapAsByteArray(image)); // Convert and insert as BLOB

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

    // Get All Rooms
    public List<Room> getAllRooms2() {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Use a transaction to ensure data consistency
        db.beginTransaction();
        try {
            // Query rooms without the image first
            Cursor cursor = db.query(TABLE_ROOMS,
                    new String[]{"id", "name", "price"},
                    null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    double price = cursor.getDouble(2);

                    // Get image in a separate query
                    Bitmap image = getRoomImage(db, id);

                    roomList.add(new Room(id, name, price, image));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return roomList;
    }

    // Helper method to get room image separately
    private Bitmap getRoomImage(SQLiteDatabase db, int roomId) {
        Cursor cursor = db.query(TABLE_ROOMS,
                new String[]{"image"},
                "id = ?",
                new String[]{String.valueOf(roomId)},
                null, null, null);

        Bitmap image = null;
        if (cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(0);
            if (imageBytes != null) {
                image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }
        }
        cursor.close();
        return image;
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
    public List<BookedRoom> getUserBookings(int userId) {
        List<BookedRoom> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT rooms.id, rooms.name, rooms.price, bookings.date, bookings.time " +
                        "FROM rooms INNER JOIN bookings ON rooms.id = bookings.room_id " +
                        "WHERE bookings.user_id=?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);

                // Create a BookedRoom object with the booking date and time
                roomList.add(new BookedRoom(id, name, price, date, time));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return roomList;
    }

    // Book a Room with date and time
    public boolean bookRoom(int userId, int roomId, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("room_id", roomId);
        values.put("date", date);
        values.put("time", time);
        long result = db.insert(TABLE_BOOKINGS, null, values);
        return result != -1;
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