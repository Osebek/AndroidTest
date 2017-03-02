package com.example.nejcvesel.pazikjehodis;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PathAddFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PathAddFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PathAddFormFragment extends Fragment {
    private static final String ARG_LOCATION_LIST = "locationList";

    // TODO: Rename and change types of parameters
    private String[] locationList;

    private OnFragmentInteractionListener mListener;

    public PathAddFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PathAddFormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PathAddFormFragment newInstance(String[] param1) {
        PathAddFormFragment fragment = new PathAddFormFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_LOCATION_LIST, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationList = getArguments().getStringArray(ARG_LOCATION_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =  inflater.inflate(R.layout.fragment_path_add_form, container, false);
        final MainActivity main = (MainActivity) getActivity();


        TextView owner = (TextView) view.findViewById(R.id.pathAddOwner);
        owner.setText(main.profile.getFirstName() + main.profile.getLastName());
        Button objavi = (Button) view.findViewById(R.id.upload_path);

        final EditText name = (EditText) view.findViewById(R.id.pathAddName);
        final EditText city = (EditText) view.findViewById(R.id.pathAddCity);
        final EditText description = (EditText) view.findViewById(R.id.pathAddDescription);

        objavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Path path = new Path();
                path.setDescription(description.getText().toString());
                path.setName(name.getText().toString());
                path.setCity(city.getText().toString());
                ArrayList<Integer> lokacije = new ArrayList<Integer>();
                for (int i = 0; i < locationList.length; i++)
                {
                    lokacije.add(Integer.parseInt(locationList[i]));
                }
                path.setPathLocations(lokacije);

                main.uploadPath(path);

            }
        });


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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
