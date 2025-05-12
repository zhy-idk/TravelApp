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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightCreate extends AppCompatActivity {
    // Declaration
    EditText edtSchedule, edtSeat, edtOrigin, edtDest;
    Button btnSave;
    ImageButton btnUpload;
    Spinner spinAirline;
    Uri imageUri;
    String airline;
    private static final int IMAGE_PICK_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_create);

        // Initialization

        edtSchedule = findViewById(R.id.edtSchedule);
        edtSeat = findViewById(R.id.edtSeat);
        edtOrigin = findViewById(R.id.edtOrigin);
        edtDest = findViewById(R.id.edtDest);
        btnSave = findViewById(R.id.btnSave);
        btnUpload = findViewById(R.id.btnUpload);
        spinAirline = findViewById(R.id.spinAirline);

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
                getImage();
            }
        });

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
                         ArrayAdapter<AirlineModelClass> adapter = new ArrayAdapter<>(FlightCreate.this, android.R.layout.simple_spinner_dropdown_item, airlines);
                         spinAirline.setAdapter(adapter);
                         spinAirline.setSelection(0);
                         }
                     }
                 }
            @Override
            public void onFailure(Call<List<AirlineModelClass>> call, Throwable t) {

            }

        });
    }

    // Upload Image
    public void getImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                imageUri = uri;
                btnUpload.setImageURI(imageUri);
            });

    // Create New Flight
    public void createNewFlight() {
        airline = spinAirline.getSelectedItem().toString();
        String schedule = edtSchedule.getText().toString().trim();
        String seat = edtSeat.getText().toString().trim();
        String origin = edtOrigin.getText().toString().trim();
        String destination = edtDest.getText().toString().trim();

        if (airline.isEmpty() || schedule.isEmpty() || seat.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        AirlineModelClass selectedAirline = (AirlineModelClass) spinAirline.getSelectedItem();
        String airlineId = String.valueOf(selectedAirline.getId());
        RequestBody airlineRB = RequestBody.create(MediaType.parse("text/plain"), airlineId);
        RequestBody scheduleRB = RequestBody.create(MediaType.parse("text/plain"), schedule);
        RequestBody seatRB = RequestBody.create(MediaType.parse("text/plain"), seat);
        RequestBody originRB = RequestBody.create(MediaType.parse("text/plain"), origin);
        RequestBody destinationRB = RequestBody.create(MediaType.parse("text/plain"), destination);

        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File imageFile = new File(FileUtils.getFilePath(FlightCreate.this, imageUri));
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
