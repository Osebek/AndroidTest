package com.example.nejcvesel.pazikjehodis.Walkthrough;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nejcvesel.pazikjehodis.R;

public class WalktroughSliderFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_walkthrough_slide, container, false);

        return rootView;
    }
}

