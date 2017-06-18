package com.example.nejcvesel.pazikjehodis.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nejcvesel.pazikjehodis.Adapters.MyPathAddAdapter;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PathAddFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PathAddFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class EditPathFragment extends Fragment implements BackendAPICall.BackendCallback{
    private static final String ARG_LOCATION_LIST = "locationList";
    private static final String ARG_DESCRIPTION =  "pathDescription";
    private static final String ARG_NAME = "pathName";
    private static final String ARG_CITY = "pathCity";
    private static final String ARG_ID ="pathID";
    private static final String ARG_OWNER = "pathOwner";
    public static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;


    private ArrayList<Integer> mLocationList;
    private String mPathDescription;
    private String mPathName;
    private String mPathCity;
    private String mPathID;
    private String mPathOWner;
    BackendAPICall apiCall;

    private OnFragmentInteractionListener mListener;
    public EditPathFragment() {
        // Required empty public constructor
    }


    public static EditPathFragment newInstance(Path path) {
        EditPathFragment fragment = new EditPathFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_LOCATION_LIST, (ArrayList<Integer>)path.getPathLocations());
        args.putString(ARG_DESCRIPTION,path.getDescription());
        args.putString(ARG_NAME,path.getName());
        args.putString(ARG_CITY,path.getCity());
        args.putString(ARG_ID,path.getId().toString());
        args.putString(ARG_OWNER,path.getOwner());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocationList = getArguments().getIntegerArrayList(ARG_LOCATION_LIST);
            mPathCity = getArguments().getString(ARG_CITY);
            mPathDescription = getArguments().getString(ARG_DESCRIPTION);
            mPathID = getArguments().getString(ARG_ID);
            mPathName = getArguments().getString(ARG_NAME);
            mPathOWner = getArguments().getString(ARG_OWNER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_path, container, false);
        final MainActivity main = (MainActivity) getActivity();
        apiCall = new BackendAPICall(this, "");
        final TextView name = (TextView) view.findViewById(R.id.edit_path_title);
        final TextView description = (TextView) view.findViewById(R.id.edit_path_description);
        final TextView city = (TextView) view.findViewById(R.id.edit_path_city);
        TextView owner = (TextView) view.findViewById(R.id.edit_path_author);
        Button editLocation = (Button) view.findViewById(R.id.edit_path_edit_locations_btn);
        Button editPathSubmit = (Button) view.findViewById(R.id.edit_path_save_btn);

        name.setText(mPathName);
        description.setText(mPathDescription);
        city.setText(mPathCity);
        owner.setText(mPathOWner);

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                Fragment editPathLocationsFragment = PathAddFragment.newInstance(mLocationList);
                fm.beginTransaction()
                        .replace(R.id.content_frame,editPathLocationsFragment,"PathAddFragment")
                        .addToBackStack("PathAddFragment")
                        .commit();
            }
        });

        editPathSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("On click so tele not: " + Arrays.toString(mLocationList.toArray()));
                Path path = new Path();
                path.setPathLocations(mLocationList);
                path.setId(Integer.valueOf(mPathID));
                path.setDescription(description.getText().toString());
                path.setName(name.getText().toString());
                path.setCity(city.getText().toString());
                apiCall.updatePath(path,((MainActivity)getActivity()).userProfile.getBackendAccessToken());
            }
        }
        );



        return view;



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

    @Override
    public void getAllPathsCallback(List<Path> paths, String message) {

    }

    @Override
    public void getAllLocationsCallback(List<Location> loactions, String status) {

    }

    @Override
    public void getSpecificLocationCallback(Location loaction, String message) {

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
        FragmentManager fragmentManager = getFragmentManager();
        if (message.equals("OK"))
        {
            Toast msg = Toast.makeText(getActivity(),"Spreminjanje poti uspešno",Toast.LENGTH_LONG);
            msg.show();
        }
        if (message.equals("ERROR"))
        {
            Toast msg = Toast.makeText(getActivity(),"Spreminjanje poti ni uspelo. Napaka na strežniku",Toast.LENGTH_LONG);
            msg.show();

        }
        if (message.equals("ERROR_FAIL"))
        {
            Toast msg = Toast.makeText(getActivity(),"Spreminjanje ni uspelo. Ni povezave do strežnika",Toast.LENGTH_LONG);
            msg.show();

        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame,new PathListFragment(),"MyPathListFragment")
                .addToBackStack("MyPathListFragment")
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        System.out.println("onResultINside");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("permission granted");

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void getUserPathsCallback(List<Path> userPaths, String message) {

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
