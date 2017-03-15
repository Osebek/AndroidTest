package com.example.nejcvesel.pazikjehodis;

/**
 * Created by nejcvesel on 15/02/17.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.MyLocationAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PathLocationsFragment extends Fragment implements BackendAPICall.BackendCallback {
    Parcelable state;
    RecyclerView recView;
    LinearLayoutManager llm;
    MyLocationAdapter locAdapter;
    int positionIndex = -1;
    int topView;
    MyPathLocationsAdapter specialAdapter;


    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LOCATIONS = "locations";
    private static final String ARG_OWNER = "owner";
    private static final String ARG_CITY = "city";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_NAME ="name";

    private int mColumnCount = 1;
    private String[] locIDs;
    private String owner;
    private String title;
    private String city;
    private String description;
    private String name;
    private OnListFragmentInteractionListener mListener;
    private BackendAPICall apiCall;

    public PathLocationsFragment() {


    }


    public static PathLocationsFragment newInstance(int columnCount,String[] pathIDs,
                                                    String owner, String city, String name, String description) {
        PathLocationsFragment fragment = new PathLocationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putStringArray(ARG_LOCATIONS,pathIDs);
        args.putString(ARG_OWNER, owner);
        args.putString(ARG_CITY,city);
        args.putString(ARG_NAME,name);
        args.putString(ARG_DESCRIPTION,description);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onPause()
    {
        positionIndex= llm.findFirstVisibleItemPosition();
        View startView = recView.getChildAt(0);
        topView = (startView == null) ? 0 : (startView.getTop() - recView.getPaddingTop());
        System.out.println("PAUSE");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        System.out.println("RESUME");
        super.onResume();

        if (positionIndex!= -1) {
            llm.scrollToPositionWithOffset(positionIndex, topView);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            locIDs = getArguments().getStringArray(ARG_LOCATIONS);
            owner = getArguments().getString(ARG_OWNER);
            city = getArguments().getString(ARG_CITY);
            description = getArguments().getString(ARG_DESCRIPTION);
            name = getArguments().getString(ARG_NAME);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_path_locations, container, false);
        View view = view1.findViewById(R.id.recyclerViewList);
        apiCall = new BackendAPICall(this, "");
        specialAdapter = new MyPathLocationsAdapter(getActivity());



        // Set the adapter
        if (view instanceof RecyclerView) {

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            this.recView = recyclerView;

            if (mColumnCount <= 1) {
                LinearLayoutManager llm = new LinearLayoutManager(context);
                this.llm = llm;
                recyclerView.setLayoutManager(llm);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if (positionIndex == -1) {
//                locAdapter = new MyLocationAdapter(getActivity());


                Path path = new Path();
                path.setName(name);
                path.setCity(city);
                path.setDescription(description);
                path.setOwner(owner);
                ArrayList<Integer> lokacije = new ArrayList<Integer>();
                for (int i = 0; i < locIDs.length; i++)
                {
                    lokacije.add(Integer.parseInt(locIDs[i]));

                }
                path.setPathLocations(lokacije);
                specialAdapter.addData(path);
                System.out.println("Item count:" );
//                BackendAPICall apiCall = new BackendAPICall(getActivity());

                for (int i = 0; i < locIDs.length ; i++)
                {
                    apiCall.getSpecificLocation(locIDs[i]);
                //apiCall.getSpecificLocationToAdapter(((MainActivity) getActivity()).authToken,locIDs[i],locAdapter);
                }
            }
            recyclerView.setAdapter(specialAdapter);


        }

        if (positionIndex!= -1) {
            llm.scrollToPositionWithOffset(positionIndex, topView);
        }

        return view1;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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

    }

    @Override
    public void getSpecificLocationCallback(Location loaction, String message) {
        if(message.equals("OK")){
            specialAdapter.addData(loaction);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Location item);
    }
}

