package com.example.salalith.placessearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.salalith.placessearch.DetailsActivity.g1;
import static com.example.salalith.placessearch.DetailsActivity.g2;
import static com.example.salalith.placessearch.DetailsActivity.googlePlace;
import static com.example.salalith.placessearch.DetailsActivity.yelpreviews;
import static com.example.salalith.placessearch.ListActivity.details;

public class ReviewsTab  extends Fragment {
    public static List<GoogleReviewsData> movieList=new ArrayList<>();
    private RecyclerView recyclerView;
    public static ReviewsAdapter reviewmrecycleAdapter;
    private Spinner reviews,sort;
    public static String s12="";
    public static TextView norev;
    public static Context c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reviewstab, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviews=(Spinner) getActivity().findViewById(R.id.spinner);
        sort=(Spinner) getActivity().findViewById(R.id.spinner2);
        norev=(TextView)getActivity().findViewById(R.id.textView18);
        c=getActivity();


        ArrayAdapter<CharSequence> adapterspinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.rev_array, android.R.layout.simple_spinner_item);
        adapterspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviews.setAdapter(adapterspinner);
        reviews.setOnItemSelectedListener(new SpinnerClickReviews());



        ArrayAdapter<CharSequence> adapterspinner1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapterspinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort.setAdapter(adapterspinner1);
        sort.setOnItemSelectedListener(new SpinnerSortReviews());


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_viewreviews);


        reviewmrecycleAdapter = new ReviewsAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reviewmrecycleAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                GoogleReviewsData movie = movieList.get(position);
                //Toast.makeText(getActivity().getApplicationContext(), movie.getAuthor_name() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getAuthor_url()));
                startActivity(browserIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));









        prepareMovieData("Googlereviews");
        //reviews.setSelection(1);
        //reviews.setSelection(0);

    }


    public static void prepareMovieData(String s) {

        norev.setVisibility(TextView.VISIBLE);
        norev.setText("No Reviews");

        movieList.clear();
        s12=s;
        g2.clear();
        g1.clear();
        try {
        if(s.equals("Googlereviews")) {
            googlePlace = new JSONObject(details.get(0));
            JSONObject result=googlePlace.getJSONObject("result");
            JSONArray reviews=result.getJSONArray("reviews");

            if(!result.has("reviews")){
                Toast.makeText(c,"No reviews Found",Toast.LENGTH_LONG).show();


            }
            else if(reviews.length()==0){
                Toast.makeText(c,"No reviews Found",Toast.LENGTH_LONG).show();

            }
            if(result.has("reviews") && reviews.length()>0) {
                norev.setVisibility(TextView.INVISIBLE);

                for (int i = 0; i < reviews.length(); i++) {
                    JSONObject r = reviews.getJSONObject(i);

                    Date date = new Date(r.getLong("time") * 1000);
                    Log.d("time", r.getLong("time") + "");
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                    String date1 = format.format(date).substring(0);


                    GoogleReviewsData grd = new GoogleReviewsData(r.getString("author_name"), r.getString("author_url"), r.getString("profile_photo_url"), r.getString("text"), r.getInt("rating"), date1);
                    g1.add(grd);
                }
            }

            for (GoogleReviewsData gtemp : g1)
                movieList.add(gtemp);
            Log.d("hello","hai");

        }


            if (s.equals("Yelpreviews")) {
                if (ListActivity.details.get(1).equals("not found")) {

                    norev.setVisibility(TextView.VISIBLE);
                    norev.setText("No Reviews");

                } else {
                    norev.setVisibility(TextView.INVISIBLE);

                    yelpreviews = new JSONObject(ListActivity.details.get(1));
                    if (yelpreviews.has("reviews")) {
                        JSONArray reviewsYelp = yelpreviews.getJSONArray("reviews");


                        for (int i = 0; i < reviewsYelp.length(); i++) {
                            JSONObject r1 = reviewsYelp.getJSONObject(i);
                            JSONObject ruser = r1.getJSONObject("user");

                            GoogleReviewsData gry = new GoogleReviewsData(ruser.getString("name"), r1.getString("url"), ruser.getString("image_url"), r1.getString("text"), r1.getInt("rating"), r1.getString("time_created"));
                            g2.add(gry);
                        }

                    }
                }
                Log.d("hai", g2.size() + "present");

                for (GoogleReviewsData gtemp : g2)
                    movieList.add(gtemp);
            }
        }catch (Exception e){

        }



        Collections.sort(movieList, new Comparator<GoogleReviewsData>(){
            public int compare(GoogleReviewsData s1, GoogleReviewsData s2) {
             /*   if(s1.getRating()<s2.getRating()){
                    return -1;
                }
                else{
                    return 1;
                }*/
                return s1.getTime().compareToIgnoreCase(s2.getTime());
            }
        });

        if(movieList.isEmpty()){
            Toast.makeText(c,"No reviews Found",Toast.LENGTH_LONG).show();

        }
        reviewmrecycleAdapter.notifyDataSetChanged();


    }







    public static void Highest_rating(){


        Collections.sort(movieList, new Comparator<GoogleReviewsData>(){
            public int compare(GoogleReviewsData s1, GoogleReviewsData s2) {
                if(s1.getRating()<s2.getRating()){
                    return 1;
                }
                else{
                    return -1;
                }
                //return s1.getTime().compareToIgnoreCase(s2.getTime());
            }
        });
        reviewmrecycleAdapter.notifyDataSetChanged();


    }
    public static void Least_rating(){

        Collections.sort(movieList, new Comparator<GoogleReviewsData>(){
            public int compare(GoogleReviewsData s1, GoogleReviewsData s2) {
               if(s1.getRating()<s2.getRating()){
                    return -1;
                }
                else{
                    return 1;
                }
                //return s1.getTime().compareToIgnoreCase(s2.getTime());
            }
        });
        reviewmrecycleAdapter.notifyDataSetChanged();

    }
    public static void Most_recent(){


        Collections.sort(movieList, new Comparator<GoogleReviewsData>(){
            public int compare(GoogleReviewsData s1, GoogleReviewsData s2) {

               return s1.getTime().compareToIgnoreCase(s2.getTime())*-1;
            }
        });

        reviewmrecycleAdapter.notifyDataSetChanged();

    }
    public static  void least_recent(){
        Collections.sort(movieList, new Comparator<GoogleReviewsData>(){
            public int compare(GoogleReviewsData s1, GoogleReviewsData s2) {

                return s1.getTime().compareToIgnoreCase(s2.getTime());
            }
        });

        reviewmrecycleAdapter.notifyDataSetChanged();

    }



}

