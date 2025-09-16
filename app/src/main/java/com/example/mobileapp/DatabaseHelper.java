package com.example.mobileapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "HotelBookingDB";
    private static final int DB_VERSION = 4; // Incremented to 4 for new column
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_PROMOTIONS = "Promotions";
    public static final String TABLE_ATTRACTIONS = "Attractions";
    public static final String TABLE_ROOMS = "Rooms";
    public static final String TABLE_BOOKINGS = "Bookings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ROOM_NAME = "room_name";
    public static final String COLUMN_BASE_PRICE = "base_price";
    public static final String COLUMN_IS_AVAILABLE = "is_available";
    public static final String COLUMN_ROOM_ID = "room_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CHECK_IN_DATE = "check_in_date";
    public static final String COLUMN_NUM_DAYS = "num_days";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        String createUsersSQL = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT);";
        db.execSQL(createUsersSQL);

        // Promotions table
        String createPromotionsSQL = "CREATE TABLE " + TABLE_PROMOTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT);";
        db.execSQL(createPromotionsSQL);

        // Attractions table
        String createAttractionsSQL = "CREATE TABLE " + TABLE_ATTRACTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT);";
        db.execSQL(createAttractionsSQL);

        // Rooms table
        String createRoomsSQL = "CREATE TABLE " + TABLE_ROOMS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ROOM_NAME + " TEXT, " +
                COLUMN_BASE_PRICE + " REAL, " +
                COLUMN_IS_AVAILABLE + " INTEGER, " + // 1 for true, 0 for false
                COLUMN_DESCRIPTION + " TEXT);";
        db.execSQL(createRoomsSQL);

        // Bookings table
        String createBookingsSQL = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ROOM_ID + " INTEGER, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_CHECK_IN_DATE + " TEXT, " +
                COLUMN_NUM_DAYS + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ROOM_ID + ") REFERENCES " + TABLE_ROOMS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "));";
        db.execSQL(createBookingsSQL);

        // Insert sample rooms
        ContentValues roomValues = new ContentValues();
        roomValues.put(COLUMN_ROOM_NAME, "Ocean View Suite");
        roomValues.put(COLUMN_BASE_PRICE, 199.0);
        roomValues.put(COLUMN_IS_AVAILABLE, 1);
        roomValues.put(COLUMN_DESCRIPTION, "A spacious suite with panoramic ocean views, featuring a king-sized bed, private balcony, and luxurious amenities including high-speed Wi-Fi and a minibar.");
        db.insert(TABLE_ROOMS, null, roomValues);

        roomValues.clear();
        roomValues.put(COLUMN_ROOM_NAME, "Deluxe Room");
        roomValues.put(COLUMN_BASE_PRICE, 149.0);
        roomValues.put(COLUMN_IS_AVAILABLE, 0);
        roomValues.put(COLUMN_DESCRIPTION, "A cozy room with a queen-sized bed, modern amenities, and a partial ocean view. Perfect for solo travelers or couples.");
        db.insert(TABLE_ROOMS, null, roomValues);

        roomValues.clear();
        roomValues.put(COLUMN_ROOM_NAME, "Family Suite");
        roomValues.put(COLUMN_BASE_PRICE, 249.0);
        roomValues.put(COLUMN_IS_AVAILABLE, 1);
        roomValues.put(COLUMN_DESCRIPTION, "A large suite with two bedrooms, a living area, and a kitchenette, ideal for families. Includes ocean views and access to resort amenities.");
        db.insert(TABLE_ROOMS, null, roomValues);

        // Insert sample promotions
        ContentValues promoValues = new ContentValues();
        promoValues.put(COLUMN_TITLE, "Beach Tour Discount");
        promoValues.put(COLUMN_DESCRIPTION, "Get 20% off on guided beach tours this month!");
        db.insert(TABLE_PROMOTIONS, null, promoValues);

        promoValues.clear();
        promoValues.put(COLUMN_TITLE, "Spa Package Deal");
        promoValues.put(COLUMN_DESCRIPTION, "Book a spa treatment and get a free facial.");
        db.insert(TABLE_PROMOTIONS, null, promoValues);

        // Insert sample attractions
        ContentValues attractionValues = new ContentValues();
        attractionValues.put(COLUMN_TITLE, "Water Sports Adventure");
        attractionValues.put(COLUMN_DESCRIPTION, "Try kayaking or jet skiing nearby.");
        db.insert(TABLE_ATTRACTIONS, null, attractionValues);

        attractionValues.clear();
        attractionValues.put(COLUMN_TITLE, "Snorkeling Excursion");
        attractionValues.put(COLUMN_DESCRIPTION, "Join a snorkeling trip to coral reefs.");
        db.insert(TABLE_ATTRACTIONS, null, attractionValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTRACTIONS);
        onCreate(db);
    }
}