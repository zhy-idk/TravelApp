package com.example.travelapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightPreview extends AppCompatActivity {
    // Declaration
    EditText edtAirline, edtSchedule, edtSeat, edtOrigin, edtDest;
    Button btnSave;
    ImageButton btnUpload;
    Spinner spnAirline;

    Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_preview);

        // Initialization
        edtSchedule = findViewById(R.id.edtSchedule);
        edtSeat = findViewById(R.id.edtSeat);
        edtOrigin = findViewById(R.id.edtOrigin);
        edtDest = findViewById(R.id.edtDest);
        btnUpload = findViewById(R.id.btnUpload);
        btnSave = findViewById(R.id.btnSave);

        spnAirline = findViewById(R.id.spnAirline);

        // Getting Intent
        Intent intent = getIntent();
        int flightId = intent.getIntExtra("flightID", -1);
        String airline = intent.getStringExtra("flightAirline");
        String schedule = intent.getStringExtra("flightSchedule");
        String seatCount = String.valueOf(intent.getIntExtra("flightSeat", 0));
        String origin = intent.getStringExtra("flightOrigin");
        String destination = intent.getStringExtra("flightDestination");
        String image_field = intent.getStringExtra("flightImage");

        // Fetch Airlines
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<AirlineModelClass>> call = apiService.getAirlines();

        call.enqueue(new Callback<List<AirlineModelClass>>() {
            @Override
            public void onResponse(Call<List<AirlineModelClass>> call, Response<List<AirlineModelClass>> response) {
                if (response.isSuccessful()){
                    List<AirlineModelClass> airlines = response.body();
                    Log.d("Test airline fetch", airlines.toString());
                    if (!airlines.isEmpty()){
                        ArrayAdapter<AirlineModelClass> adapter = new ArrayAdapter<>(FlightPreview.this, android.R.layout.simple_spinner_dropdown_item, airlines);
                        spnAirline.setAdapter(adapter);

                        for (int i = 0; i < airlines.size(); i++) {
                            if (airlines.get(i).getName().equals(airline)) {
                                spnAirline.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AirlineModelClass>> call, Throwable t) {

            }
        });



        // Updating The Text Fields
        edtSchedule.setText(schedule);
        edtSeat.setText(seatCount);
        edtOrigin.setText(origin);
        edtDest.setText(destination);

        Glide.with(btnUpload.getContext()).load(RetrofitClient.BASE_URL + image_field).into(btnUpload);

        // Button Functionalities
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Text Fields to RequestBody
                AirlineModelClass selectedAirline = (AirlineModelClass) spnAirline.getSelectedItem();
                String airlineId = String.valueOf(selectedAirline.getId());
                RequestBody airline = RequestBody.create(MediaType.parse("text/plain"), airlineId);
                RequestBody schedule = RequestBody.create(MediaType.parse("text/plain"), edtSchedule.getText().toString());
                RequestBody seat = RequestBody.create(MediaType.parse("text/plain"), edtSeat.getText().toString());
                RequestBody origin = RequestBody.create(MediaType.parse("text/plain"), edtOrigin.getText().toString());
                RequestBody destination = RequestBody.create(MediaType.parse("text/plain"), edtDest.getText().toString());

                // Image File
                MultipartBody.Part imagePart = null;
                if (selectedImageUri != null) {
                    File imageFile = new File(FileUtils.getFilePath(FlightPreview.this, selectedImageUri));
                    RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                    imagePart = MultipartBody.Part.createFormData("image_field", imageFile.getName(), imageBody);
                }

                // Retrofit API Call
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                Call<ResponseBody> call = apiService.updateFlight(
                        flightId, airline, schedule, seat, origin, destination, imagePart
                );

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(FlightPreview.this, "Flight updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FlightPreview.this, "Failed to update flight", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(FlightPreview.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public void getImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                selectedImageUri = uri;
                btnUpload.setImageURI(selectedImageUri);
            });
}