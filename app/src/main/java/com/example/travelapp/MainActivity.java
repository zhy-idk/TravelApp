package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlightAdapter dataAdapter;
    private List<FlightModelClass> dataList = new ArrayList<>();
    private Button btnAdd, btnRefresh;
    private EditText searchView;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Spinner spinner;

    private  Call<List<FlightModelClass>> searchCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.btnAdd);
        btnRefresh = findViewById(R.id.btnRefresh);

        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);

        spinner = findViewById(R.id.spinner);

        dataAdapter = new FlightAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);
        scrollView = findViewById(R.id.scrollView2);

        ArrayList<String> entries = new ArrayList<>();
        entries.add("airline");
        entries.add("origin");
        entries.add("destination");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, entries);
        spinner.setAdapter(spinnerAdapter);

        // Button Functionality
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlightCreate.class);
                startActivity(intent);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    fetchFlights();
                    progressBar.setVisibility(View.INVISIBLE);
                    scrollView.scrollTo(0,0);
                    Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                }, 3000);

            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchFlights(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // Search Flights
    private void searchFlights(String query) {
        dataList.clear();

        if(searchCall != null){
            searchCall.cancel();
        }
        String airline = null;
        String origin = null;
        String destination = null;

        switch(spinner.getSelectedItem().toString()) {
            case "airline":
                airline = query;
                break;
            case "origin":
                origin = query;
                break;
            case "destination":
                destination = query;
                break;
        }

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        searchCall = apiService.searchFlights(airline, origin, destination);

        searchCall.enqueue(new Callback<List<FlightModelClass>>() {
            @Override
            public void onResponse(Call<List<FlightModelClass>> call, Response<List<FlightModelClass>> response) {
                if (response.isSuccessful()){
                    List<FlightModelClass> flights = response.body();
                    if (flights != null && !flights.isEmpty()){
                        dataList.addAll(flights);

                        dataAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FlightModelClass>> call, Throwable t) {

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
                        dataList.addAll(flights);

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