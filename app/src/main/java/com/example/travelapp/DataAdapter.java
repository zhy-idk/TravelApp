package com.example.travelapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DataItem> dataList;

    // View types
    private static final int TYPE_FLIGHT = 0;
    private static final int TYPE_HOTEL = 1;

    public DataAdapter(List<DataItem> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FLIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
            return new FlightViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
            return new HotelViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DataItem item = dataList.get(position);
        if (holder instanceof FlightViewHolder) {
            FlightModelClass flight = (FlightModelClass) item.getData();
            ((FlightViewHolder) holder).bind(flight);
        } else if (holder instanceof HotelViewHolder) {
            HotelModelClass hotel = (HotelModelClass) item.getData();
            ((HotelViewHolder) holder).bind(hotel);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Flight ViewHolder
    public class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAirline, textViewSchedule, textViewSeats, textViewOrigin, textViewDestination;
        Button btnDel, btnUpd;

        public FlightViewHolder(View itemView) {
            super(itemView);
            textViewAirline = itemView.findViewById(R.id.textViewAirline);
            textViewSchedule = itemView.findViewById(R.id.textViewSchedule);
            textViewSeats = itemView.findViewById(R.id.textViewSeats);
            textViewOrigin = itemView.findViewById(R.id.textViewOrigin);
            textViewDestination = itemView.findViewById(R.id.textViewDestination);
            btnDel = itemView.findViewById(R.id.btnDel);
            btnUpd = itemView.findViewById(R.id.btnUpd);
        }

        public void bind(FlightModelClass flight) {
            textViewAirline.setText(flight.getAirline());
            textViewSchedule.setText(flight.getSchedule());
            textViewSeats.setText(String.valueOf(flight.getSeat_count()));
            textViewOrigin.setText(flight.getOrigin());
            textViewDestination.setText(flight.getDestination());

            btnDel.setOnClickListener(new View.OnClickListener() {
                int flightId = flight.getId();
                @Override
                public void onClick(View v) {
                    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                    Call<Void> call = apiService.deleteFlight(flightId);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(itemView.getContext(), "Flight deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(itemView.getContext(), "Failed to delete flight", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(itemView.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            btnUpd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent
                    Intent intent = new Intent(itemView.getContext(), FlightPreview.class);

                    // Intent Put Extra
                    intent.putExtra("flightID", flight.getId());
                    intent.putExtra("flightAirline", flight.getAirline());
                    intent.putExtra("flightSchedule", flight.getSchedule());
                    intent.putExtra("flightSeat", flight.getSeat_count());
                    intent.putExtra("flightOrigin", flight.getOrigin());
                    intent.putExtra("flightDestination", flight.getDestination());

                    // Intent Call
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    // Hotel ViewHolder
    public class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHotelName, textViewHotelLocation, textViewHotelPrice, textViewHotelRooms;
        Button btnDel;

        public HotelViewHolder(View itemView) {
            super(itemView);
            textViewHotelName = itemView.findViewById(R.id.textViewHotelName);
            textViewHotelLocation = itemView.findViewById(R.id.textViewHotelLocation);
            textViewHotelPrice = itemView.findViewById(R.id.textViewHotelPrice);
            textViewHotelRooms = itemView.findViewById(R.id.textViewHotelRooms);
            btnDel = itemView.findViewById(R.id.btnDel);
        }

        public void bind(HotelModelClass hotel) {
            textViewHotelName.setText(hotel.getName());
            textViewHotelLocation.setText(hotel.getLocation());
            textViewHotelPrice.setText(String.format("$%.2f", hotel.getPricePerNight()));
            textViewHotelRooms.setText(String.valueOf(hotel.getAvailableRooms()));
        }
    }
}