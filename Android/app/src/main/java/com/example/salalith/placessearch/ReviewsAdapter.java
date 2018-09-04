package com.example.salalith.placessearch;

import android.graphics.Movie;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private List<GoogleReviewsData> reviewsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, time, text;
        public RatingBar rb;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textView5);
            time = (TextView) view.findViewById(R.id.textView14);
            text = (TextView) view.findViewById(R.id.textView15);
            img=(ImageView)view.findViewById(R.id.image1);
            rb=(RatingBar)view.findViewById(R.id.ratingBar);

        }
    }


    public ReviewsAdapter(List<GoogleReviewsData> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GoogleReviewsData movie = reviewsList.get(position);
        holder.name.setText(movie.getAuthor_name());
        holder.time.setText(movie.getTime());
        holder.text.setText(movie.getText());
        Picasso.get().load(movie.getPhoto_url()).resize(125,150).into(holder.img);
        holder.rb.setIsIndicator(true);
        holder.rb.setRating(movie.getRating());

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}