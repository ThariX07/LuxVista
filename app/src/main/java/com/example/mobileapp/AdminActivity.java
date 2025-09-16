package com.example.mobileapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminActivity extends AppCompatActivity {

    private TextInputEditText entPromotionTitle, entPromotionDesc, entAttractionTitle, entAttractionDesc;
    private MaterialButton btnAddPromotion, btnAddAttraction;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        entPromotionTitle = findViewById(R.id.ent_promotion_title);
        entPromotionDesc = findViewById(R.id.ent_promotion_desc);
        entAttractionTitle = findViewById(R.id.ent_attraction_title);
        entAttractionDesc = findViewById(R.id.ent_attraction_desc);
        btnAddPromotion = findViewById(R.id.btn_add_promotion);
        btnAddAttraction = findViewById(R.id.btn_add_attraction);
        dbHelper = new DatabaseHelper(this);

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Add Promotion button
        btnAddPromotion.setOnClickListener(v -> {
            String title = entPromotionTitle.getText().toString().trim();
            String description = entPromotionDesc.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all promotion fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (addPromotion(title, description)) {
                Toast.makeText(this, "Promotion added. Notification sent to users.", Toast.LENGTH_SHORT).show();
                entPromotionTitle.setText("");
                entPromotionDesc.setText("");
                // Simulate notification (in a real app, use Firebase Cloud Messaging)
                simulateNotification("New Promotion: " + title);
            } else {
                Toast.makeText(this, "Failed to add promotion", Toast.LENGTH_SHORT).show();
            }
        });

        // Add Attraction button
        btnAddAttraction.setOnClickListener(v -> {
            String title = entAttractionTitle.getText().toString().trim();
            String description = entAttractionDesc.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all attraction fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (addAttraction(title, description)) {
                Toast.makeText(this, "Attraction added. Notification sent to users.", Toast.LENGTH_SHORT).show();
                entAttractionTitle.setText("");
                entAttractionDesc.setText("");
                // Simulate notification
                simulateNotification("New Attraction: " + title);
            } else {
                Toast.makeText(this, "Failed to add attraction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean addPromotion(String title, String description) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);

        long newRowId = database.insert(DatabaseHelper.TABLE_PROMOTIONS, null, values);
        return newRowId != -1;
    }

    private boolean addAttraction(String title, String description) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);

        long newRowId = database.insert(DatabaseHelper.TABLE_ATTRACTIONS, null, values);
        return newRowId != -1;
    }

    private void simulateNotification(String message) {
        // For this assignment, simulate notification with a Toast
        // In a real app, use Firebase Cloud Messaging to send push notifications
        Toast.makeText(this, "Notification sent: " + message, Toast.LENGTH_LONG).show();
    }
}