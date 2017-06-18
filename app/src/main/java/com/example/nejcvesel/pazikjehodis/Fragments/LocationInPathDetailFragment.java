package com.example.nejcvesel.pazikjehodis.Fragments;

/**
 * Created by nejcvesel on 16/02/17.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.Utility.OnSwipeTouchListener;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationInPathDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TEXT = "text";
    private static final String ARG_TITLE = "title";
    private static final String ARG_NAME = "name";
    private static final String ARG_PICTURE = "picture";
    private static final String ARG_ID = "id";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_PATH_LOCATIONS = "pathLocations";
    private static final String ARG_NUM_OF_LOCATION = "locations";

    private String mParamText;
    private String mParamTitle;
    private String mParamName;
    private String mParamPicture;
    private String mParamId;
    private String mParamAddress;
    private String[] mpathLocations;
    private int mNumOfLocations;
    private ArrayList<Location> lokacije = new ArrayList<Location>();


    private OnFragmentInteractionListener mListener;

    public LocationInPathDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param loc Parameter 1.
     * @return A new instance of fragment LocationDetail.
     */
    public static LocationInPathDetailFragment newInstance(Location loc, String[] pathLocations, ArrayList<Location> locations) {
        LocationInPathDetailFragment fragment = new LocationInPathDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, loc.getText());
        args.putString(ARG_NAME, loc.getName());
        args.putString(ARG_TITLE,loc.getTitle());
        args.putString(ARG_PICTURE,loc.getPicture());
        args.putString(ARG_ADDRESS,loc.getAddress());
        args.putString(ARG_ID,Integer.toString(loc.getId()));
        args.putInt(ARG_NUM_OF_LOCATION,locations.size());
        args.putStringArray(ARG_PATH_LOCATIONS,pathLocations);

        for (int i = 0 ; i < locations.size(); i++) {
            args.putSerializable(String.valueOf(i), locations.get(i));
        }
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
            mParamPicture = getArguments().getString(ARG_PICTURE);
            mParamId = getArguments().getString(ARG_ID);
            mParamAddress = getArguments().getString(ARG_ADDRESS);
            mpathLocations = getArguments().getStringArray(ARG_PATH_LOCATIONS);
            mNumOfLocations = getArguments().getInt(ARG_NUM_OF_LOCATION);
            this.lokacije.clear();
            for (int i = 0; i < mNumOfLocations ; i++)
            {
                this.lokacije.add((Location) getArguments().getSerializable(String.valueOf(i)));
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_location_in_path_detail, container, false);
        final TextView text = (TextView) myInflatedView.findViewById(R.id.locationDetailText);
        final TextView title = (TextView) myInflatedView.findViewById(R.id.locationDetailTitle);
        final TextView name = (TextView) myInflatedView.findViewById(R.id.locationDetailName);
        final ImageView picture = (ImageView) myInflatedView.findViewById(R.id.locationDetailPicture);
        final ImageView loc_icon = (ImageView) myInflatedView.findViewById(R.id.location_icon);
        final TextView goLeft = (TextView) myInflatedView.findViewById(R.id.goLeft);
        final TextView goRight = (TextView) myInflatedView.findViewById(R.id.goRight);
        final TextView navContents = (TextView) myInflatedView.findViewById(R.id.contents);
        final ScrollView scroll = (ScrollView) myInflatedView.findViewById(R.id.content);

        scroll.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                int curr = getCurrentLoc(mParamId,mpathLocations);
                if (curr > 0)
                {
                    curr=curr-1;
                    Location loc = lokacije.get(curr);
                    replace(name,title,text,loc.getName(),loc.getAddress(),container,picture,loc.getTitle(),loc.getText(),loc.getPicture());
                    mParamId = String.valueOf(loc.getId());
                    navContents.setText(String.valueOf(curr+1) + "/" + String.valueOf(mNumOfLocations));

                }
            }

            @Override
            public void onSwipeLeft()
            {
                int curr = getCurrentLoc(mParamId,mpathLocations);
                if (curr < mpathLocations.length-1)
                {
                    System.out.println(Arrays.toString(mpathLocations));
                    curr=curr+1;
                    Location loc = lokacije.get(curr);
                    replace(name,title,text,loc.getName(),loc.getAddress(),container,picture,loc.getTitle(),loc.getText(),loc.getPicture());
                    mParamId = String.valueOf(loc.getId());
                    navContents.setText(String.valueOf(curr+1) + "/" + String.valueOf(mNumOfLocations));


                }

            }
        });





       /* loc_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity) getActivity();
                String loc = "[" + mParamId + "]";
                main.showLocationOnMap(v,loc);
            }
        });*/

        goLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = getCurrentLoc(mParamId,mpathLocations);
                if (curr > 0)
                {
                    curr=curr-1;
                    Location loc = lokacije.get(curr);
                    replace(name,title,text,loc.getName(),loc.getAddress(),container,picture,loc.getTitle(),loc.getText(),loc.getPicture());
                    mParamId = String.valueOf(loc.getId());
                    navContents.setText(String.valueOf(curr+1) + "/" + String.valueOf(mNumOfLocations));


                }

            }
        });

        goRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = getCurrentLoc(mParamId,mpathLocations);
                if (curr < mpathLocations.length-1)
                {
                    System.out.println(Arrays.toString(mpathLocations));
                    curr=curr+1;
                    Location loc = lokacije.get(curr);
                    replace(name,title,text,loc.getName(),loc.getAddress(),container,picture,loc.getTitle(),loc.getText(),loc.getPicture());
                    mParamId = String.valueOf(loc.getId());
                    navContents.setText(String.valueOf(curr+1) + "/" + String.valueOf(mNumOfLocations));


                }

            }
        });
        int curr = getCurrentLoc(mParamId,mpathLocations);
        Location location = lokacije.get(curr);

        text.setText(location.getText() + "\n\n" + "- " + mParamName);
        title.setText(location.getTitle());
        name.setText(location.getAddress());

        navContents.setText(String.valueOf(getCurrentLoc(mParamId,mpathLocations)+1) + "/" + String.valueOf(mNumOfLocations));


        WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        Picasso.with(container.getContext()).load(ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(location.getPicture()))
                .resize(width-40,(int)(height/2.5))
                .centerCrop()
                .into(picture);

        return myInflatedView;

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void replace (TextView name, TextView title, TextView text, String sName, String sAddress, ViewGroup container,
                         ImageView picture, String sTitle, String sText, String newImageURL )
    {
        name.setText(sAddress);
        title.setText(sTitle);
        text.setText(sText + "\n\n" + "- " + sName);

        WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        Picasso.with(container.getContext()).load(ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(newImageURL))
                .resize(width-40,(int)(height/2.5))
                .centerCrop()
                .into(picture);

    }

    public int getCurrentLoc(String id, String[] locs)
    {
        for (int i = 0; i < locs.length ; i++)
        {
            if (locs[i].equals(id))
                return i;
        }
        return -1;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
