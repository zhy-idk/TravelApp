package com.example.travelapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<FlightModelClass> dataList;

    public FlightAdapter(List<FlightModelClass> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        FlightModelClass item = dataList.get(position);
        holder.textViewAirline.setText(item.getAirline().getName());
        holder.textViewSchedule.setText(item.getSchedule());
        holder.textViewSeats.setText(String.valueOf(item.getSeat_count()));
        holder.textViewOrigin.setText(item.getOrigin());
        holder.textViewDestination.setText(item.getDestination());

        Glide.with(holder.imgView.getContext()).load(RetrofitClient.BASE_URL + item.getImage_field()).into(holder.imgView);

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            int flightId = item.getId();
            @Override
            public void onClick(View v) {
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                Call<Void> call = apiService.deleteFlight(flightId);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(holder.itemView.getContext(), "Flight deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Failed to delete flight", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(holder.itemView.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent
                Intent intent = new Intent(holder.itemView.getContext(), FlightPreview.class);

                // Intent Put Extra
                intent.putExtra("flightID", item.getId());
                intent.putExtra("flightAirline", item.getAirline().getName());
                intent.putExtra("flightSchedule", item.getSchedule());
                intent.putExtra("flightSeat", item.getSeat_count());
                intent.putExtra("flightOrigin", item.getOrigin());
                intent.putExtra("flightDestination", item.getDestination());
                intent.putExtra("flightImage", item.getImage_field());

                // Intent Call
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Flight ViewHolder
    public class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAirline, textViewSchedule, textViewSeats, textViewOrigin, textViewDestination;
        Button btnDel, btnUpd;
        ImageView imgView;

        public FlightViewHolder(View itemView) {
            super(itemView);
            textViewAirline = itemView.findViewById(R.id.textViewAirline);
            textViewSchedule = itemView.findViewById(R.id.textViewSchedule);
            textViewSeats = itemView.findViewById(R.id.textViewSeats);
            textViewOrigin = itemView.findViewById(R.id.textViewOrigin);
            textViewDestination = itemView.findViewById(R.id.textViewDestination);
            imgView = itemView.findViewById(R.id.imgView);
            btnDel = itemView.findViewById(R.id.btnDel);
            btnUpd = itemView.findViewById(R.id.btnUpd);
        }
    }
}