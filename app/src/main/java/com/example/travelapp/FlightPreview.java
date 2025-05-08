package com.example.travelapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.bumptech.glide.Glide;

import java.io.File;

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

    Uri selectedImageUri = null;

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
        btnUpload = findViewById(R.id.btnUpload);
        btnSave = findViewById(R.id.btnSave);

        // Getting Intent
        Intent intent = getIntent();
        int flightId = intent.getIntExtra("flightID", -1);
        String airline = intent.getStringExtra("flightAirline");
        String schedule = intent.getStringExtra("flightSchedule");
        String seatCount = String.valueOf(intent.getIntExtra("flightSeat", 0));
        String origin = intent.getStringExtra("flightOrigin");
        String destination = intent.getStringExtra("flightDestination");
        String image_field = intent.getStringExtra("flightImage");

        // Updating The Text Fields
        edtAirline.setText(airline);
        edtSchedule.setText(schedule);
        edtSeat.setText(seatCount);
        edtOrigin.setText(origin);
        edtDest.setText(destination);

        Glide.with(btnUpload.getContext()).load("http://10.10.204.23:8000" + image_field).into(btnUpload);

        // Button Functionalities
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1001);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Text Fields to RequestBody
                RequestBody airline = RequestBody.create(MediaType.parse("text/plain"), edtAirline.getText().toString());
                RequestBody schedule = RequestBody.create(MediaType.parse("text/plain"), edtSchedule.getText().toString());
                RequestBody seat = RequestBody.create(MediaType.parse("text/plain"), edtSeat.getText().toString());
                RequestBody origin = RequestBody.create(MediaType.parse("text/plain"), edtOrigin.getText().toString());
                RequestBody destination = RequestBody.create(MediaType.parse("text/plain"), edtDest.getText().toString());

                // Image File
                MultipartBody.Part imagePart = null;
                if (selectedImageUri != null) {
                    File imageFile = FileUtils.getFileFromUri(FlightPreview.this, selectedImageUri);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            btnUpload.setImageURI(selectedImageUri);
        }
    }
}