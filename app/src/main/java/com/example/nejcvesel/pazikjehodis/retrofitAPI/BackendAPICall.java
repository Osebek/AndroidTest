package com.example.nejcvesel.pazikjehodis.retrofitAPI;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.nejcvesel.pazikjehodis.Adapters.MyLocationAdapter;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.AuthorizationInterface;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.FormUploadEditInterface;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Location;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.LocationInterface;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.Path;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.PathInterface;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.User;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.UserInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nejcvesel on 09/12/16.
 */


public class BackendAPICall {
    BackendCallback backendCallback;
    Context context;
    public BackendAPICall(){}
    public BackendAPICall(Activity activity){
        backendCallback = (BackendCallback) activity;
    }
    public BackendAPICall(BackendCallback fragment, String name){
        backendCallback = fragment;
    }
    public void getAllPaths() {
        PathInterface service =
                ServiceGenerator.createUnauthorizedService(PathInterface.class);

        Call<List<Path>> call = service.getAllPaths();
        call.enqueue(new Callback<List<Path>>() {
            @Override
            public void onResponse(Call<List<Path>> call, Response<List<Path>> response) {
                String mess = "";
                List<Path> paths;
                if(response.isSuccessful()){
                    paths = response.body();
                    mess = "OK";
                }else{
                    paths = new ArrayList<Path>();
                    mess = "ERROR";
                }
                backendCallback.getAllPathsCallback(paths, mess);
            }

            @Override
            public void onFailure(Call<List<Path>> call, Throwable t) {
                backendCallback.getAllPathsCallback(null, "ERORR_FAIL");
            }
        });
    }



