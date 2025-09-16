package com.example.mobileapp;

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
import com.google.android.material.datepicker.MaterialDatePicker;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Services extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private List<Service> serviceList;
    private TextView selectedDateText;
    private MaterialButton btnPickDate;
    private Long selectedDateInMillis;
    private Map<Long, List<String>> reservations; // Simulates a database for reservations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        // Initialize views
        recyclerView = findViewById(R.id.services_recycler_view);
        selectedDateText = findViewById(R.id.selected_date);
        btnPickDate = findViewById(R.id.btn_pick_date);

        // Initialize reservations (simulated database)
        reservations = new HashMap<>();

        // Initialize service list
        serviceList = new ArrayList<>();
        populateServiceList();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceAdapter(serviceList, this); // Pass the activity instance
        recyclerView.setAdapter(adapter);

        // Set up date picker
        btnPickDate.setOnClickListener(v -> showDatePicker());
    }

    private void populateServiceList() {
        // Sample data - replace with database/API call
        serviceList.add(new Service("Spa Treatment", "Indulge in a relaxing spa treatment with ocean views, including massage, facial, and aromatherapy.", 99));
        serviceList.add(new Service("Fine Dining", "Experience gourmet dining at our oceanfront restaurant with a curated menu of local and international cuisine.", 149));
        serviceList.add(new Service("Poolside Cabana", "Reserve a private cabana by the pool, complete with shade, seating, and personalized service.", 79));
        serviceList.add(new Service("Guided Beach Tour", "Explore the beach with a guided tour, including snorkeling and a visit to hidden coves.", 59));
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Reservation Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDateInMillis = selection;
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            selectedDateText.setText(sdf.format(new Date(selection)));
            updateAvailability();
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void updateAvailability() {
        List<String> bookedServices = reservations.getOrDefault(selectedDateInMillis, new ArrayList<>());
        for (Service service : serviceList) {
            service.setAvailable(!bookedServices.contains(service.getTitle()));
        }
        adapter.notifyDataSetChanged();
    }

    public void reserveService(Service service) {
        if (selectedDateInMillis == null) {
            Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!service.isAvailable()) {
            Toast.makeText(this, service.getTitle() + " is not available on this date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add reservation to simulated database
        List<String> bookedServices = reservations.getOrDefault(selectedDateInMillis, new ArrayList<>());
        bookedServices.add(service.getTitle());
        reservations.put(selectedDateInMillis, bookedServices);

        // Update availability
        updateAvailability();
        Toast.makeText(this, "Reserved " + service.getTitle() + " for " + selectedDateText.getText(), Toast.LENGTH_SHORT).show();
    }
}

class Service {
    private String title;
    private String description;
    private double price;
    private boolean isAvailable;

    public Service(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.isAvailable = true;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
}

class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;
    private Services activity;

    public ServiceAdapter(List<Service> serviceList, Services activity) {
        this.serviceList = serviceList;
        this.activity = activity;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.title.setText(service.getTitle());
        holder.description.setText(service.getDescription());
        holder.price.setText("$" + service.getPrice());
        holder.availability.setText(service.isAvailable() ? "Available" : "Unavailable");
        holder.availability.setTextColor(service.isAvailable() ? 0xFF4CAF50 : 0xFFFF0000);
        holder.reserveButton.setEnabled(service.isAvailable());
        holder.reserveButton.setOnClickListener(v -> activity.reserveService(service));
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price, availability;
        MaterialButton reserveButton;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.service_title);
            description = itemView.findViewById(R.id.service_description);
            price = itemView.findViewById(R.id.service_price);
            availability = itemView.findViewById(R.id.service_availability);
            reserveButton = itemView.findViewById(R.id.reserve_button);
        }
    }
}