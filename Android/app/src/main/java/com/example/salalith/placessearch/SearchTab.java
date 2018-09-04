package com.example.salalith.placessearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.salalith.placessearch.MainActivity.mCurrentLocation;

public class SearchTab extends Fragment implements View.OnClickListener {


    private static final String ENDPOINT = "https://salalithtravelandenter.appspot.com/endpoint?keyword=cafe&category=default&distance=50&currloc=customloc&location1=Los%20Angeles,%20CA,%20USA&lat=34.0266&long=-118.2831";
    private Gson gson;
    private RequestQueue requestQueue;
    private ArrayList<JSONObject> g = new ArrayList<>();
    public static  ArrayList<String> message = new ArrayList<>();

    public static final String TAG = "AutoCompleteActivity";
    private static final int AUTO_COMP_REQ_CODE = 2;
    private AutoCompleteTextView searchPlace;
    protected GeoDataClient geoDataClient;
    public Button clear;
    private EditText keyword, distance;
    private RadioButton customLoc, otherLoc;
    private Spinner category;
    public static String index = "";
    private TextView keyerror,radioerror;
    private ProgressDialog progress;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.searchtab, container, false);

        Button clear = (Button) rootView.findViewById(R.id.clear);
        clear.setOnClickListener(this);

        Button search = (Button) rootView.findViewById(R.id.search);
        search.setOnClickListener(this);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        searchPlace = (AutoCompleteTextView) getActivity().findViewById(R.id.search_place);
        keyword = (EditText) getActivity().findViewById(R.id.keyword);
        distance = (EditText) getActivity().findViewById(R.id.distance);
        customLoc = (RadioButton) getActivity().findViewById(R.id.cusloc);
        otherLoc = (RadioButton) getActivity().findViewById(R.id.otherloc);
        category = (Spinner) getActivity().findViewById(R.id.category);
        keyerror=(TextView)getActivity().findViewById(R.id.textView12);
        radioerror=(TextView)getActivity().findViewById(R.id.textView11);


        ArrayAdapter<CharSequence> adapterspinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_name, android.R.layout.simple_spinner_item);
        adapterspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapterspinner);
        category.setOnItemSelectedListener(new SpinnerCustomOnItemSelectedListenerSearchTab());


        CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(getActivity());
        searchPlace.setAdapter(adapter);
        searchPlace.setOnItemClickListener(onItemClickListener);


        customLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                if (customLoc.isChecked()) {
                    searchPlace.setText("");
                    searchPlace.setEnabled(false);
                }
            }
        });

        otherLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                if (otherLoc.isChecked()) {

                    searchPlace.setEnabled(true);
                }
            }
        });





    }


    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                   /* Toast.makeText(getActivity(),
                            "selected place "
                                    + ((com.example.salalith.placessearch.PlaceAPI)adapterView.
                                    getItemAtPosition(i)).getPlaceText()
                            , Toast.LENGTH_SHORT).show();*/
                    //do something with the selection
                    searchPlace.setText(((com.example.salalith.placessearch.PlaceAPI) adapterView.getItemAtPosition(i)).getPlaceText());


                }
            };


    private void fetchPosts(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, onPostsLoaded, onPostsError);
        requestQueue.add(request);

    }

    @Override
    public void onPause() {
        super.onPause();
        progress.cancel();
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            Log.i("PostActivity", response);
            message.add(response);
            try {


                JSONObject nbs = new JSONObject(response);
                if (nbs.has("next_page_token"))
                    Log.d("JSON", nbs.getString("next_page_token"));
                else {
                    //progress.cancel();

                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putStringArrayListExtra("EXTRA_MESSAGE", message);
                    startActivity(intent);

                }

                g.add(nbs);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (g.size() != 0 && nbs.getString("next_page_token") != null) {
                    String url = "https://salalithtravelandenter.appspot.com/nextpoint?nextpage=" + nbs.getString("next_page_token");
                    fetchPosts(url);

                }
            }
            catch (Exception e){

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:

                keyword.setText("");
                searchPlace.setText("");
                distance.setText("");
                customLoc.setChecked(true);
                otherLoc.setChecked(false);
                category.setSelection(0);
                keyerror.setText("");
                radioerror.setText("");

                        Log.d("clear", "clear");
                break;

            case R.id.search:
                Log.d("search",keyword.getText()+" "+searchPlace.getText()+" "+distance.getText()+" "+customLoc.isChecked()+" "+otherLoc.isChecked()+" "+index );


                if(keyword.getText().toString().trim().equals("") ||(searchPlace.getText().toString().trim().equals("")&&otherLoc.isChecked()==true)){
                if(keyword.getText().toString().trim().equals("")){
                    keyerror.setText("Please enter mandatory field");
                    Toast.makeText(getActivity(),"Please fix all fields with errors",Toast.LENGTH_SHORT).show();
                }
                if(searchPlace.getText().toString().trim().equals("")&&otherLoc.isChecked()==true){
                    radioerror.setText("Please enter mandatory field");
                    Toast.makeText(getActivity(),"Please fix all fields with errors",Toast.LENGTH_SHORT).show();
                }
                }
                else {
                    message.clear();

                    String key = keyword.getText() + "";
                    key = key.replaceAll(" ", "+");
                    String cate = getResources().getStringArray(R.array.array_name)[category.getSelectedItemPosition()].replaceAll(" ", "_");
                    int dist = 10;
                    if (distance.getText().toString() != null && distance.getText().toString().length() > 0) {
                        dist = Integer.parseInt(distance.getText().toString());

                        if (dist > 50) {
                            dist = 50;
                        }
                    }
                    Log.d("element", distance.getText() + "");

                    Log.d("element", dist+ "");


                    String loc = searchPlace.getText() + "";
                    loc = loc.replaceAll(" ", "+");
                    String ENDPOINTnow = "";
                    if (customLoc.isChecked() == true) {
                        ENDPOINTnow = "https://salalithtravelandenter.appspot.com/endpoint?keyword=" + key + "&category=" + cate + "&distance=" + dist + "&currloc=currloc&location1=" + loc + "&lat=" + mCurrentLocation.getLatitude() + "&long=" + mCurrentLocation.getLongitude();
                    } else {
                        ENDPOINTnow = "https://salalithtravelandenter.appspot.com/endpoint?keyword=" + key + "&category=" + cate + "&distance=" + dist + "&currloc=customloc&location1=" + loc + "&lat=" + mCurrentLocation.getLatitude() + "&long=" + mCurrentLocation.getLongitude();

                    }

                    progressbar();
                    Log.d("link",ENDPOINTnow);
                    fetchPosts(ENDPOINTnow);
                }
                break;
        }

    }


    public static void getspinnerString() {
        index = "";
    }


    public void progressbar(){
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Fetching Results");
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
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }





}