    public void getAllLocations(String authToken) {
        final MyLocationAdapter myLocationAdapter;
        List<Location> locations = new ArrayList<Location>();
        LocationInterface service =
                ServiceGenerator.createUnauthorizedService(LocationInterface.class);

        Call<List<Location>> call = service.getAllLocations();
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                String mess = "";
                List<Location> locations;
                if(response.isSuccessful()) {
                     locations = response.body();
                    mess = "OK";
                }else {
                    locations = new ArrayList<Location>();
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mess = "ERROR";
                }
                backendCallback.getAllLocationsCallback(locations, mess);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("Fetching locations did not work");
                backendCallback.getAllLocationsCallback(null, "ERROR_FAIL");
            }
        });
    }


    public void getUserLocations(String authToken) {
        LocationInterface service =
                ServiceGenerator.createAuthorizedService(LocationInterface.class,authToken);
        Call<List<Location>> call = service.getUserLocations();
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                List<Location> locations;
                String mess ="";
                if(response.isSuccessful()){
                    locations = response.body();
                    mess = "OK";
                }else{
                    locations = new ArrayList<Location>();
                    mess = "ERROR";
                }
                backendCallback.getUserLocationCallback(locations, mess);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                backendCallback.getUserLocationCallback(null, "ERROR_FAIL");
            }
        });
    }

    public void getUserPaths(String authToken)
    {
        PathInterface service = ServiceGenerator.createAuthorizedService(PathInterface.class, authToken);
        Call<List<Path>> call = service.getUserPaths();

        call.enqueue(new Callback<List<Path>>() {
            @Override
            public void onResponse(Call<List<Path>> call, Response<List<Path>> response) {
                if (response.isSuccessful())
                {
                    backendCallback.getUserPathsCallback(response.body(),"OK");
                }
                else
                {
                    backendCallback.getUserPathsCallback(null,"ERROR");
                }
            }
            @Override
            public void onFailure(Call<List<Path>> call, Throwable t) {
                backendCallback.getUserPathsCallback(null,"ERROR_FAIL");
            }
        });

    }

    public void getSpecificLocation(String locationID) {
        LocationInterface service =
                ServiceGenerator.createUnauthorizedService(LocationInterface.class);

        Call<Location> call = service.getSpecificLocation(locationID);
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                String mess = "";
                Location location;
                if(response.isSuccessful()){
                    location = response.body();
                    mess = "OK";
                }else{
                    location = new Location();
                    mess = "ERROR";
                }
                backendCallback.getSpecificLocationCallback(location, mess);
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                backendCallback.getSpecificLocationCallback(null, "ERROR_FAIL");
            }
        });
    }

    public void getSpecificUser(String authToken, String userID) {
        UserInterface service =
                ServiceGenerator.createAuthorizedService(UserInterface.class, authToken);

        Call<User> call = service.getUserByID(userID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user;
                String mess = "";
                if(response.isSuccessful()){
                    user = response.body();
                    mess = "OK";
                }else{
                    user = null;
                    mess = "ERROR";
                }
                backendCallback.getSpecificUserCallback(user, mess);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                backendCallback.getSpecificUserCallback(null, "ERROR_FAIL");
            }
        });
    }

    public void getAllUsers(String authToken) {
        UserInterface service =
                ServiceGenerator.createAuthorizedService(UserInterface.class, authToken);

        Call<List<User>> call = service.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users;
                String mess ="";
                if(response.isSuccessful()){
                    users = response.body();
                    mess = "OK";
                }else{
                    users = null;
                    mess = "ERROR";
                }
                backendCallback.getAllUsersCallback(users, mess);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                backendCallback.getAllUsersCallback(null, "ERROR_FAIL");
            }
        });
    }

    public void getUserProfile(String authToken) {
        UserInterface service =
                ServiceGenerator.createAuthorizedService(UserInterface.class, authToken);
        Call<User> call = service.getCurrentUser();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user;
                String mess = "";
                if(response.isSuccessful()){
                    user = response.body();
                    mess = "OK";
                }else{
                    user = null;
                    mess = "ERROR";
                }
                backendCallback.getUserProfileCallback(user, mess);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                backendCallback.getUserProfileCallback(null, "ERROR_FAIL");
            }
        });
    }

    public static String repairURL(String pictureURL)
    {
        String[] rez = pictureURL.split("/static/");
        return "static/" + rez[2];
    }

    public void refreshToken(String backendToken)
    {
        AuthorizationInterface service = ServiceGenerator.createUnauthorizedService(AuthorizationInterface.class);
        Call<BackendToken> call = service.refreshToken(
                "refresh_token",
                ServiceGenerator.CLIENT_ID,
                ServiceGenerator.CLIENT_SECRET,
                backendToken);


        call.enqueue(new Callback<BackendToken>() {
            @Override
            public void onResponse(Call<BackendToken> call, Response<BackendToken> response) {
                BackendToken newToken;
                String mess = "";
                if (response.isSuccessful()) {
                    newToken = response.body();
                    mess = "OK";
                }else{
                    newToken = null;
                    mess = "ERROR";
                }
                backendCallback.getRefreshTokeneCallback(newToken, mess);
            }

            @Override
            public void onFailure(Call<BackendToken> call, Throwable t) {
                backendCallback.getRefreshTokeneCallback(null, "ERROR_FAIL");
            }
        });

    }

    public void addPath(final Path path, final String authToken, final SharedPreferences pref)
    {

        PathInterface service =
                ServiceGenerator.createAuthorizedService(PathInterface.class, pref.getString(authToken + "_token","null"));

        Call<Path> call = service.uploadPath(path);
        call.enqueue(new Callback<Path>() {
            @Override
            public void onResponse(Call<Path> call,
                                   Response<Path> response) {
                Log.v("Upload", "success");
                if (response.errorBody() != null)
                {
                    try {
                        String error = response.errorBody().string();
                        System.out.println(error);
                        if (error.equals("{\"detail\":\"Invalid token header. No credentials provided.\"}"))
                        {

                            AuthorizationInterface apiService = ServiceGenerator.createUnauthorizedService(AuthorizationInterface.class);
                            System.out.println("Refresh token: " + pref.getString(authToken + "_refresh",null));
                            Call<BackendToken> klic = apiService.refreshToken(
                                    "refresh_token",
                                    ServiceGenerator.CLIENT_ID,
                                    ServiceGenerator.CLIENT_SECRET,
                                    pref.getString(authToken + "_refresh","null"));

                            System.out.println("auth token: " + authToken);

                            klic.enqueue(new Callback<BackendToken>() {
                                @Override
                                public void onResponse(Call<BackendToken> nestedCall, Response<BackendToken> nestedResponse) {
                                    System.out.println("wat");

                                }

                                @Override
                                public void onFailure(Call<BackendToken> nestedCall, Throwable t) {

                                }
                            });


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onFailure(Call<Path> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });


    }

    public void convertToken(String authToken)
    {
        AuthorizationInterface auth = ServiceGenerator.createUnauthorizedService(AuthorizationInterface.class);
        Call<BackendToken> klic = auth.convertToken(
                "convert_token",
                ServiceGenerator.CLIENT_ID,
                ServiceGenerator.CLIENT_SECRET,
                "facebook",
                authToken
        );
        klic.enqueue(new Callback<BackendToken>() {
            @Override
            public void onResponse(Call<BackendToken> call, Response<BackendToken> response) {
                BackendToken token;
                String mess = "";
                if(response.isSuccessful()){
                    token = response.body();
                    mess = "OK";
                }else{
                    token = null;
                    mess = "ERROR";
                }
                backendCallback.getConvertTokenCallback(token, mess);
            }

            @Override
            public void onFailure(Call<BackendToken> call, Throwable t) {
                backendCallback.getConvertTokenCallback(null, "ERROR_FAIL");
            }
        });
    }

    public void convertTokenAndAddPath(final Path path, final String authToken, final SharedPreferences sharedPrefs)
    {
        AuthorizationInterface auth = ServiceGenerator.createUnauthorizedService(AuthorizationInterface.class);
        Call<BackendToken> klic = auth.convertToken(
                "convert_token",
                ServiceGenerator.CLIENT_ID,
                ServiceGenerator.CLIENT_SECRET,
                "facebook",
                authToken
        );

        klic.enqueue(new Callback<BackendToken>() {
            @Override
            public void onResponse(Call<BackendToken> call, Response<BackendToken> response) {
                System.out.println("bla");
                System.out.println(response.body().getRefreshToken());
                BackendToken bat = response.body();
                String at = bat.getAccessToken();
                String refresh = bat.getRefreshToken();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(authToken + "_token", at);
                editor.putString(authToken + "_refresh",refresh);
                editor.commit();

                PathInterface service =
                        ServiceGenerator.createAuthorizedService(PathInterface.class, at);

                Call<Path> klicPoti = service.uploadPath(path);
                klicPoti.enqueue(new Callback<Path>() {
                    @Override
                    public void onResponse(Call<Path> call,
                                           Response<Path> response) {
                        Log.v("Upload", "success with adding path");
                    }

                    @Override
                    public void onFailure(Call<Path> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                    }
                });


            }

            @Override
            public void onFailure(Call<BackendToken> call, Throwable t) {

            }
        });



    }

    public void uploadFile(Uri fileUri,Float latitude, Float longtitude, String name, String address, String title, String text,String authToken,Context context)
    {


        FormUploadEditInterface service =
                ServiceGenerator.createAuthorizedService(FormUploadEditInterface.class, authToken);
        String filePath = getRealPathFromURI(context,fileUri);
        System.out.println(filePath);

        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        String latitude_string = latitude.toString();
        RequestBody latitude_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), latitude_string);

        String longtitudeString = longtitude.toString();
        RequestBody longtitude_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), longtitudeString);

        RequestBody text_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), text);

        RequestBody title_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), title);

        RequestBody name_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), name);

        RequestBody address_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), address);


        Call<ResponseBody> call = service.upload(latitude_body,longtitude_body,name_body,address_body,title_body,text_body,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    backendCallback.getAddMessageCallback("OK","uploadFile");
                }
                else
                {
                    backendCallback.getAddMessageCallback("ERROR","uploadFile");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                backendCallback.getAddMessageCallback("ERROR_FAIL","uploadFile");
            }
        });


    }

    public void updateLocationWithPicture(Uri fileUri,Float latitude, Float longtitude, String name, String address, String title, String text,String authToken,Context context, String id)
    {
        FormUploadEditInterface service =
                ServiceGenerator.createAuthorizedService(FormUploadEditInterface.class, authToken);
        String filePath = getRealPathFromURI(context,fileUri);

        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        String latitude_string = latitude.toString();
        RequestBody latitude_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), latitude_string);

        String longtitudeString = longtitude.toString();
        RequestBody longtitude_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), longtitudeString);

        RequestBody text_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), text);

        RequestBody title_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), title);

        RequestBody name_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), name);

        RequestBody address_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), address);

        Call<ResponseBody> call = service.updateLocationAndChangePicture(id,latitude_body,longtitude_body,name_body,address_body,title_body,text_body,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    backendCallback.getAddMessageCallback("OK","updateLocationWithPicture");
                }
                else
                {
                    try {
                        backendCallback.getAddMessageCallback("ERROR " + response.errorBody().string(),"updateLocationWithPicture");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                backendCallback.getAddMessageCallback("ERROR_FAIL","updateLocationWithPicture");
            }
        });


    }

    public void deleteLocation(String authToken, String id)
    {
        FormUploadEditInterface service =
                ServiceGenerator.createAuthorizedService(FormUploadEditInterface.class, authToken);

        Call<ResponseBody> call = service.deleteLocation(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    backendCallback.getAddMessageCallback("OK","deleteLocation");
                }
                else
                {
                    backendCallback.getAddMessageCallback("ERROR", "deleteLocation");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                backendCallback.getAddMessageCallback("ERROR_FAIL", "deleteLocation");
            }
        });



    }

    public void updateLocation(Float latitude, Float longtitude, String name, String address, String title, String text, String authToken, String id)
    {
        FormUploadEditInterface service =
                ServiceGenerator.createAuthorizedService(FormUploadEditInterface.class, authToken);


        String latitude_string = latitude.toString();
        RequestBody latitude_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), latitude_string);

        String longtitudeString = longtitude.toString();
        RequestBody longtitude_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), longtitudeString);

        RequestBody text_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), text);

        RequestBody title_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), title);

        RequestBody name_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), name);

        RequestBody address_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), address);

        Call<ResponseBody> call = service.updateLocation(id,latitude_body,longtitude_body,name_body,address_body,title_body,text_body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    backendCallback.getAddMessageCallback("OK","updateLocation");
                }
                else
                {
                    try {
                        backendCallback.getAddMessageCallback("ERROR " + response.errorBody().string(),"updateLocation");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                backendCallback.getAddMessageCallback("ERROR_FAIL","updateLocation");
            }
        });


    }

    public void updatePath(Path path,String authToken)
    {
        PathInterface service =
                ServiceGenerator.createAuthorizedService(PathInterface.class, authToken);

        Call<ResponseBody> call = service.updatePath(path.getId().toString(),path);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    backendCallback.getAddMessageCallback("OK","updatePath");
                }
                else
                {
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    backendCallback.getAddMessageCallback("ERROR","updatePath");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                backendCallback.getAddMessageCallback("ERROR_FAIL","updatePath");

            }
        });
    }

    public void deletePath(String authToken, String id)
    {
        PathInterface service =
                ServiceGenerator.createAuthorizedService(PathInterface.class, authToken);

        Call<ResponseBody> call = service.deletePath(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    backendCallback.getAddMessageCallback("OK","deletePath");
                }
                else
                {
                    System.out.println(response.errorBody().toString());
                    backendCallback.getAddMessageCallback("ERROR","deletePath");
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                backendCallback.getAddMessageCallback("ERROR_FAIL","deletePath");

            }
        });


    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public interface BackendCallback{
        public void getAllPathsCallback(List<Path> paths, String message);
        public void getAllLocationsCallback(List<Location> loactions, String status);
        public void getSpecificLocationCallback(Location loaction, String message);
        public void getSpecificUserCallback(User user, String message);
        public void getAllUsersCallback(List<User> user, String message);
        public void getUserLocationCallback (List<Location> location, String message);
        public void getUserProfileCallback(User user, String message);
        public void getRefreshTokeneCallback(BackendToken token, String message);
        public void getConvertTokenCallback(BackendToken token, String message);
        public void getAddMessageCallback(String message, String backendCall);
        public void getUserPathsCallback(List<Path> userPaths, String message);
    }
}


