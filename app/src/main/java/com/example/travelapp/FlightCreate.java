package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightCreate extends AppCompatActivity {
    // Declaration
    EditText edtAirline, edtSchedule, edtSeat, edtOrigin, edtDest;
    Button btnSave;
    ImageButton btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_create);

        // Initialization
        edtAirline = findViewById(R.id.edtAirline);
        edtSchedule = findViewById(R.id.edtSchedule);
        edtSeat = findViewById(R.id.edtSeat);
        edtOrigin = findViewById(R.id.edtOrigin);
        edtDest = findViewById(R.id.edtDest);
        btnSave = findViewById(R.id.btnSave);
        btnUpload = findViewById(R.id.btnUpload);

        // Event Listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createNewFlight();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    // Upload Image
    public void uploadImage() {

    }

    // Create New Flight
//    public void createNewFlight() {
//        FlightModelClass newFlight = new FlightModelClass(0, "New Airline", "10:00 - 12:00 PM", 50, "MNL", "SGN");
//        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
//        Call<FlightModelClass> call = apiService.createFlight();
//
//        call.enqueue(new Callback<FlightModelClass>() {
//            @Override
//            public void onResponse(Call<FlightModelClass> call, Response<FlightModelClass> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    FlightModelClass createdFlight = response.body();
//                    Toast.makeText(FlightCreate.this, "Flight created: " + createdFlight.getAirline(), Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    Toast.makeText(FlightCreate.this, "Failed to create flight", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FlightModelClass> call, Throwable t) {
//                Toast.makeText(FlightCreate.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
