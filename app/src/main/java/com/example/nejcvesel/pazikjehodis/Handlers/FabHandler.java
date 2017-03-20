package com.example.nejcvesel.pazikjehodis.Handlers;


import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Path;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.nejcvesel.pazikjehodis.Fragments.LocationFormFragment;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.konifar.fab_transformation.FabTransformation;

import java.util.ArrayList;

/**
 * Created by brani on 3/6/2017.
 */

public class FabHandler implements FloatingActionButton.OnClickListener {
    private FloatingActionButton fab,fab1,fab2;
    private Button button_cancel, button_save;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Activity activity;
    private boolean isFabOpen, isFabEnable;
    private ArrayList<Point> positions = new ArrayList<>(); // First item of array is position on fab and secound is on fab1
    public FabHandler(Activity activity){
        this.activity = activity;
        this.fab = (FloatingActionButton)activity.findViewById(R.id.fab);
        this.fab1 = (FloatingActionButton)activity.findViewById(R.id.fab1);
//        this.fab2 = (FloatingActionButton)activity.findViewById(R.id.fab2);
        this.button_cancel = (Button)activity.findViewById(R.id.button_cancel);
        this.button_save = (Button)activity.findViewById(R.id.button_save);
        this.fab_open = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fab_open);
        this.fab_close = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.fab_close);
        this.rotate_forward = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.rotate_forward);
        this.rotate_backward = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.rotate_backward);
        this.isFabEnable = true;
        this.isFabOpen = false;
        this.positions.add(new Point(this.fab.getX(),this.fab.getY()));
        this.positions.add(new Point(this.fab1.getX(),this.fab1.getY()));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == this.fab.getId()){
            ((MainActivity) this.activity).CloseMarkerInfoWindow();
            OpenFab();
            EnableAddLocation();
        }
        if(v.getId() == this.fab1.getId()){
            // implement my current location
        }

        if(v.getId() == this.button_cancel.getId()){
            CloseFab();
            DisableAddLocation();
            ((MainActivity) this.activity).RemoveMarker();
        }

        if(v.getId() == this.button_save.getId()){
            System.out.println("CLICKED SAVE BTN");
            if(((MainActivity) this.activity).isMarker()){

                CloseFab();
                DisableFab();
                FragmentManager fm = this.activity.getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.content_frame,new LocationFormFragment(),"LocationFormFragment")
                        .addToBackStack("LocationFormFragment")
                        .commit();
                // open form location fragment
            }
        }
    }

    private void AnimationOpenClose(boolean open){
        View toolbar = (View)this.activity.findViewById(R.id.toolbar_footer);
        if(!open){
//            this.fab.startAnimation(rotate_backward);
//            this.fab1.startAnimation(fab_close);
//            this.fab2.startAnimation(fab_close);
//            this.fab1.setClickable(false);
//            this.fab2.setClickable(false);

            final FloatingActionButton f = (FloatingActionButton) this.activity.findViewById(R.id.fab);
            ((MainActivity)this.activity).RemoveMarker();
            FabTransformation.with(this.fab)
                    .transformFrom(toolbar);
            this.isFabOpen = false;
        } else {
//            this.fab.startAnimation(rotate_forward);
//            this.fab1.startAnimation(fab_open);
//            this.fab2.startAnimation(fab_open);
//            this.fab1.setClickable(true);
//            this.fab2.setClickable(true);


            FabTransformation.with(this.fab).transformTo(toolbar);
            this.isFabOpen = true;
        }
    }

    public void OpenFab(){
        if(!isFabOpen)
            AnimationOpenClose(true);
    }

    public void CloseFab(){
        if(isFabOpen)
            AnimationOpenClose(false);
    }

    public void DisableFab(){
        CloseFab();
        View toolbar = (View)this.activity.findViewById(R.id.toolbar_footer);
        final FloatingActionButton f = (FloatingActionButton) this.activity.findViewById(R.id.fab);
        ((MainActivity)this.activity).RemoveMarker();
        FabTransformation.with(this.fab)
                .setListener(new FabTransformation.OnTransformListener() {
                    @Override
                    public void onStartTransform() {
                        //
                    }

                    @Override
                    public void onEndTransform() {
                        f.setVisibility(View.GONE);
                    }
                })
                .transformFrom(toolbar);
        this.fab.setVisibility(View.GONE);
        this.fab1.setVisibility(View.GONE);
        this.isFabEnable = false;
    }

    public void EnableFab(){
        this.fab.setVisibility(View.VISIBLE);
        this.fab1.setVisibility(View.VISIBLE);
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

class Point{
    double x,y;
    public Point(double x,double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
