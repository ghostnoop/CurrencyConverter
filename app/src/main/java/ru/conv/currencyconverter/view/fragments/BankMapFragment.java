package ru.conv.currencyconverter.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ru.conv.currencyconverter.R;
import ru.conv.currencyconverter.model.BankPoint;

public class BankMapFragment extends Fragment implements OnMapReadyCallback {
    private final List<BankPoint> markers = Arrays.asList(
            new BankPoint("test", 50., 50.),
            new BankPoint("test1", 30., 24.)
    );
    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        for (BankPoint bankPoint : markers) {
            googleMap.addMarker(new MarkerOptions().alpha(1.0f)
                    .position(bankPoint.getLatLng())
                    .title(bankPoint.getTitle())
            );
        }
        requestPermission();

    }


    private void requestPermission() {
        int permissionStatus2 = ContextCompat.checkSelfPermission(
                Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus2 == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices
                    .getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(
                                            location.getLatitude(),
                                            location.getLongitude()),
                                    4f));
                        } else {

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    markers.get(0).getLatLng(), 4f)
                            );
                        }
                    });


        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }


}