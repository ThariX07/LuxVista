package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class home extends AppCompatActivity {

    private MaterialButton logout_button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout_button=findViewById(R.id.logout_button) ;

        // Rooms Button
        MaterialButton roomsButton = findViewById(R.id.rooms_button);
        roomsButton.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, RoomsActivity.class);
            startActivity(intent);
        });

        // Services Button
        MaterialButton servicesButton = findViewById(R.id.services_button);
        servicesButton.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, Services.class);
            startActivity(intent);
        });

        // Profile Button
        MaterialButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, user_profile.class);
            startActivity(intent);
        });

        //logout btn
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            }
        });

        // Promotions ImageButton
        ImageButton promotionsButton = findViewById(R.id.imageButton2);
        promotionsButton.setOnClickListener(v -> {
            Intent intent = new Intent(home.this, PromotionsActivity.class);
            startActivity(intent);
        });
    }
}