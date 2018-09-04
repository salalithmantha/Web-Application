package com.example.salalith.placessearch;

public class PlaceSG {


    private String icon;
    private String name;
    private String genre;
    private String favicon;



    private String placeID;

    public PlaceSG() {
    }

    public PlaceSG(String name, String genre, String icon, String favicon,String placeID) {
        this.name = name;
        this.genre = genre;
        this.icon=icon;
        this.favicon=favicon;
        this.placeID=placeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAddr() {
        return genre;
    }

    public void setAddr(String genre) {
        this.genre = genre;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }
    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}