package ru.conv.currencyconverter.model;

import com.google.android.gms.maps.model.LatLng;

public class BankPoint {
    private final String title;
    private final LatLng latLng;


    public BankPoint(String title, double latitude, double longitude) {
        this.title = title;
        this.latLng = new LatLng(latitude, longitude);
    }

    public String getTitle() {
        return title;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
