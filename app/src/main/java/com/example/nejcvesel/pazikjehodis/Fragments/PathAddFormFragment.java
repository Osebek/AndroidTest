package com.example.nejcvesel.pazikjehodis.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class PathAddFormFragment extends Fragment implements  BackendAPICall.BackendCallback {
    private static final String ARG_LOCATION_LIST = "locationList";

    private String[] locationList;
    BackendAPICall apiCall;

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
        apiCall = new BackendAPICall(this,"");


        TextView owner = (TextView) view.findViewById(R.id.pathAddOwner);
        //TODO: return coments!
//        owner.setText(main.profile.getFirstName() + main.profile.getLastName());
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

                System.out.println("Lokacije ki jih hocm dodat: " + Arrays.toString(lokacije.toArray()));

                if (!path.getDescription().equals("") &&
                    !path.getName().equals("") &&
                    !path.getCity().equals("") &&
                     path.getDescription().length() >  120 ) {
                    apiCall.addPath(path,((MainActivity)getActivity()).userProfile.getBackendAccessToken());
                }
                else
                {
                    if (path.getDescription().length() < 120)
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Napaka");
                        alertDialog.setMessage("Opis mora vsebovati vsaj 120 znakov");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "V redu",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Napaka");
                    alertDialog.setMessage("Vnesti morate vsa polja");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "V redu",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }

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
            Toast msg = Toast.makeText(getActivity(),"Dodajanje poti uspešno",Toast.LENGTH_LONG);
            msg.show();
        }
        if (message.equals("ERROR"))
        {
            Toast msg = Toast.makeText(getActivity(),"Dodajanje ni uspelo. Napaka na strežniku",Toast.LENGTH_LONG);
            msg.show();

        }
        if (message.equals("ERROR_FAIL"))
        {
            Toast msg = Toast.makeText(getActivity(),"Dodajanje ni uspelo. Ni povezave do strežnika",Toast.LENGTH_LONG);
            msg.show();

        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame,new PathListFragment(),"PathListFragment")
                .addToBackStack("PathListFragment")
                .commit();

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
