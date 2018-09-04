package com.example.salalith.placessearch;

import android.graphics.Bitmap;

public class ImageData {


    private Bitmap image;

     public ImageData(Bitmap image){
        this.image=image;

    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}
