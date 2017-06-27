package com.example.nejcvesel.pazikjehodis.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.location.Address;

import com.example.nejcvesel.pazikjehodis.Handlers.Constants;
import com.example.nejcvesel.pazikjehodis.Handlers.FetchAddressIntentService;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.Handlers.MarkerClickHandler;
import com.example.nejcvesel.pazikjehodis.Handlers.OnSwipeTouchListener;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
import java.util.Locale;

/**
 * Created by brani on 12/19/2016.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback,BackendAPICall.BackendCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final static String TAG_FRAGMENT = "MapsFragment";
    private BackendAPICall api;
    private ProgressDialog progressDialog;
    private GoogleMap mMap;
    HashMap<Marker, Location> markerLocationMap = new HashMap<Marker, Location>();
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;



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
        ((MainActivity) getActivity()).mMap = mMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MainActivity main = (MainActivity) getActivity();
                Marker marker = null;
                if(main.markerAddEnable) {
                    if (main.isMarker()) {
                        main.RemoveMarker();
                    }
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(200f))
                            .title("Hello world"));
                    main.AddMarker(marker);
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(main, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                        String add = address;
                        add += (city != null) ? ", " + city : "";
                        add += (state != null) ? ", " + state : "";
                        add += (postalCode != null) ? ", " + postalCode : "";
                        add += (country != null) ? ", " + country : "";
                        add += (postalCode != null) ? ", " + postalCode : "";


                        main.SetLocationInfoContainer(add);
                    }
                    catch (Exception e){
                        Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
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
        markerInfoWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Neki neki");
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

    protected void startIntentService() {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Gets the best and most recent location currently available,
        // which may be null in rare cases when a location is not available.
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//
//        if (mLastLocation != null) {
//            // Determine whether a Geocoder is available.
//            if (!Geocoder.isPresent()) {
//                Toast.makeText(getContext(), "NOT",
//                        Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            if (mAddressRequested) {
//                startIntentService();
//            }
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
//            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
//            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(getContext(), "LATLNG", Toast.LENGTH_LONG).show();
            }

        }
    }
}
