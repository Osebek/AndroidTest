package com.example.nejcvesel.pazikjehodis.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;

/**
 * Created by brani on 12/19/2016.
 */
//TODO: add check when photo added
//TODO: implement for different/lower apk versions file upload/ test as well
//TODO: get address for pin location

public class LocationFormFragment extends Fragment {
    private static final String ARG_OWNER = "owner";
    private static final String ARG_TEXT = "text";
    private static final String ARG_ID = "id";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_PICTURE_URL = "pictureUrl";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGTITUDE = "longtitude";
    private static final String ARG_NAME = "name";
    private static final String ARG_TITLE = "title";
    private static final String ARG_EDIT_MODE = "edit_mode";

    private String mParamText;
    private String mParamTitle;
    private String mParamName;
    private String mParamPicture;
    private String mParamId;
    private String mParamLatitude;
    private String mParamLongtitude;
    private String mParamAddress;
    private String mParamOwner;
    private boolean mParamEditMode;


    public LocationFormFragment() {


    }

    public static LocationFormFragment newInstance(Location loc) {
        LocationFormFragment fragment = new LocationFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OWNER,loc.getOwner());
        args.putString(ARG_TEXT,loc.getText());
        args.putString(ARG_ID,String.valueOf(loc.getId()));
        args.putString(ARG_ADDRESS,loc.getAddress());
        args.putString(ARG_PICTURE_URL,loc.getPicture());
        args.putString(ARG_LATITUDE,loc.getLatitude());
        args.putString(ARG_LONGTITUDE,loc.getLongtitude());
        args.putString(ARG_NAME,loc.getName());
        args.putString(ARG_TITLE,loc.getTitle());
        args.putBoolean(ARG_EDIT_MODE,true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamText = getArguments().getString(ARG_TEXT);
            mParamTitle = getArguments().getString(ARG_TITLE);
            mParamName = getArguments().getString(ARG_NAME);
            mParamPicture = getArguments().getString(ARG_PICTURE_URL);
            mParamId = getArguments().getString(ARG_ID);
            mParamAddress = getArguments().getString(ARG_ADDRESS);
            mParamOwner = getArguments().getString(ARG_OWNER);
            mParamLongtitude = getArguments().getString(ARG_LONGTITUDE);
            mParamLatitude = getArguments().getString(ARG_LATITUDE);
            mParamEditMode = getArguments().getBoolean(ARG_EDIT_MODE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_location_form, container, false);
        TextView ownerName = (TextView) myInflatedView.findViewById(R.id.inputName);
        MainActivity main = (MainActivity) getActivity();
//        ownerName.setText(main.profile.getFirstName() + main.profile.getLastName());
        //TODO: return to upper comented code .... this is just for test!
        ownerName.setText("");
        Button  submit  = ((Button)myInflatedView.findViewById(R.id.upload_location));


        if (mParamEditMode)
        {
            ((TextView)myInflatedView.findViewById(R.id.inputAddress)).setText(mParamAddress);
            ((TextView)myInflatedView.findViewById(R.id.inputDescription)).setText(mParamText);
            ((TextView)myInflatedView.findViewById(R.id.inputTitle)).setText(mParamTitle);
            submit.setText("Posodobi");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity)getActivity();
            }
        });




        return myInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}