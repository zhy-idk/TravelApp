package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private List<DataItem> dataList = new ArrayList<>();
    private Button btnAdd, btnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.btnAdd);
        btnDel = findViewById(R.id.btnDel);

        dataAdapter = new DataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);

        // Button Functionality
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlightCreate.class);
                startActivity(intent);
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFlight(1);
            }
        });

        // Call Fetch Methods With Time Interval
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fetchFlights();
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // Create a new flight


    // Delete an existing flight
    public void deleteFlight(int flightId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.deleteFlight(flightId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Flight deleted", Toast.LENGTH_SHORT).show();
                    fetchFlights();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete flight", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch flight data
    private void fetchFlights() {
        dataList.clear();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<FlightModelClass>> call = apiService.getFlights();

        call.enqueue(new Callback<List<FlightModelClass>>() {
            @Override
            public void onResponse(Call<List<FlightModelClass>> call, Response<List<FlightModelClass>> response) {
                if (response.isSuccessful()) {
                    List<FlightModelClass> flights = response.body();
                    if (flights != null && !flights.isEmpty()) {
                        for (FlightModelClass flight : flights) {
                            dataList.add(new DataItem(DataItem.TYPE_FLIGHT, flight));
                        }
                        dataAdapter.notifyDataSetChanged();
                    } else {
                        dataAdapter.notifyDataSetChanged();
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
}