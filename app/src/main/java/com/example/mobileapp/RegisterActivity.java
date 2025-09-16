package com.example.mobileapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText entUsername, entPassword, entConfirmPassword;
    private MaterialButton btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        entUsername = findViewById(R.id.ent_username);
        entPassword = findViewById(R.id.ent_password);
        entConfirmPassword = findViewById(R.id.ent_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        dbHelper = new DatabaseHelper(this);

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Register button
        btnRegister.setOnClickListener(v -> {
            String username = entUsername.getText().toString().trim();
            String password = entPassword.getText().toString().trim();
            String confirmPassword = entConfirmPassword.getText().toString().trim();

            // Validation
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (registerUser(username, password)) {
                Toast.makeText(this, "Registration successful. Please login.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close RegisterActivity
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });

        // Back to login link
        findViewById(R.id.textView_login).setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean registerUser(String username, String password) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        long newRowId = database.insert(DatabaseHelper.TABLE_USERS, null, values);
        return newRowId != -1; // Returns true if insertion is successful, false if username exists
    }
}