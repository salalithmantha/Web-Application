package com.example.salalith.placessearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.places.Places;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import static com.example.salalith.placessearch.DetailsActivity.ptemp;

public class InfoTab  extends Fragment {

    private TextView address,phone,price,gpage,website;
    private RatingBar rating;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.infotab, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try{
            JSONObject jres=new JSONObject(ListActivity.details.get(0));
            JSONObject kres=jres.getJSONObject("result");
            address=(TextView) view.findViewById(R.id.address);
            phone=(TextView) view.findViewById(R.id.phone);
            price=(TextView) view.findViewById(R.id.price);
            rating=(RatingBar) view.findViewById(R.id.rating);
            gpage=(TextView) view.findViewById(R.id.gpage);
            website=(TextView) view.findViewById(R.id.website);

            website.setClickable(true);
            website.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='"+ptemp.getWebsiteUri()+"'>"+ptemp.getWebsiteUri() +" </a>";
            website.setText(Html.fromHtml(text));

            gpage.setClickable(true);
            gpage.setMovementMethod(LinkMovementMethod.getInstance());
            String text1 = "<a href='"+kres.getString("url")+"'>"+kres.getString("url")+" </a>";
            gpage.setText(Html.fromHtml(text1));

            String Dollar="";
            for(int i=0;i<ptemp.getPriceLevel();i++){
                Dollar+="$";
            }



            address.setText(ptemp.getAddress());
            phone.setText(ptemp.getPhoneNumber());
            price.setText(Dollar);
            rating.setRating(ptemp.getRating());
            //gpage.setText(kres.getString("url"));
            //website.setText(ptemp.getWebsiteUri()+"");



        }
        catch (Exception e){

        }




    }
}