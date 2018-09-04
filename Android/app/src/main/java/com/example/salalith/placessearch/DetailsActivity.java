package com.example.salalith.placessearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.salalith.placessearch.ListActivity.details;
import static com.example.salalith.placessearch.ListActivity.myPlace;

public class DetailsActivity extends AppCompatActivity  {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static Place ptemp;
    public static JSONObject googlePlace,yelpreviews;
    public static ArrayList<GoogleReviewsData> g1=new ArrayList<>();
    public static ArrayList<GoogleReviewsData> g2=new ArrayList<>();

    public static int yelp=0;
    private TextView fav;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA");
        Log.d("Details",myPlace.getName()+"");
        fav=(TextView) findViewById(R.id.button1);
        fav.setTextColor(getResources().getColor(android.R.color.transparent));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        ptemp=myPlace;
        if(ptemp.getName().length()>24)
            toolbar.setTitle(ptemp.getName().subSequence(0,24)+"...");
        else
            toolbar.setTitle(ptemp.getName());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        try {
            yelpreviews=new JSONObject(details.get(1));

            g1.clear();
            g2.clear();


/*            if(details.get(1).equals("not found")){
                yelp=-1;
            }
            else {
                yelpreviews=new JSONObject(details.get(1));
                if(yelpreviews.has("reviews")) {
                    JSONArray reviewsYelp = yelpreviews.getJSONArray("reviews");


                    for (int i = 0; i < reviewsYelp.length(); i++) {
                        JSONObject r1 = reviewsYelp.getJSONObject(i);
                        JSONObject ruser = r1.getJSONObject("user");
                        GoogleReviewsData gry = new GoogleReviewsData(ruser.getString("name"), r1.getString("url"), ruser.getString("image_url"), r1.getString("text"), r1.getInt("rating"), r1.getString("time_created"));
                        g2.add(gry);
                    }

                }
            }*/
        }catch (Exception e){
        }



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    InfoTab tab1 = new InfoTab();
                    return tab1;
                case 1:
                    PhotosTab tab2 = new PhotosTab();
                    return tab2;
                case 2:
                    MapTab tab3 = new MapTab();
                    return tab3;
                case 3:
                    ReviewsTab tab4 = new ReviewsTab();
                    return tab4;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }



    public void addfav(View view) {
        fav.setTextColor(getResources().getColor(android.R.color.transparent));


        Log.d("Debug",fav.getDrawingCacheBackgroundColor()+"");
        /*if(fav.getBackground().equals("android.graphics.drawable.VectorDrawable@4c8f16d")){
            fav.setBackground(getResources().getDrawable(R.drawable.heart_fill_white));
        }
        else {
            fav.setBackground(getResources().getDrawable(R.drawable.hear_outline_white));

        }*/
        if(fav.getText().equals("out")) {
            fav.setBackground(getResources().getDrawable(R.drawable.heart_fill_white));
            fav.setText("fill");
            Toast.makeText(this,myPlace.getName()+" was added to favourites",Toast.LENGTH_LONG).show();

        }else{
            fav.setBackground(getResources().getDrawable(R.drawable.hear_outline_white));
            Toast.makeText(this,myPlace.getName()+" was removed from favourites",Toast.LENGTH_LONG).show();

            fav.setText("out");
        }
    }



    public void share(View view) {

        String tweet="Check out "+ptemp.getName()+" located at "+ptemp.getAddress()+" Website: "+ptemp.getWebsiteUri();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text="+tweet));
        startActivity(browserIntent);

    }

}
