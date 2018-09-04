package com.example.salalith.placessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {

    private List<PlaceSG> PlacesList;




    public class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView title, addr;
        public ImageButton icon,favicon;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            addr = (TextView) view.findViewById(R.id.genre);
            icon=(ImageButton) view.findViewById(R.id.icon);
            favicon=(ImageButton) view.findViewById(R.id.favicon);
        }


    }


    public PlacesAdapter(List<PlaceSG> PlacesList) {
        this.PlacesList = PlacesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlaceSG place = PlacesList.get(position);
        holder.title.setText(place.getName());
        holder.addr.setText(place.getAddr());
        Picasso.get().load(place.getIcon()).resize(125,150).into(holder.icon);
        holder.favicon.setImageResource(R.drawable.heart_outline_black);
    }

    @Override
    public int getItemCount() {
        return PlacesList.size();
    }
}