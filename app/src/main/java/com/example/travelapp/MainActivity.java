package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private List<DataItem> dataList = new ArrayList<>();
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnAdd = findViewById(R.id.btnAdd);

        dataAdapter = new DataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFlight();
            }
        });

        // Call Fetch Methods
        fetchFlights();
        fetchHotels();
    }

    // Create a new flight
    public void createNewFlight() {
        FlightModelClass newFlight = new FlightModelClass(0, "New Airline", "10:00 - 12:00 PM", 50, "MNL", "SGN");
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<FlightModelClass> call = apiService.createFlight(newFlight);

        call.enqueue(new Callback<FlightModelClass>() {
            @Override
            public void onResponse(Call<FlightModelClass> call, Response<FlightModelClass> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FlightModelClass createdFlight = response.body();
                    Toast.makeText(MainActivity.this, "Flight created: " + createdFlight.getAirline(), Toast.LENGTH_SHORT).show();
                    fetchFlights();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to create flight", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FlightModelClass> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update an existing flight
    public void updateExistingFlight(int flightId) {
        FlightModelClass updatedFlight = new FlightModelClass(flightId, "Updated Airline", "1:00 - 3:00 PM", 30, "SGN", "HKT");
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<FlightModelClass> call = apiService.updateFlight(flightId, updatedFlight);

        call.enqueue(new Callback<FlightModelClass>() {
            @Override
            public void onResponse(Call<FlightModelClass> call, Response<FlightModelClass> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FlightModelClass flight = response.body();
                    Toast.makeText(MainActivity.this, "Flight updated: " + flight.getAirline(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to update flight", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FlightModelClass> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch flight data
    private void fetchFlights() {
        // Clear the existing data to prevent duplication
        dataList.clear();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<FlightModelClass>> call = apiService.getFlights();

        call.enqueue(new Callback<List<FlightModelClass>>() {
            @Override
            public void onResponse(Call<List<FlightModelClass>> call, Response<List<FlightModelClass>> response) {
                if (response.isSuccessful()) {
                    List<FlightModelClass> flights = response.body();
                    if (flights != null && !flights.isEmpty()) {
                        // Add each flight as a DataItem to the list
                        for (FlightModelClass flight : flights) {
                            dataList.add(new DataItem(DataItem.TYPE_FLIGHT, flight));
                        }
                        // Notify the adapter that data has changed and UI should be updated
                        dataAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "No flights available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch flights", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FlightModelClass>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching flight data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Fetch hotel data
    private void fetchHotels() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<HotelModelClass>> call = apiService.getHotels();

        call.enqueue(new Callback<List<HotelModelClass>>() {
            @Override
            public void onResponse(Call<List<HotelModelClass>> call, Response<List<HotelModelClass>> response) {
                if (response.isSuccessful()) {
                    List<HotelModelClass> hotels = response.body();
                    if (hotels != null && !hotels.isEmpty()) {
                        for (HotelModelClass hotel : hotels) {
                            // Add hotel to data list
                            dataList.add(new DataItem(DataItem.TYPE_HOTEL, hotel));
                        }
                        dataAdapter.notifyDataSetChanged();  // Notify adapter to update UI
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch hotels", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelModelClass>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching hotel data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}