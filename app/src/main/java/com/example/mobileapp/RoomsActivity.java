package com.example.mobileapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RoomsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private List<Room> filteredList;
    private Spinner spinnerRoomType, spinnerAvailability;
    private TextInputEditText priceMin, priceMax;
    private MaterialButton btnSort;
    private boolean isAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        // Initialize views
        recyclerView = findViewById(R.id.rooms_recycler_view);
        spinnerRoomType = findViewById(R.id.spinner_room_type);
        spinnerAvailability = findViewById(R.id.spinner_availability);
        priceMin = findViewById(R.id.price_min);
        priceMax = findViewById(R.id.price_max);
        btnSort = findViewById(R.id.btn_sort);

        // Initialize room list
        roomList = new ArrayList<>();
        filteredList = new ArrayList<>();
        populateRoomList();

        // Set up spinners
        ArrayAdapter<CharSequence> roomTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.room_types, android.R.layout.simple_spinner_item);
        roomTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoomType.setAdapter(roomTypeAdapter);

        ArrayAdapter<CharSequence> availabilityAdapter = ArrayAdapter.createFromResource(this,
                R.array.availability_options, android.R.layout.simple_spinner_item);
        availabilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvailability.setAdapter(availabilityAdapter);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        // Set up filter listeners
        spinnerRoomType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        spinnerAvailability.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Set up price filter listeners
        TextWatcher priceWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                applyFilters();
            }
        };
        priceMin.addTextChangedListener(priceWatcher);
        priceMax.addTextChangedListener(priceWatcher);

        // Set up sort button
        btnSort.setOnClickListener(v -> {
            isAscending = !isAscending;
            btnSort.setText(isAscending ? "Sort by Price (Asc)" : "Sort by Price (Desc)");
            sortRooms();
        });

        // Initial filter application
        applyFilters();
    }

    private void populateRoomList() {
        // Sample data - replace with database/API call
        roomList.add(new Room("Ocean View Suite", "A spacious suite with panoramic ocean views, featuring a king-sized bed, private balcony, and luxurious amenities including high-speed Wi-Fi and a minibar.", 199, true, R.drawable.ic_room));
        roomList.add(new Room("Deluxe Room", "A cozy room with a queen-sized bed, modern amenities, and a partial ocean view. Perfect for solo travelers or couples.", 149, false, R.drawable.ic_room));
        roomList.add(new Room("Family Suite", "A large suite with two bedrooms, a living area, and a kitchenette, ideal for families. Includes ocean views and access to resort amenities.", 249, true, R.drawable.ic_room));
        filteredList.addAll(roomList);
    }

    private void applyFilters() {
        String selectedRoomType = spinnerRoomType.getSelectedItem().toString();
        String selectedAvailability = spinnerAvailability.getSelectedItem().toString();
        double minPrice;
        double maxPrice;
        try {
            minPrice = priceMin.getText().toString().isEmpty() ? 0 : Double.parseDouble(priceMin.getText().toString());
            maxPrice = priceMax.getText().toString().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(priceMax.getText().toString());
        } catch (NumberFormatException e) {
            minPrice = 0;
            maxPrice = Double.MAX_VALUE;
            Toast.makeText(this, "Invalid price input, showing all rooms", Toast.LENGTH_SHORT).show();
        }

        filteredList.clear();
        for (Room room : roomList) {
            boolean matchesType = selectedRoomType.equals("All Room Types") || room.getTitle().equals(selectedRoomType);
            boolean matchesAvailability = selectedAvailability.equals("All") ||
                    (selectedAvailability.equals("Available") && room.isAvailable()) ||
                    (selectedAvailability.equals("Unavailable") && !room.isAvailable());
            boolean matchesPrice = room.getPrice() >= minPrice && room.getPrice() <= maxPrice;

            if (matchesType && matchesAvailability && matchesPrice) {
                filteredList.add(room);
            }
        }
        sortRooms();
        adapter.notifyDataSetChanged();
    }

    private void sortRooms() {
        Collections.sort(filteredList, (r1, r2) -> isAscending ?
                Double.compare(r1.getPrice(), r2.getPrice()) :
                Double.compare(r2.getPrice(), r1.getPrice()));
        adapter.notifyDataSetChanged();
    }

    public void showBookingDialog(Room room) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_book_room, null);
        builder.setView(dialogView);

        // Initialize dialog views
        TextView tvRoomName = dialogView.findViewById(R.id.tv_room_name);
        TextView tvBasePrice = dialogView.findViewById(R.id.tv_base_price);
        TextInputEditText etCheckInDate = dialogView.findViewById(R.id.et_check_in_date);
        TextInputEditText etNumDays = dialogView.findViewById(R.id.et_num_days);
        TextView tvTotalPrice = dialogView.findViewById(R.id.tv_total_price);
        MaterialButton btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        MaterialButton btnCancel = dialogView.findViewById(R.id.btn_cancel);

        // Set initial values
        tvRoomName.setText("Room: " + room.getTitle());
        tvBasePrice.setText(String.format(Locale.US, "Base Price: $%.2f/day", room.getPrice()));
        double basePrice = room.getPrice();

        // Set initial total price based on default number of days
        int initialNumDays;
        try {
            initialNumDays = Integer.parseInt(etNumDays.getText().toString());
            if (initialNumDays < 1) initialNumDays = 1;
        } catch (NumberFormatException e) {
            initialNumDays = 1;
        }
        double initialTotalPrice = basePrice * initialNumDays;
        tvTotalPrice.setText(String.format(Locale.US, "Total Price: $%.2f", initialTotalPrice));

        // Update total price when number of days changes
        etNumDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int numDays = Integer.parseInt(s.toString());
                    if (numDays < 1) numDays = 1;
                    double totalPrice = basePrice * numDays;
                    tvTotalPrice.setText(String.format(Locale.US, "Total Price: $%.2f", totalPrice));
                } catch (NumberFormatException e) {
                    tvTotalPrice.setText(String.format(Locale.US, "Total Price: $%.2f", basePrice));
                }
            }
        });

        AlertDialog dialog = builder.create();

        // Confirm booking
        btnConfirm.setOnClickListener(v -> {
            String checkInDate = etCheckInDate.getText().toString().trim();
            String numDaysStr = etNumDays.getText().toString().trim();

            // Validate inputs
            if (checkInDate.isEmpty() || numDaysStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int numDays;
            try {
                numDays = Integer.parseInt(numDaysStr);
                if (numDays < 1) {
                    Toast.makeText(this, "Number of days must be at least 1", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number of days", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            sdf.setLenient(false);
            try {
                sdf.parse(checkInDate);
            } catch (ParseException e) {
                Toast.makeText(this, "Invalid date format (use YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simulate booking confirmation (no database in this version)
            Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}

class Room {
    private String title;
    private String description;
    private double price;
    private boolean isAvailable;
    private int imageResId;

    public Room(String title, String description, double price, boolean isAvailable, int imageResId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.isAvailable = isAvailable;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return isAvailable; }
    public int getImageResId() { return imageResId; }
}

class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private RoomsActivity activity;

    public RoomAdapter(List<Room> roomList, RoomsActivity activity) {
        this.roomList = roomList;
        this.activity = activity;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.title.setText(room.getTitle());
        holder.description.setText(room.getDescription());
        holder.price.setText("$" + room.getPrice() + "/night");
        holder.image.setImageResource(room.getImageResId());
        holder.bookButton.setOnClickListener(v -> activity.showBookingDialog(room));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description, price;
        MaterialButton bookButton;

        public RoomViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.room_image);
            title = itemView.findViewById(R.id.room_title);
            description = itemView.findViewById(R.id.room_description);
            price = itemView.findViewById(R.id.room_price);
            bookButton = itemView.findViewById(R.id.book_button);
        }
    }
}