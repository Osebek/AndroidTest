package com.example.nejcvesel.pazikjehodis.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.Handlers.MarkerClickHandler;
import com.example.nejcvesel.pazikjehodis.Handlers.OnSwipeTouchListener;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by brani on 12/19/2016.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback,BackendAPICall.BackendCallback {
    private final static String TAG_FRAGMENT = "MapsFragment";
    private BackendAPICall api;
    private ProgressDialog progressDialog;
    private GoogleMap mMap;
    HashMap<Marker, Location> markerLocationMap = new HashMap<Marker, Location>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = MapFragment.newInstance();
        fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        //MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        api = new BackendAPICall(this, "");
        progressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Pridobivam lokacije");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ljubljana = new LatLng(46.056946, 14.505751);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ljubljana));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MainActivity main = (MainActivity) getActivity();
                Marker marker = null;
                if(main.markerAddEnable)
                if (main.isMarker()) {
                    main.RemoveMarker();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(200f))
                        .title("Hello world"));
                main.AddMarker(marker);
            }


        });

        mMap.setOnMarkerClickListener(new MarkerClickHandler(getActivity(), markerLocationMap));
        View markerInfoWindow = getActivity().findViewById(R.id.infoCardMarker);
        markerInfoWindow.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()) {
            public void onSwipeTop() {
                Log.v("SWIPE", "YUHUUU BRANE SWIPE TOP");
            }

            public void onSwipeRight() {
                Log.v("SWIPE", "YUHUUU BRANE SWIPE RIGHT");
            }

            public void onSwipeLeft() {
                Log.v("SWIPE", "YUHUUU BRANE SWIPE LEFT");
            }

            public void onSwipeBottom() {
                Log.v("SWIPE", "YUHUUU BRANE SWIPE BOTTOM");
            }
        });

        MainActivity main = (MainActivity) getActivity();
        progressDialog.show();
        if (main.pathLocations.size() > 0) {
            for (int i = 0; i < main.pathLocations.size(); i++) {
                api.getSpecificLocation(main.pathLocations.get(i));
            }
        } else {
            api.getAllLocations("");
        }
    }

    @Override
    public void getAllPathsCallback(List<Path> paths, String message) {

    }

    @Override
    public void getAllLocationsCallback(List<Location> loactions, String message) {
        if(message.equals("OK")) {
            Log.v("Get All Location", "Active");
            for (Location loc : loactions) {
                LatLng lok = new LatLng(Double.valueOf(loc.getLatitude()), Double.valueOf(loc.getLongtitude()));
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(lok)
                        .title(loc.getTitle()));
                markerLocationMap.put(marker, loc);

            }
            progressDialog.hide();
        }
        if(message.equals("ERROR"))
        {
            progressDialog.hide();
            CharSequence text = "Nalaganje lokacije ni uspelo. Napaka na strežniku.";
            Toast opozorilo = Toast.makeText(getActivity(),text,Toast.LENGTH_LONG);
            opozorilo.show();


        }
        if(message.equals("ERROR_FAIL"))
        {
            progressDialog.hide();
            CharSequence text = "Nalaganje lokacije ni uspelo. Ni povezave s strežnikom";
            Toast opozorilo = Toast.makeText(getActivity(),text,Toast.LENGTH_LONG);
            opozorilo.show();

        }
    }

    @Override
    public void getSpecificLocationCallback(Location loaction, String message) {
        Log.v("Get Specific Location", "Active");
        LatLng lok = new LatLng(Double.valueOf(loaction.getLatitude()), Double.valueOf(loaction.getLongtitude()));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(lok)
                .title(loaction.getTitle()));
        markerLocationMap.put(marker, loaction);
        progressDialog.hide();
    }

    @Override
    public void getSpecificUserCallback(User user, String message) {

    }

    @Override
    public void getAllUsersCallback(List<User> user, String message) {

    }

    @Override
    public void getUserLocationCallback(List<Location> location, String message) {

    }

    @Override
    public void getUserProfileCallback(User user, String message) {

    }

    @Override
    public void getRefreshTokeneCallback(BackendToken token, String message) {

    }

    @Override
    public void getConvertTokenCallback(BackendToken token, String message) {

    }

    @Override
    public void getAddMessageCallback(String message, String backendCall) {

    }

    @Override
    public void getUserPathsCallback(List<Path> userPaths, String message) {

    }

}
