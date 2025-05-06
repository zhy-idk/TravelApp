package com.example.travelapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        public FlightViewHolder(View itemView) {
            super(itemView);
            textViewAirline = itemView.findViewById(R.id.textViewAirline);
            textViewSchedule = itemView.findViewById(R.id.textViewSchedule);
            textViewSeats = itemView.findViewById(R.id.textViewSeats);
            textViewOrigin = itemView.findViewById(R.id.textViewOrigin);
            textViewDestination = itemView.findViewById(R.id.textViewDestination);
        }

        public void bind(FlightModelClass flight) {
            textViewAirline.setText(flight.getAirline());
            textViewSchedule.setText(flight.getSchedule());
            textViewSeats.setText(String.valueOf(flight.getSeat_count()));
            textViewOrigin.setText(flight.getOrigin());
            textViewDestination.setText(flight.getDestination());
        }
    }

    // Hotel ViewHolder
    public class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHotelName, textViewHotelLocation, textViewHotelPrice, textViewHotelRooms;

        public HotelViewHolder(View itemView) {
            super(itemView);
            textViewHotelName = itemView.findViewById(R.id.textViewHotelName);
            textViewHotelLocation = itemView.findViewById(R.id.textViewHotelLocation);
            textViewHotelPrice = itemView.findViewById(R.id.textViewHotelPrice);
            textViewHotelRooms = itemView.findViewById(R.id.textViewHotelRooms);
        }

        public void bind(HotelModelClass hotel) {
            textViewHotelName.setText(hotel.getName());
            textViewHotelLocation.setText(hotel.getLocation());
            textViewHotelPrice.setText(String.format("$%.2f", hotel.getPricePerNight()));
            textViewHotelRooms.setText(String.valueOf(hotel.getAvailableRooms()));
        }
    }
}