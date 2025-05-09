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
    private FlightAdapter dataAdapter;
    private List<FlightModelClass> dataList = new ArrayList<>();
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.btnAdd);

        dataAdapter = new FlightAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);

        // Button Functionality
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlightCreate.class);
                startActivity(intent);
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
                        dataList.addAll(flights);

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