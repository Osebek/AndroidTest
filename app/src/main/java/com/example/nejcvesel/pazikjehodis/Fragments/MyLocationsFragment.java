package com.example.nejcvesel.pazikjehodis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;
import com.example.nejcvesel.pazikjehodis.Adapters.MyUserLocationsAdapter;

import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyLocationsFragment extends Fragment implements BackendAPICall.BackendCallback{
    Parcelable state;
    RecyclerView recView;
    LinearLayoutManager llm;
    MyUserLocationsAdapter locAdapter;
    int positionIndex = -1;
    int topView;
    private BackendAPICall apiCall;


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public MyLocationsFragment() {


    }
    
    @SuppressWarnings("unused")
    public static MyLocationsFragment newInstance(int columnCount) {
        MyLocationsFragment fragment = new MyLocationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onPause()
    {
        positionIndex= llm.findFirstVisibleItemPosition();
        View startView = recView.getChildAt(0);
        topView = (startView == null) ? 0 : (startView.getTop() - recView.getPaddingTop());
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (positionIndex!= -1) {
            llm.scrollToPositionWithOffset(positionIndex, topView);
        }

    }










    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_location_list, container, false);
        apiCall = new BackendAPICall(this, "");
        locAdapter = new MyUserLocationsAdapter(getActivity());
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
                apiCall.getUserLocations(((MainActivity) getActivity()).userProfile.getBackendAccessToken());
            }
            recyclerView.setAdapter(locAdapter);
        }

        if (positionIndex!= -1) {
            llm.scrollToPositionWithOffset(positionIndex, topView);
        }

        return view;
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
        if(message.equals("OK")){
            for(Location loc: location){
                locAdapter.addData(loc);
            }
        }
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
