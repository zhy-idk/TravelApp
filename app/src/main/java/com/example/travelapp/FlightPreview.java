package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightPreview extends AppCompatActivity {
    // Declaration
    EditText edtAirline, edtSchedule, edtSeat, edtOrigin, edtDest;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_preview);

        // Initialization
        edtAirline = findViewById(R.id.edtAirline);
        edtSchedule = findViewById(R.id.edtSchedule);
        edtSeat = findViewById(R.id.edtSeat);
        edtOrigin = findViewById(R.id.edtOrigin);
        edtDest = findViewById(R.id.edtDest);
        btnSave = findViewById(R.id.btnSave);

        // Getting Intent
        Intent intent = getIntent();
        int flightId = intent.getIntExtra("flightID", -1);
        String airline = intent.getStringExtra("flightAirline");
        String schedule = intent.getStringExtra("flightSchedule");
        String seatCount = String.valueOf(intent.getIntExtra("flightSeat", 0));
        String origin = intent.getStringExtra("flightOrigin");
        String destination = intent.getStringExtra("flightDestination");

        // Updating The Text Fields
        edtAirline.setText(airline);
        edtSchedule.setText(schedule);
        edtSeat.setText(seatCount);
        edtOrigin.setText(origin);
        edtDest.setText(destination);

        // Button Functionalities
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlightModelClass updatedFlight = new FlightModelClass(flightId, edtAirline.getText().toString(), edtSchedule.getText().toString(), Integer.parseInt(edtSeat.getText().toString()), edtOrigin.getText().toString(), edtDest.getText().toString());
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                Call<FlightModelClass> call = apiService.updateFlight(flightId, updatedFlight);

                call.enqueue(new Callback<FlightModelClass>() {
                    @Override
                    public void onResponse(Call<FlightModelClass> call, Response<FlightModelClass> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            FlightModelClass flight = response.body();
                            Toast.makeText(FlightPreview.this, "Flight updated: " + flight.getAirline(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FlightPreview.this, "Failed to update flight", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FlightModelClass> call, Throwable t) {
                        Toast.makeText(FlightPreview.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}