package com.example.nejcvesel.pazikjehodis.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;
import com.example.nejcvesel.pazikjehodis.Adapters.MyPathAddAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
//TODO: search bar to look nice
public class PathAddFragment extends Fragment implements BackendAPICall.BackendCallback, MyPathAddAdapter.PathAddCallback {
    Parcelable state;
    RecyclerView recView;
    LinearLayoutManager llm;
    MyPathAddAdapter locAdapter;
    int positionIndex = -1;
    int topView;
    private BackendAPICall apiCall;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String EDIT_MODE = "edit-mode";
    private static final String CHECKED_LOCATIONS = "checked_locations";


    private int mColumnCount = 1;
    private boolean mEditMode = false;
    private ArrayList<Integer> mCheckedLocations;
    private OnListFragmentInteractionListener mListener;

    public PathAddFragment() {


    }


    @SuppressWarnings("unused")
    public static PathAddFragment newInstance(int columnCount) {
        PathAddFragment fragment = new PathAddFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static PathAddFragment newInstance(ArrayList<Integer> checkLocations)
    {
        PathAddFragment fragment = new PathAddFragment();
        Bundle args = new Bundle();
        args.putBoolean(EDIT_MODE,true);
        args.putIntegerArrayList(CHECKED_LOCATIONS,checkLocations);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckedLocations = new ArrayList<>();
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_COLUMN_COUNT)) {
                mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            } else if (getArguments().containsKey(EDIT_MODE) && getArguments().containsKey(CHECKED_LOCATIONS)) {
                mEditMode = getArguments().getBoolean(EDIT_MODE);
                mCheckedLocations = getArguments().getIntegerArrayList(CHECKED_LOCATIONS);
            }
            else
            {

            }
        }
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
        View view1 = inflater.inflate(R.layout.fragment_pathadd_list, container, false);
        RecyclerView view = (RecyclerView) view1.findViewById(R.id.recyclerViewList);
        TextView makePath = (TextView) view1.findViewById(R.id.makePath);
        final EditText search = (EditText) view1.findViewById(R.id.search);

        if(mEditMode)
        {
            makePath.setText("Shrani nove lokacije");

        }
        else
        {
            makePath.setText("Sestavi novo pot");
        }

        apiCall = new BackendAPICall(this, "");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                locAdapter.getFilter().filter(search.getText().toString());

            }
        });

        makePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Collection<String> paths = main.locationsToAddToPath.values();
                if (mEditMode)
                {
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();

                }
                else {
                    if (mCheckedLocations.size() > 1) {
                        //String[] foos = mCheckedLocations.toArray(new String[mCheckedLocations.size()]);
                        String[] foos = Arrays.toString(mCheckedLocations.toArray()).split("[\\[\\]]")[1].split(", ");
                        System.out.println(Arrays.toString(foos));
                        Fragment fragment = PathAddFormFragment.newInstance(foos);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction =
                                fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment, "PathAddFormFragment");
                        fragmentTransaction.addToBackStack("PathAddFormFragment");
                        fragmentTransaction.commit();
                    } else {
                        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                        alert.setTitle("Napaka");
                        alert.setMessage("Izbrati morate vsaj dve lokaciji");
                        alert.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "V redu",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alert.show();


                    }
                }





            }
        });


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
                if (mEditMode) {
                    locAdapter = new MyPathAddAdapter(getActivity(), mCheckedLocations);
                    locAdapter.attachCallback(this);
                }
                else
                {
                    locAdapter = new MyPathAddAdapter(getActivity(),new ArrayList<Integer>());
                    locAdapter.attachCallback(this);
                }

                apiCall.getAllLocations("");
            }

            recyclerView.setAdapter(locAdapter);
        }
        System.out.println(positionIndex);

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
    public void getAllLocationsCallback(List<Location> loactions, String message) {
        if(message.equals("OK")){
            for (Location loc : loactions)
            {
                locAdapter.addData(loc);
            }
            locAdapter.getFilter().filter("");
        }
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

    }

    @Override
    public void checkLocationCallback(int id) {
            mCheckedLocations.add(id);

            System.out.println("Dodal. Trenutno stanje" + Arrays.toString(mCheckedLocations.toArray()));


    }

    @Override
    public void uncheckLocationCallback(int id) {
            mCheckedLocations.remove((Integer) id);
            System.out.println("Zbrisal. Trenutno stanje" + Arrays.toString(mCheckedLocations.toArray()));


    }

    @Override
    public void allLocationsCallback(int id) {

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
