package com.example.nejcvesel.pazikjehodis;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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

import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.MyPathAddAdapter;

import java.util.Collection;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
//TODO: search bar to look nice
//TODO: check if at least one path is selected
public class PathAddFragment extends Fragment {
    Parcelable state;
    RecyclerView recView;
    LinearLayoutManager llm;
    MyPathAddAdapter locAdapter;
    int positionIndex = -1;
    int topView;

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
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
        View view1 = inflater.inflate(R.layout.fragment_pathadd_list, container, false);
        RecyclerView view = (RecyclerView) view1.findViewById(R.id.recyclerViewList);

        TextView makePath = (TextView) view1.findViewById(R.id.makePath);
        final EditText search = (EditText) view1.findViewById(R.id.search);


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

        final FragmentManager fm = getFragmentManager();


        makePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity) getActivity();
                Collection<String> paths = main.locationsToAddToPath.values();
                String[] foos = paths.toArray(new String[paths.size()]);


                Fragment fragment = PathAddFormFragment.newInstance(foos);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,fragment,"PathAddFormFragment");
                fragmentTransaction.addToBackStack("PathAddFormFragment");
                fragmentTransaction.commit();




            }
        });


        if (view instanceof RecyclerView) {
            System.out.println("True");
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
                locAdapter = new MyPathAddAdapter(getActivity());
                BackendAPICall apiCall = new BackendAPICall();
                apiCall.getAllAddPathLocationsToAdapter(((MainActivity) getActivity()).authToken, locAdapter);

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
