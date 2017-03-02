package com.example.nejcvesel.pazikjehodis;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by brani on 12/19/2016.
 */
//TODO: add check when photo added
//TODO: implement for different/lower apk versions file upload/ test as well
//TODO: get address for pin location

public class LocationFormFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_location_form, container, false);
        TextView ownerName = (TextView) myInflatedView.findViewById(R.id.inputName);
        MainActivity main = (MainActivity) getActivity();
        ownerName.setText(main.profile.getFirstName() + main.profile.getLastName());
        return myInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}