package com.example.nejcvesel.pazikjehodis;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class MapsFragment extends Fragment implements OnMapReadyCallback{
    private final static String TAG_FRAGMENT = "MapsFragment";

    private GoogleMap mMap;
    HashMap<Marker,Location> markerLocationMap  = new HashMap<Marker,Location>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = MapFragment.newInstance();
        fm.beginTransaction().replace(R.id.map,mapFragment).commit();
        //MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ljubljana = new LatLng(46.056946, 14.505751);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ljubljana));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                MainActivity main = (MainActivity)getActivity();
                Marker marker = null;
                if(main.isMarker()){
                    main.RemoveMarker();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(200f))
                        .title("Hello world"));
                main.AddMarker(marker);
            }




        });

       /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener ()
        {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Location loc = markerLocationMap.get(marker);
                if (loc != null) {
                    Fragment fragment = LocationDetailFragment.newInstance(loc);
                    FragmentManager fragmentManager = (getActivity()).getFragmentManager();
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment,"LocationDetail");
                    fragmentTransaction.addToBackStack("LocationDetail");
                    fragmentTransaction.commit();
                }
                else
                {
                    ((MainActivity)getActivity()).OpenFormFragment();

                }

                return true;
            }




        });*/

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                // Get location from marker
                Location loc = markerLocationMap.get(marker);
                // Get layout tooltpi
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.location_details_marker,null);
                // Set values to the view
                TextView header = (TextView) view.findViewById(R.id.header_infowindow);
                header.setText(loc.getText());
                TextView content = (TextView) view.findViewById(R.id.content_infowindow);
                content.setText(loc.getAddress());
                ImageView image = (ImageView) view.findViewById(R.id.imageHolder);
//
//                ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.view_switcher);
//                switcher.reset();
                // Server call for pic
                System.out.println(BackendAPICall.repairURL(loc.getPicture()));
                Picasso.with(view.getContext()).load(ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(loc.getPicture()))
                        .resize(300,100)
                        .centerCrop()
                        .into(image);




            return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }

        });
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Pridobivam lokacije");
        progressDialog.show();

        MainActivity main = (MainActivity) getActivity();
        if (main.pathLocations.size() > 0)
        {
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
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<Location> call, Throwable t) {
                        System.out.println("Fetching locations did not work");
                        progressDialog.dismiss();

                    }
                });

            }
        }

        else {


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
                    progressDialog.dismiss();


                }

                @Override
                public void onFailure(Call<List<Location>> call, Throwable t) {
                    System.out.println("Fetching locations did not work");
                    progressDialog.dismiss();

                }
            });

        }



    }




}
