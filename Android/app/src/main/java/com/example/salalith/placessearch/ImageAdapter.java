package com.example.salalith.placessearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private List<ImageData> imageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.place_image1);

        }
    }


    public ImageAdapter(List<ImageData> imageList) {
        this.imageList = imageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageData movie = imageList.get(position);
        holder.img.setImageBitmap(movie.getImage());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
