package com.example.nejcvesel.pazikjehodis.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.R;
import com.example.nejcvesel.pazikjehodis.UserProfile;
import com.example.nejcvesel.pazikjehodis.Utility.UtilityFunctions;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.BackendAPICall;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.Console;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by brani on 12/19/2016.
 */
//TODO: add check when photo added
//TODO: implement for different/lower apk versions file upload/ test as well
//TODO: get address for pin location

public class LocationFormFragment extends Fragment implements BackendAPICall.BackendCallback {
    private static final String ARG_OWNER = "owner";
    private static final String ARG_TEXT = "text";
    private static final String ARG_ID = "id";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_PICTURE_URL = "pictureUrl";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGTITUDE = "longtitude";
    private static final String ARG_NAME = "name";
    private static final String ARG_TITLE = "title";
    private static final String ARG_EDIT_MODE = "edit_mode";
    private static final int PERMISSION_REQUEST_CODE = 1;
    Uri selectedImage;

    private String picURL;

    private String mParamText;
    private String mParamTitle;
    private String mParamName;
    private String mParamPicture;
    private String mParamId;
    private String mParamLatitude;
    private String mParamLongtitude;
    private String mParamAddress;
    private String mParamOwner;
    private boolean mParamEditMode;
    private BackendAPICall apiCall;
    private UserProfile userProfile;
    private Uri url = null;
   // private CallbackManager callbackManager;


    public LocationFormFragment() {


    }

    public static LocationFormFragment newInstance(Location loc) {
        LocationFormFragment fragment = new LocationFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OWNER, loc.getOwner());
        args.putString(ARG_TEXT, loc.getText());
        args.putString(ARG_ID, String.valueOf(loc.getId()));
        args.putString(ARG_ADDRESS, loc.getAddress());
        args.putString(ARG_PICTURE_URL, loc.getPicture());
        args.putString(ARG_LATITUDE, loc.getLatitude());
        args.putString(ARG_LONGTITUDE, loc.getLongtitude());
        args.putString(ARG_NAME, loc.getName());
        args.putString(ARG_TITLE, loc.getTitle());
        System.out.println(loc.getId());
        if (loc.getId().equals(-1) || loc.getId() == null) {
            args.putBoolean(ARG_EDIT_MODE, false);
        }
        else
        {
            args.putBoolean(ARG_EDIT_MODE,true);
        }
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
            mParamPicture = getArguments().getString(ARG_PICTURE_URL);
            mParamId = getArguments().getString(ARG_ID);
            mParamAddress = getArguments().getString(ARG_ADDRESS);
            mParamOwner = getArguments().getString(ARG_OWNER);
            mParamLongtitude = getArguments().getString(ARG_LONGTITUDE);
            mParamLatitude = getArguments().getString(ARG_LATITUDE);
            mParamEditMode = getArguments().getBoolean(ARG_EDIT_MODE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_add_location,container, false);
        TextView ownerName = (TextView) myInflatedView.findViewById(R.id.input_author);
        MainActivity main = (MainActivity) getActivity();
//        main.fabClick.DisableFab();
        apiCall = new BackendAPICall(this, "");
        userProfile = ((MainActivity) getActivity()).userProfile;
        ownerName.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
        //TODO: return to upper comented code .... this is just for test!
        //ownerName.setText("");
        Button submit = (Button) myInflatedView.findViewById(R.id.input_btn_upload);

        final TextView address = (TextView) myInflatedView.findViewById(R.id.input_address);
        address.setText(mParamAddress);
        final TextView name = (TextView) myInflatedView.findViewById(R.id.input_author);
        final TextView description = (TextView) myInflatedView.findViewById(R.id.input_story);
        final TextView title = (TextView) myInflatedView.findViewById(R.id.input_loc_title);
        final TextView imageURL = (TextView) myInflatedView.findViewById(R.id.input_img_url);

        Button addImage = (Button) myInflatedView.findViewById(R.id.input_btn_add_pic);


