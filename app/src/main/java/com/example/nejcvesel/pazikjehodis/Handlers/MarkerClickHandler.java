package com.example.nejcvesel.pazikjehodis.Handlers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nejcvesel.pazikjehodis.Fragments.LocationDetailFragment;
import com.example.nejcvesel.pazikjehodis.Fragments.LocationFormFragment;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.ServiceGenerator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MarkerClickHandler implements GoogleMap.OnMarkerClickListener {
    HashMap<Marker,Location> markerLocationMap;
    private Activity activity;

    public MarkerClickHandler(Activity activity, HashMap<Marker,Location> markers){
        this.activity = activity;
        this.markerLocationMap = markers;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
//        ((MainActivity) this.activity).layout.hide();
        MainActivity main = (MainActivity) this.activity;
        if(main.markerAddEnable)
            return true;

        final View markerInfoWindow = this.activity.findViewById(R.id.infoCardMarker);
        AppBarLayout appbar = (AppBarLayout) this.activity.findViewById(R.id.appbar);
        TextView header = (TextView) markerInfoWindow.findViewById(R.id.header_infowindow);
        TextView content = (TextView) markerInfoWindow.findViewById(R.id.content_infowindow);
        ImageView icon = (ImageView) markerInfoWindow.findViewById(R.id.imageHolder);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location loc = markerLocationMap.get(marker);
                Fragment fragment = LocationDetailFragment.newInstance(loc);
                FragmentManager fragmentManager = activity.getFragmentManager();
                ((MainActivity) activity).HideFab();
                markerInfoWindow.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, "LocationDetailFragment");
                fragmentTransaction.addToBackStack("LocationDetailFragment");
                fragmentTransaction.commit();

            }
        });

        if(markerInfoWindow.getX() != 0){
            markerInfoWindow.animate().x(0).y(-200).alpha(1.0f).setDuration(0).start();
            markerInfoWindow.setVisibility(View.GONE);
            ProgressBar bar = (ProgressBar) markerInfoWindow.findViewById(R.id.progressBar1);
            bar.setVisibility(View.VISIBLE);
        }

        if(markerInfoWindow.getVisibility() == View.VISIBLE){
            ProgressBar bar = (ProgressBar) markerInfoWindow.findViewById(R.id.progressBar1);
            bar.setVisibility(View.VISIBLE);
        }else{
            markerInfoWindow.setVisibility(View.VISIBLE);
            markerInfoWindow.setAlpha(0.0f);
            markerInfoWindow.animate().translationY(markerInfoWindow.getHeight() + appbar.getHeight()).setDuration(50).alpha(1.0f);
        }
        Location loc = this.markerLocationMap.get(marker);
        if (loc != null) {
            String image;
            try{
                header.setText(loc.getText());
                content.setText(loc.getAddress());
                image = ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(loc.getPicture());
            }catch (Exception e){
                Toast.makeText(markerInfoWindow.getContext(), R.string.error_load_info, Toast.LENGTH_LONG).show();
                return true;
            }
            Picasso.with(markerInfoWindow.getContext()).load(image).noFade()
                    .into(icon,new MarkerCallback(markerInfoWindow));
        }
        else
        {

        }


        return true;
    }

    static class MarkerCallback implements com.squareup.picasso.Callback {
        View view=null;

        MarkerCallback(View v) {
            this.view=v;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            new CountDownTimer(500, 500) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar1);
                    bar.setVisibility(View.GONE);
                }
            }.start();
        }
    }
}
