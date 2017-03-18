package com.example.nejcvesel.pazikjehodis.Fragments;

/**
 * Created by nejcvesel on 17/03/17.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nejcvesel.pazikjehodis.Adapters.MyPathAdapter;
import com.example.nejcvesel.pazikjehodis.Adapters.MyUserPathsAdapter;
import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;

import java.util.List;


/**
 * Created by nejcvesel on 13/02/17.
 */


public class MyPathListFragment extends Fragment implements BackendAPICall.BackendCallback{

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private LocationFragment.OnListFragmentInteractionListener mListener;
    private BackendAPICall apiCall;
    private MyUserPathsAdapter adapter;

    public MyPathListFragment() {


    }


    @SuppressWarnings("unused")
    public static MyPathListFragment newInstance(int columnCount) {
        MyPathListFragment fragment = new MyPathListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_path_list, container, false);

        RecyclerView recyclerView;
        apiCall = new BackendAPICall(this, "");
        adapter = new MyUserPathsAdapter(getActivity());
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

//            final MyPathAdapter adapter = new MyPathAdapter(getActivity());
//            BackendAPICall apiCall = new BackendAPICall();
            apiCall.getUserPaths(((MainActivity)getActivity()).userProfile.getBackendAccessToken());
            recyclerView.setAdapter(adapter);

        }

        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LocationFragment.OnListFragmentInteractionListener) {
            mListener = (LocationFragment.OnListFragmentInteractionListener) context;
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
    public void getUserPathsCallback(List<Path> userPaths, String message) {
        if (message.equals("OK"))
        {
            for (Path path : userPaths)
            {
                adapter.addData(path);
            }
        }
        else if (message.equals("ERROR"))
        {
            CharSequence tekst = "Nalaganje poti ni uspelo. Napaka na strežniku";
            Toast msg = Toast.makeText(getActivity(),tekst,Toast.LENGTH_LONG);
            msg.show();

        }
        else if (message.equals("ERROR_FAIL"))
        {
            CharSequence tekst = "Nalaganje poti ni uspelo. Ni povezave do strežnika";
            Toast error_fail = Toast.makeText(getActivity(),tekst,Toast.LENGTH_LONG);
            error_fail.show();
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
        void onListFragmentInteraction(Path item);
    }

}
