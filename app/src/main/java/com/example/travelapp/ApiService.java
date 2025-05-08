package com.example.travelapp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    // Creating Data
    @Multipart
    @POST("flight/create/")
    Call<FlightModelClass> createFlight(
            @Part("airline") RequestBody airline,
            @Part("schedule") RequestBody schedule,
            @Part("seat_count") RequestBody seatCount,
            @Part("origin") RequestBody origin,
            @Part("destination") RequestBody destination,
            @Part MultipartBody.Part image
    );

    // Reading Data
    @GET("flight/")
    Call<List<FlightModelClass>> getFlights();

    // Updating Data
    @Multipart
    @PUT("flight/{id}/")
    Call<ResponseBody> updateFlight(
            @Path("id") int flightId,
            @Part("airline") RequestBody airline,
            @Part("schedule") RequestBody schedule,
            @Part("seat_count") RequestBody seatCount,
            @Part("origin") RequestBody origin,
            @Part("destination") RequestBody destination,
            @Part MultipartBody.Part image
    );

    // Deleting Data
    @DELETE("flight/{id}/")
    Call<Void> deleteFlight(@Path("id") int flightId);
}