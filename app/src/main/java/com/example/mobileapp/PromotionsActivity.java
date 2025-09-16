package com.example.mobileapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.List;

public class PromotionsActivity extends AppCompatActivity {

    private RecyclerView offersRecyclerView, attractionsRecyclerView;
    private PromotionAdapter offersAdapter, attractionsAdapter;
    private List<Promotion> offersList, attractionsList;
    private SwitchMaterial notificationSwitch;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LuxeVistaPrefs";
    private static final String NOTIFICATION_KEY = "notifications_enabled";
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

        // Initialize views
        offersRecyclerView = findViewById(R.id.offers_recycler_view);
        attractionsRecyclerView = findViewById(R.id.attractions_recycler_view);
        notificationSwitch = findViewById(R.id.notification_switch);
        dbHelper = new DatabaseHelper(this);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean notificationsEnabled = sharedPreferences.getBoolean(NOTIFICATION_KEY, false);
        notificationSwitch.setChecked(notificationsEnabled);

        // Set up notification toggle
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NOTIFICATION_KEY, isChecked);
            editor.apply();
            // In a real app, enable/disable notifications here (e.g., using Firebase Cloud Messaging)
        });

        // Initialize promotion lists
        offersList = new ArrayList<>();
        attractionsList = new ArrayList<>();
        try {
            populatePromotionLists();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading promotions: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Set up RecyclerViews
        offersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        offersAdapter = new PromotionAdapter(offersList);
        offersRecyclerView.setAdapter(offersAdapter);

        attractionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attractionsAdapter = new PromotionAdapter(attractionsList);
        attractionsRecyclerView.setAdapter(attractionsAdapter);
    }

    private void populatePromotionLists() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // Load Promotions
        try (Cursor promotionCursor = database.query(
                DatabaseHelper.TABLE_PROMOTIONS,
                new String[]{DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DESCRIPTION},
                null, null, null, null, null)) {
            while (promotionCursor.moveToNext()) {
                String title = promotionCursor.getString(promotionCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String description = promotionCursor.getString(promotionCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                offersList.add(new Promotion(title, description));
            }
        }

        // Load Attractions
        try (Cursor attractionCursor = database.query(
                DatabaseHelper.TABLE_ATTRACTIONS,
                new String[]{DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DESCRIPTION},
                null, null, null, null, null)) {
            while (attractionCursor.moveToNext()) {
                String title = attractionCursor.getString(attractionCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String description = attractionCursor.getString(attractionCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                attractionsList.add(new Promotion(title, description));
            }
        }
    }
}

class Promotion {
    private String title;
    private String description;

    public Promotion(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
}

class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {
    private List<Promotion> promotionList;

    public PromotionAdapter(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    @Override
    public PromotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion, parent, false);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PromotionViewHolder holder, int position) {
        Promotion promotion = promotionList.get(position);
        holder.title.setText(promotion.getTitle());
        holder.description.setText(promotion.getDescription());
        holder.learnMoreButton.setOnClickListener(v -> {
            // Implement "Learn More" action, e.g., open a detailed page or external link
        });
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    static class PromotionViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        MaterialButton learnMoreButton;

        public PromotionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.promotion_title);
            description = itemView.findViewById(R.id.promotion_description);
            learnMoreButton = itemView.findViewById(R.id.learn_more_button);
        }
    }
}