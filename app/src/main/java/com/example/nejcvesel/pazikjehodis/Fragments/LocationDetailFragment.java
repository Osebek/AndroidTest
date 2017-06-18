package com.example.nejcvesel.pazikjehodis.Fragments;

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
import android.widget.TextView;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.ServiceGenerator;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TEXT = "text";
    private static final String ARG_TITLE = "title";
    private static final String ARG_NAME = "name";
    private static final String ARG_PICTURE = "picture";
    private static final String ARG_ID = "id";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_OWNER ="owner";

    private String mParamText;
    private String mParamTitle;
    private String mParamName;
    private String mParamPicture;
    private String mParamId;
    private String mParamAddress;
    private String mParamOwner;


    private OnFragmentInteractionListener mListener;

    public LocationDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param loc Parameter 1.
     * @return A new instance of fragment LocationDetail.
     */
    public static LocationDetailFragment newInstance(Location loc) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, loc.getText());
        args.putString(ARG_NAME, loc.getName());
        args.putString(ARG_TITLE,loc.getTitle());
        args.putString(ARG_PICTURE,loc.getPicture());
        args.putString(ARG_ADDRESS,loc.getAddress());
        args.putString(ARG_ID,Integer.toString(loc.getId()));
        args.putString(ARG_OWNER,loc.getOwner());
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
            mParamOwner = getArguments().getString(ARG_OWNER);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_location_detail, container, false);
        TextView text = (TextView) myInflatedView.findViewById(R.id.locationDetailText);
        TextView title = (TextView) myInflatedView.findViewById(R.id.locationDetailTitle);
        TextView name = (TextView) myInflatedView.findViewById(R.id.locationDetailName);
        ImageView picture = (ImageView) myInflatedView.findViewById(R.id.locationDetailPicture);
        ImageView loc_icon = (ImageView) myInflatedView.findViewById(R.id.location_icon);

   /*     loc_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity main = (MainActivity) getActivity();
                String loc = "[" + mParamId + "]";
                main.showLocationOnMap(v,loc);

            }
        });*/


        text.setText(mParamText + "\n\n" + "- " + mParamName);
        title.setText(mParamTitle);
        name.setText(mParamAddress);


        WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        Picasso.with(container.getContext()).load(ServiceGenerator.API_BASE_URL + BackendAPICall.repairURL(mParamPicture))
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