        if (mParamEditMode) {
            System.out.println("Edit mode!");
            ((TextView) myInflatedView.findViewById(R.id.input_address)).setText(mParamAddress);
            ((TextView) myInflatedView.findViewById(R.id.input_story)).setText(mParamText);
            ((TextView) myInflatedView.findViewById(R.id.input_loc_title)).setText(mParamTitle);
            submit.setText("Posodobi");
        }

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachPic(v);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mParamEditMode) {
                    uploadLocation(name.getText().toString(), address.getText().toString(),
                            title.getText().toString(), description.getText().toString(),
                            picURL);
                }
                else
                {
                    if (imageURL.getText().toString().equals(""))
                    {
                        partialUpdate(
                                address.getText().toString(),
                                title.getText().toString(),
                                description.getText().toString(),
                                mParamId);
                    }
                    else {
                        updateLocationAndChangePicture(
                                address.getText().toString(),
                                title.getText().toString(),
                                description.getText().toString(),
                                picURL, mParamId);
                    }
                }
            }
        });

        return myInflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void uploadLocation(String name, String address, String title, String description, String imageURL) {
        Uri url = Uri.parse(imageURL);

        if (description.length() < 120) {
            CharSequence text = "Opis mora biti daljši od 120 znakov";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
        }
        else {

        if (name.equals("") || address.equals("") || title.equals("") ||
                description.equals("") || imageURL.equals("")) {
            CharSequence text = "Vnesti morate vsa polja";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();

        } else {
            Marker currMarker = ((MainActivity) getActivity()).currentMarker;
            //if (currMarker != null) {
            //  LatLng latlng = currMarker.getPosition();


            //apiCall.uploadFile(url, ((float) latlng.latitude), ((float) latlng.longitude), name,
            //      address, title, description, userProfile.getBackendAccessToken(), getActivity());
            apiCall.uploadFile(url, ((float) 46.056946), ((float) 14.505751), name,
                    address, title, description, userProfile.getBackendAccessToken(), getActivity());
        }


        }

    }

    public void updateLocationAndChangePicture(String address, String title, String description, String imageURL,String id) {
        // To be discussed if it is allowed to change the coordinates and/or the name, currently not allowed

        Uri url = Uri.parse(imageURL);
            apiCall.updateLocationWithPicture(url, Float.valueOf(mParamLatitude),
                    Float.valueOf(mParamLongtitude), mParamName, address, title, description,
                    userProfile.getBackendAccessToken(),
                    getActivity(), id);
        }


    public void partialUpdate(String address, String title, String description, String id)
    {
        // To be discussed if it is allowed to change the coordinates and/or the name, currently not allowed
        apiCall.updateLocation(
                Float.valueOf(mParamLongtitude),
                Float.valueOf(mParamLatitude),
                mParamName,
                address,
                title,
                description,
                userProfile.getBackendAccessToken(),
                id);

    }





    public void AttachPic(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                   selectedImage = imageReturnedIntent.getData();
                    System.out.println("Fotka fotka");
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    url = selectedImage;
                    picURL = selectedImage.toString();
                    if (Build.VERSION.SDK_INT >= 23)
                    {
                        if (checkPermission())
                        {
                            System.out.println("premission was granted");
                            // Code for above or equal 23 API Oriented Device
                            // Your Permission granted already .Do next code
                            String realPath = UtilityFunctions.getRealPathFromURI(getActivity().getBaseContext(), url);
                            String[] splitUrl = realPath.split("/");
                            //LocationFormFragment form = (LocationFormFragment) getFragmentManager().findFragmentByTag("LocationFormFragment");
                            TextView picText = (TextView) this.getView().findViewById(R.id.input_img_url);
                            picText.setText(selectedImage.toString());
                            Button addImageButton = (Button) this.getView().findViewById(R.id.input_btn_add_pic);
                            addImageButton.setText("Spremeni sliko");
                            picText.setText("URL slike: " + splitUrl[splitUrl.length - 1]);

                        } else {
                            requestPermission(); // Code for permission
                        }
                    }
                    else
                    {
                        String realPath = UtilityFunctions.getRealPathFromURI(getActivity().getBaseContext(), url);
                        String[] splitUrl = realPath.split("/");
                        //LocationFormFragment form = (LocationFormFragment) getFragmentManager().findFragmentByTag("LocationFormFragment");
                        TextView picText = (TextView) this.getView().findViewById(R.id.input_img_url);
                        picText.setText(selectedImage.toString());
                        Button addImageButton = (Button) this.getView().findViewById(R.id.input_btn_add_pic);
                        addImageButton.setText("Spremeni sliko");
                        picText.setText("URL slike: " + splitUrl[splitUrl.length - 1]);

                        // Code for Below 23 API Oriented Device
                        // Do next code
                    }

//                    System.out.println(selectedImage.toString());
//                    System.out.println("Do tuki rpidem");
//                    if(currentMarker != null){
//                        LatLng latlng= currentMarker.getPosition();
//                        sendRequest.uploadFile(selectedImage, ((float) latlng.latitude), ((float) latlng.longitude),
//                                "To je teksts","something","some Name",
//                                authToken,this.getApplicationContext());
//                    }
                }
                break;

        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String realPath = UtilityFunctions.getRealPathFromURI(getActivity().getBaseContext(), url);
                    String[] splitUrl = realPath.split("/");
                    //LocationFormFragment form = (LocationFormFragment) getFragmentManager().findFragmentByTag("LocationFormFragment");
                    TextView picText = (TextView) this.getView().findViewById(R.id.input_img_url);
                    picText.setText(selectedImage.toString());
                    Button addImageButton = (Button) this.getView().findViewById(R.id.input_btn_add_pic);
                    addImageButton.setText("Spremeni sliko");
                    picText.setText("URL slike: " + splitUrl[splitUrl.length - 1]);

                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
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
        CharSequence text = "Napaka";
        System.out.println(backendCall);
        System.out.println(message);

        if (backendCall.equals("uploadFile"))
        {
            if (message.equals("OK"))
            {
                text = "Dodajanje lokacije uspešno";
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, new MapsFragment(), "MapsFragment")
                        .addToBackStack("MapsFragment").commit();

            }
            else if (message.equals("ERROR"))
            {
                text = "Dodajanje lokacije ni bilo uspešno. Napaka na strežniku";
            }
            else if (message.equals("ERROR_FAIL"))
            {
                text = "Dodajanje lokacije ni bilo uspešno. Ni povezave do strežnika.";
            }
        }
        else if (backendCall.equals("updateLocation") || backendCall.equals("updateLocationWithPicture"))
        {
            if (message.equals("OK"))
            {
                text = "Spreminjanje lokacije uspešno";
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, new MyLocationsFragment(), "MyLocationsFragment")
                        .addToBackStack("MyLocationsFragment").commit();

            }
            else if (message.equals("ERROR"))
            {
                text = "Spreminjanje lokacije ni bilo uspešno. Napaka na strežniku";
            }
            else if (message.equals("ERROR_FAIL"))
            {
                text = "Spreminjanje lokacije ni bilo uspešno. Ni povezave do strežnika.";
            }
        }
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getActivity(), text, duration);
        toast.show();

    }

    @Override
    public void getUserPathsCallback(List<Path> userPaths, String message) {

    }
}
