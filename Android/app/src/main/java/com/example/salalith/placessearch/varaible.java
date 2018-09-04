package com.example.salalith.placessearch;

public class varaible {
    private PhotosTab t1;
    private String a,b;

    public varaible(PhotosTab t1, String a) {
        this.t1 = t1;
        this.a = a;
    }

    public PhotosTab getT1() {
        return t1;
    }

    public void setT1(PhotosTab t1) {
        this.t1 = t1;
    }

    public String getA() {

        return a;
    }

    public void setA(String a,String b) {
        t1.sendRequest(a,b);

        this.a = a;
    }
}
