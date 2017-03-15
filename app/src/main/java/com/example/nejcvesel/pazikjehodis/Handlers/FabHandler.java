package com.example.nejcvesel.pazikjehodis.Handlers;


import android.app.Activity;
import android.graphics.Path;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;

/**
 * Created by brani on 3/6/2017.
 */

public class FabHandler implements FloatingActionButton.OnClickListener {
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Activity activity;
    private boolean isFabOpen, isFabEnable;
    public FabHandler(Activity activity){
        this.activity = activity;
        this.fab = (FloatingActionButton)activity.findViewById(R.id.fab);
        this.fab1 = (FloatingActionButton)activity.findViewById(R.id.fab1);
        this.fab2 = (FloatingActionButton)activity.findViewById(R.id.fab2);
        this.fab_open = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fab_open);
        this.fab_close = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.fab_close);
        this.rotate_forward = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.rotate_forward);
        this.rotate_backward = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.rotate_backward);
        this.isFabEnable = true;
        this.isFabOpen = false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.fab.getId()){
            Log.v("Pressed", "fab");
            ((MainActivity) this.activity).CloseMarkerInfoWindow();
            if(!this.isFabOpen)
                OpenFab();
            else
                CloseFab();
        }
        if(v.getId() == this.fab1.getId()){
            Log.v("Pressed", "fab1");
            if(!((MainActivity) this.activity).markerAddEnable)
                EnableAddLocation();
            else
                DisableAddLocation();
        }
        if(v.getId() == this.fab2.getId()){
            Log.v("Pressed", "fab2");
        }
    }

    private void AnimationFab(boolean open){
        if(!open){
            this.fab.startAnimation(rotate_backward);
            this.fab1.startAnimation(fab_close);
            this.fab2.startAnimation(fab_close);
            this.fab1.setClickable(false);
            this.fab2.setClickable(false);
            this.isFabOpen = false;
        } else {
            this.fab.startAnimation(rotate_forward);
            this.fab1.startAnimation(fab_open);
            this.fab2.startAnimation(fab_open);
            this.fab1.setClickable(true);
            this.fab2.setClickable(true);
            this.isFabOpen = true;
        }
    }

    public void OpenFab(){
        if(!isFabOpen)
            AnimationFab(true);
    }

    public void CloseFab(){
        if(isFabOpen)
            AnimationFab(false);
    }

    public void DisableFab(){
        CloseFab();
        this.fab.setVisibility(View.GONE);
        this.isFabEnable = false;
    }

    public void EnableFab(){
        this.fab.setVisibility(View.VISIBLE);
        CloseFab();
        this.isFabEnable = true;
    }

    private void EnableAddLocation(){
        ((MainActivity) this.activity).markerAddEnable = true;
    }

    private void DisableAddLocation(){
        ((MainActivity) this.activity).markerAddEnable = false;
    }

}
