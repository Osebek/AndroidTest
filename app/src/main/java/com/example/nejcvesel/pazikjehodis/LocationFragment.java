package com.example.nejcvesel.pazikjehodis;

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

import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.MyLocationAdapter;



/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LocationFragment extends Fragment {
    Parcelable state;
    RecyclerView recView;
    LinearLayoutManager llm;
    MyLocationAdapter locAdapter;
    int positionIndex = -1;
    int topView;


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public LocationFragment() {


    }



    @SuppressWarnings("unused")
    public static LocationFragment newInstance(int columnCount) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//    }
//    @Override
//    public void onSaveInstanceState(Bundle state) {
//        super.onSaveInstanceState(state);
//
//        state.putParcelable(LIST_STATE_KEY, layoutManager.onSaveInstanceState());
//    }
//
//    protected void onRestoreInstanceState(Bundle state) {
//        super.onRestoreInstanceState(state);
//
//        Parcelable listState = state.getParcelable(LIST_STATE_KEY);
//    }


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);





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
                locAdapter = new MyLocationAdapter(getActivity());
                BackendAPICall apiCall = new BackendAPICall();
                apiCall.getAllLocationsToAdapter(((MainActivity) getActivity()).authToken, locAdapter);

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
