package com.example.salalith.placessearch;

import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.salalith.placessearch.SearchTab.message;

public class ListActivity extends AppCompatActivity {

    private List<PlaceSG> placeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlacesAdapter mAdapter;
    private ArrayList<JSONObject> g = new ArrayList<>();
    private JSONObject nbs;
    private Gson gson;
    private int page = 0;
    private ImageButton favicon;
    private Button next, prev;
    protected GeoDataClient mGeoDataClient;
    public static Place myPlace;
    public static PlaceBufferResponse places;
    private RequestQueue requestQueue;
    public static ArrayList<String> details=new ArrayList<>();
    private ProgressDialog progress;
    private TextView nores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        details.clear();
        nores=(TextView)findViewById(R.id.textView17);
        nores.setVisibility(TextView.GONE);

        MapTab.mGeoDataClient = Places.getGeoDataClient(this, null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGeoDataClient = Places.getGeoDataClient(this, null);
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        page = 0;
        favicon = (ImageButton) findViewById(R.id.favicon);

        Intent intent = getIntent();
        ArrayList<String> rbc = intent.getStringArrayListExtra("EXTRA_MESSAGE");
        Log.d("new activity", message.get(0));

        try {
            for (int i = 0; i < message.size(); i++) {

                nbs = new JSONObject(message.get(i));
                g.add(nbs);
                Log.d("new Activity", g.get(0).getString("next_page_token"));

            }

            /*nbs = g.get(page);
            JSONArray resarraytemp = nbs.getJSONArray("results");
            if(resarraytemp.length()==0){
                nores.setVisibility(TextView.VISIBLE);
            }
            else{
                nores.setVisibility(TextView.INVISIBLE);
            }*/

        } catch (Exception e) {

        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.previous);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new PlacesAdapter(placeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {





            @Override
            public void onClick(View view, int position) {
                progressbar();
                MapTab.photosDataList.clear();
                MapTab.movieList.clear();
                PlaceSG place1 = placeList.get(position);

                MapTab.getPhotos(place1.getPlaceID());

//                Toast.makeText(getApplicationContext(), place1.getName() + " is selected!", Toast.LENGTH_SHORT).show();

                fetchPosts("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place1.getPlaceID()+"&key=AIzaSyBRWIU6rOkQFYOelBkXXhKUU9QnSd1P2lQ");

                mGeoDataClient.getPlaceById(place1.getPlaceID()).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                        if (task.isSuccessful()) {
                            places = task.getResult();
                            myPlace = places.get(0);

                            Log.i("placedet", "Place found: " + myPlace.getName());

                            progress.cancel();
                           Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
                            intent.putExtra("EXTRA", "hello");
                            startActivity(intent);

                            //places.release();

                        } else {
                            Log.e("placedet", "Place not found.");
                        }
                    }
                });


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareMovieData();

    }



    private void prepareMovieData() {
        try {

            nbs = g.get(page);
            JSONArray resarray = nbs.getJSONArray("results");
            if (nbs.has("results")&& resarray.length()!=0) {
                nores.setVisibility(TextView.INVISIBLE);
                next.setVisibility(Button.VISIBLE);
                prev.setVisibility(Button.VISIBLE);

                for (int i = 0; i < resarray.length(); i++) {
                    JSONObject r = resarray.getJSONObject(i);
                    PlaceSG p = new PlaceSG(r.getString("name"), r.getString("vicinity"), r.getString("icon"), "heart_outline_black.xml", r.getString("place_id"));
                    placeList.add(p);
                }
            }else{
                nores.setVisibility(TextView.VISIBLE);
                Toast.makeText(this,"No Results Found",Toast.LENGTH_LONG).show();
                nores.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                nores.setText("No results");
                next.setVisibility(Button.INVISIBLE);
                prev.setVisibility(Button.INVISIBLE);
            }

        } catch (Exception e) {

        }
        if (page == g.size() - 1) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
        }
        if (page == 0) {
            prev.setEnabled(false);
        } else {
            prev.setEnabled(true);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void previous(View v) {
        if (page > 0) {
            page--;
            placeList.clear();

            prepareMovieData();
        }
        mAdapter.notifyDataSetChanged();


    }

    public void next(View v) {
        if (page < g.size() - 1) {
            Log.d("click", "cliked me!!!!");

            page++;
            placeList.clear();
            prepareMovieData();


        }
        mAdapter.notifyDataSetChanged();

    }


    private void fetchPosts(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, onPostsLoaded, onPostsError);
        requestQueue.add(request);

    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            details.add(response);


            if (details.size() == 2) {
                /*Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
                intent.putExtra("EXTRA", "hello");
                startActivity(intent);*/

            } else {

                try {
                    String state = "", city = "", zipcode = "", country = "";
                    JSONObject det = new JSONObject(response);
                    JSONObject results = det.getJSONObject("result");
                    JSONArray addr = results.getJSONArray("address_components");

                    for (int i = 0; i < addr.length(); i++) {
                        JSONObject inner = addr.getJSONObject(i);
                        JSONArray types = inner.getJSONArray("types");
                        for (int j = 0; j < types.length(); j++) {
                            if (types.getString(j).equals("administrative_area_level_1")) {
                                state = inner.getString("short_name");
                            }
                            if (types.getString(j).equals("locality")) {
                                city = inner.getString("short_name");
                            }
                            if (types.getString(j).equals("postal_code")) {
                                zipcode = inner.getString("short_name");
                            }
                            if (types.getString(j).equals("country")) {
                                country = inner.getString("short_name");
                            }


                        }
                    }
                    Log.d("yelp", "" + state + city + zipcode + country);
                    String address[] = results.getString("formatted_address").split(",");
                    String phone = results.getString("international_phone_number").replaceAll(" ", "");
                    phone = phone.replaceAll("-", "");
                    phone = phone.replace("+", "");

                    fetchPosts("https://salalithtravelandenter.appspot.com/yelppoint?name=" + results.getString("name") + "&address1=" + address[0] + "&address2=" + address[1] + "&city=" + city + "&state=" + state + "&country=" + country + "&zipcode=" + zipcode + "&phone=" + phone);


                } catch (Exception e) {

                }


            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {

                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void progressbar(){
        progress=new ProgressDialog(this);
        progress.setMessage("Fetching Details");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        progress.setCancelable(true);

        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime) {
                    try {
                        sleep(200);
                        jumpTime += 5;
                        progress.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }


}
