package com.example.mobileapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText entUsername, entPassword;
    private MaterialButton btnSubmit;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        entUsername = findViewById(R.id.ent_username);
        entPassword = findViewById(R.id.ent_password);
        btnSubmit = findViewById(R.id.btn_submit);
        dbHelper = new DatabaseHelper(this);

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Submit button for login
        btnSubmit.setOnClickListener(v -> {
            String username = entUsername.getText().toString().trim();
            String password = entPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check for admin login
            if (username.equals("admin") && password.equals("admin")) {
                Toast.makeText(this, "Admin login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            // Regular user login
            if (validateLogin(username, password)) {
                // Store username in SharedPreferences for session
                getSharedPreferences("LuxeVistaPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("logged_in_user", username)
                        .apply();

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, home.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Register button redirects to RegisterActivity
        findViewById(R.id.btn_register).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateLogin(String username, String password) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PASSWORD},
                DatabaseHelper.COLUMN_USERNAME + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null
        );

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}