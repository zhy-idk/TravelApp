package com.example.travelapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightCreate extends AppCompatActivity {
    // Declaration
    EditText edtAirline, edtSchedule, edtSeat, edtOrigin, edtDest;
    Button btnSave;
    ImageButton btnUpload;
    Uri imageUri;
    private static final int IMAGE_PICK_CODE = 1000;

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
                createNewFlight();
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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data.getData();
            btnUpload.setImageURI(imageUri);
        }
    }

    // Create New Flight
    public void createNewFlight() {
        String airline = edtAirline.getText().toString().trim();
        String schedule = edtSchedule.getText().toString().trim();
        String seat = edtSeat.getText().toString().trim();
        String origin = edtOrigin.getText().toString().trim();
        String destination = edtDest.getText().toString().trim();

        if (airline.isEmpty() || schedule.isEmpty() || seat.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody airlineRB = RequestBody.create(MediaType.parse("text/plain"), airline);
        RequestBody scheduleRB = RequestBody.create(MediaType.parse("text/plain"), schedule);
        RequestBody seatRB = RequestBody.create(MediaType.parse("text/plain"), seat);
        RequestBody originRB = RequestBody.create(MediaType.parse("text/plain"), origin);
        RequestBody destinationRB = RequestBody.create(MediaType.parse("text/plain"), destination);

        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File imageFile = FileUtils.getFileFromUri(FlightCreate.this, imageUri);
            RequestBody imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile);
            imagePart = MultipartBody.Part.createFormData("image_field", imageFile.getName(), imageRequest);
        }

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<FlightModelClass> call = apiService.createFlight(
                airlineRB, scheduleRB, seatRB, originRB, destinationRB, imagePart
        );

        call.enqueue(new Callback<FlightModelClass>() {
            @Override
            public void onResponse(Call<FlightModelClass> call, Response<FlightModelClass> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FlightCreate.this, "Flight created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FlightCreate.this, "Failed to create flight", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FlightModelClass> call, Throwable t) {
                Toast.makeText(FlightCreate.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
