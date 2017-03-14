package com.example.nejcvesel.pazikjehodis;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.LocationInterface;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.ServiceGenerator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brani on 12/19/2016.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private final static String TAG_FRAGMENT = "MapsFragment";

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
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener ()
//        {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                View markerInfoWindow = getActivity().findViewById(R.id.infoCardMarker);
//                if(markerInfoWindow.getVisibility() != View.INVISIBLE){
//                    markerInfoWindow.animate().translationY(-200).alpha(0.0f);
////                    markerInfoWindow.setVisibility(View.GONE);
//                }
//                Location loc = markerLocationMap.get(marker);
//                if (loc != null) {
//                    AppBarLayout appbar = (AppBarLayout) getActivity().findViewById(R.id.appbar);
//                    TextView header = (TextView) markerInfoWindow.findViewById(R.id.header_infowindow);
//                    TextView content = (TextView) markerInfoWindow.findViewById(R.id.content_infowindow);
//                    ImageView icon = (ImageView) markerInfoWindow.findViewById(R.id.imageHolder);
//
//                    String image;
//                    try{
//                        header.setText(loc.getText());
//                        content.setText(loc.getAddress());
//                        image = ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(loc.getPicture());
//                    }catch (Exception e){
//                        Toast.makeText(markerInfoWindow.getContext(), R.string.error_load_info, Toast.LENGTH_LONG).show();
//                        return true;
//                    }
//
//                    markerInfoWindow.setVisibility(View.VISIBLE);
//                    markerInfoWindow.setAlpha(0.0f);
//                    markerInfoWindow.animate().translationY(markerInfoWindow.getHeight() + appbar.getHeight()).alpha(1.0f);
//                    Picasso.with(markerInfoWindow.getContext()).load(image).noFade()
//                            .placeholder(R.drawable.progress_animation).into(icon,new MarkerCallback(markerInfoWindow));
////                        .into(icon, new MarkerCallback(marker));
//                }
//                else
//                {
////                    ((MainActivity)getActivity()).OpenFormFragment();
//
//                }
//
//                return true;
//            }
//        });

//        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mMap.setInfoWindowAdapter(new PopupAdapter(getActivity(), inflater, markerLocationMap));

//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//
//                // Get location from marker
//                Location loc = markerLocationMap.get(marker);
//                // Get layout tooltpi
//                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService
//                        (Context.LAYOUT_INFLATER_SERVICE);
//                View view = inflater.inflate(R.layout.location_details_marker,null);
//                // Set values to the view
//                TextView header = (TextView) view.findViewById(R.id.header_infowindow);
//                header.setText(loc.getText());
//                TextView content = (TextView) view.findViewById(R.id.content_infowindow);
//                content.setText(loc.getAddress());
//                ImageView image = (ImageView) view.findViewById(R.id.imageHolder);
//                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
//                progressBar.setVisibility(View.VISIBLE);
//                //declare callback
//                Log.v("WindowInfo: ", "Retriving view to marker");
//
//                // Server call for pic
//                System.out.println(BackendAPICall.repairURL(loc.getPicture()));
//                Picasso.with(view.getContext()).load(ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(loc.getPicture()))
//                        .transform(new RoundedTransformation(15, 0))
//                        .fit()
//                        .centerCrop()
//                        .into(image, new com.squareup.picasso.Callback(){
//                            @Override
//                            public void onSuccess() {
//                                Log.v("WindowInfo: ", "Set Loader invisible");
//                                progressBar.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onError() {
//
//                            }
//                        });
//            return view;
//            }

//            @Override
//            public View getInfoContents(Marker marker) {
//                return null;
//            }

//        });

        MainActivity main = (MainActivity) getActivity();
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

        if (main.pathLocations.size() > 0) {
            for (int i = 0; i < main.pathLocations.size(); i++) {
                Location location = new Location();
                LocationInterface service =
                        ServiceGenerator.createUnauthorizedService(LocationInterface.class);

                Call<Location> call = service.getSpecificLocation(main.pathLocations.get(i));
                call.enqueue(new Callback<Location>() {
                    @Override
                    public void onResponse(Call<Location> call, Response<Location> response) {
                        Location loc = response.body();
                        LatLng lok = new LatLng(Double.valueOf(loc.getLatitude()), Double.valueOf(loc.getLongtitude()));
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(lok)
                                .title(loc.getTitle()));
                        markerLocationMap.put(marker, loc);

                    }

                    @Override
                    public void onFailure(Call<Location> call, Throwable t) {
                        System.out.println("Fetching locations did not work");
                    }
                });

            }
        } else {
            LocationInterface service =
                    ServiceGenerator.createUnauthorizedService(LocationInterface.class);

            Call<List<Location>> call = service.getAllLocations();
            call.enqueue(new Callback<List<Location>>() {
                @Override
                public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                    List<Location> locations = response.body();
                    for (Location loc : locations) {
                        LatLng lok = new LatLng(Double.valueOf(loc.getLatitude()), Double.valueOf(loc.getLongtitude()));
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(lok)
                                .title(loc.getTitle()));
                        markerLocationMap.put(marker, loc);

                    }

                }

                @Override
                public void onFailure(Call<List<Location>> call, Throwable t) {
                    System.out.println("Fetching locations did not work");
                }
            });

        }


    }
}
