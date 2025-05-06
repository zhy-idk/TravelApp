package com.example.travelapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    // Getting Data
    @GET("flight/")
    Call<List<FlightModelClass>> getFlights();

    @GET("hotels/")
    Call<List<HotelModelClass>> getHotels();


    // POST: Create a new flight
    @POST("flight/create/")
    Call<FlightModelClass> createFlight(@Body FlightModelClass flight);

    // PUT: Update an existing flight
    @PUT("flight/{id}")
    Call<FlightModelClass> updateFlight(@Path("id") int flightId, @Body FlightModelClass flight);

    // POST: Create a new hotel
    @POST("hotels")
    Call<HotelModelClass> createHotel(@Body HotelModelClass hotel);

    // PUT: Update an existing hotel
    @PUT("hotels/{id}")
    Call<HotelModelClass> updateHotel(@Path("id") int hotelId, @Body HotelModelClass hotel);
}