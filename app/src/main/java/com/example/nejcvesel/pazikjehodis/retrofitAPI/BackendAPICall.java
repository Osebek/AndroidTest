package com.example.nejcvesel.pazikjehodis.retrofitAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.nejcvesel.pazikjehodis.MainActivity;
import com.example.nejcvesel.pazikjehodis.MyPathLocationsAdapter;
import com.example.nejcvesel.pazikjehodis.UserProfile;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.AuthorizationInterface;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;
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
    public interface UserCallback{
        public void callBack(String accessToekn, String refreshToken);
    }

    UserCallback usrCall;
    public void getAllPaths(String authToken) {

        final MyLocationAdapter myLocationAdapter;
        List<Path> paths = new ArrayList<Path>();
        PathInterface service =
                ServiceGenerator.createUnauthorizedService(PathInterface.class);

        Call<List<Path>> call = service.getAllPaths();
        call.enqueue(new Callback<List<Path>>() {
            @Override
            public void onResponse(Call<List<Path>> call, Response<List<Path>> response) {
                List<Path> paths = response.body();
                for (Path pot : paths)
                {
                    System.out.println(pot.getId());
                }
            }

            @Override
            public void onFailure(Call<List<Path>> call, Throwable t) {
                System.out.println("Fetching locations did not work");
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
                List<Location> locations = response.body();
                BackendAPICall.printLocations(locations);
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("Fetching locations did not work");
            }
        });
    }

    public void getAllLocations(String authToken,final List<Location> lokacije) {

        List<Location> locations = new ArrayList<Location>();
        LocationInterface service =
                ServiceGenerator.createUnauthorizedService(LocationInterface.class);

        Call<List<Location>> call = service.getAllLocations();
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                List<Location> locations = response.body();

            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("Fetching locations did not work");
            }
        });
    }

    public void getAllLocationsToAdapter(String authToken, final MyLocationAdapter myLocationAdapter) {
        final long startTime = System.currentTimeMillis();
        List<Location> locations = new ArrayList<Location>();
        LocationInterface service =
                ServiceGenerator.createUnauthorizedService(LocationInterface.class);

        Call<List<Location>> call = service.getAllLocations();
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                List<Location> locations = response.body();

                for (Location loc : locations)
                {
                    myLocationAdapter.addData(loc);
                }
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);

            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("Fetching locations did not work");
                Location loc = new Location();
                loc.setLatitude("46.056946");
                loc.setLongtitude("14.505751");
                loc.setTitle("Nalaganje lokacij ni uspelo");
                loc.setId(-1);
                loc.setName("Preveri internetno povezavo");
                loc.setText("Lokacije niso bile uspešno naložene");
                loc.setPicture(ServiceGenerator.API_BASE_URL + "locationGetAll/files/locations/None/logo_red.png");
                myLocationAdapter.addData(loc);
            }
        });
    }

    public void getUserLocationsToAdapter(String authToken, final MyUserLocationsAdapter myLocationAdapter) {
        LocationInterface service =
                ServiceGenerator.createAuthorizedService(LocationInterface.class,"xVMLOqEL7xoA0Q6hduAEfZduVfADNo");
        System.out.println("poklicu sem");
        Call<List<Location>> call = service.getUserLocations();
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                List<Location> locations = response.body();

                for (Location loc : locations)
                {
                    System.out.println(loc.getName());
                    System.out.println(loc.getId());
                    myLocationAdapter.addData(loc);
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("Fetching locations did not work");
                Location loc = new Location();
                loc.setLatitude("46.056946");
                loc.setLongtitude("14.505751");
                loc.setTitle("Nalaganje lokacij ni uspelo");
                loc.setId(-1);
                loc.setName("Preveri internetno povezavo");
                loc.setText("Lokacije niso bile uspešno naložene");
                loc.setPicture(ServiceGenerator.API_BASE_URL + "locationGetAll/files/locations/None/logo_red.png");
                myLocationAdapter.addData(loc);
            }
        });
    }

    public void getAllAddPathLocationsToAdapter(String authToken, final MyPathAddAdapter myLocationAdapter) {

        List<Location> locations = new ArrayList<Location>();
        LocationInterface service =
                ServiceGenerator.createUnauthorizedService(LocationInterface.class);

        Call<List<Location>> call = service.getAllLocations();
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                List<Location> locations = response.body();

                for (Location loc : locations)
                {
                    myLocationAdapter.addData(loc);
                }
                myLocationAdapter.getFilter().filter("");

            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                System.out.println("Fetching locations did not work");
                Location loc = new Location();
                loc.setLatitude("46.056946");
                loc.setLongtitude("14.505751");
                loc.setTitle("Nalaganje lokacij ni uspelo");
                loc.setId(-1);
                loc.setName("Preveri internetno povezavo");
                loc.setText("Lokacije niso bile uspešno naložene");
                loc.setPicture(ServiceGenerator.API_BASE_URL + "locationGetAll/files/locations/None/logo_red.png");
                myLocationAdapter.addData(loc);
            }
        });

        //myLocationAdapter.getFilter().filter("");
    }

    public void getAllPathsToAdapter(final String authToken, final MyPathAdapter myPathAdapter) {

        PathInterface service =
                ServiceGenerator.createUnauthorizedService(PathInterface.class);

        Call<List<Path>> call = service.getAllPaths();
        call.enqueue(new Callback<List<Path>>() {
            @Override
            public void onResponse(Call<List<Path>> call, Response<List<Path>> response) {
                List<Path> paths= response.body();

                for (Path pth : paths)
                {
                    myPathAdapter.addData(pth);
                }
            }

            @Override
            public void onFailure(Call<List<Path>> call, Throwable t) {
                Path path = new Path();
                path.setName("Nalaganje poti ni uspelo");
                path.setOwner("Prosimo preverite vašo internetno povezavo ali puskisite ponovno");
                path.setCity("Napaka");
                path.setDescription("Napaka");
                ArrayList<Integer> al = new ArrayList<Integer>();
                path.setPathLocations(al);
                path.setId(-1);
                myPathAdapter.addData(path);
                System.out.println("Fetching locations did not work");
            }
        });
    }

    public void getSpecificLocation(String authToken, String locationID) {
        LocationInterface service =
                ServiceGenerator.createUnauthorizedService(LocationInterface.class);

        Call<Location> call = service.getSpecificLocation(locationID);
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Location location = response.body();
                System.out.println(location.getPicture());
                System.out.println(location.getText());
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                System.out.println("Fetching locations did not work");
            }
        });
    }

    public void getSpecificLocationToAdapter(String authToken, String locationID, final MyLocationAdapter myLocationAdapter) {
        LocationInterface service =
                ServiceGenerator.createUnauthorizedService(LocationInterface.class);

        Call<Location> call = service.getSpecificLocation(locationID);
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Location location = response.body();
                myLocationAdapter.addData(location);
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                System.out.println("Fetching locations did not work");
            }
        });
    }



    public void getSpecificLocationToExtendedAdapter(String authToken, String locationID, final MyPathLocationsAdapter myLocationAdapter) {
        LocationInterface service =
                ServiceGenerator.createUnauthorizedService(LocationInterface.class);

        Call<Location> call = service.getSpecificLocation(locationID);
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Location location = response.body();
                myLocationAdapter.addData(location);
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                System.out.println("Fetching locations did not work");
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
                User user = response.body();
                System.out.println(user.getId());
                System.out.println(user.getUsername());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Fetching locations did not work");
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
                List<User> users = response.body();
                System.out.println(users.size());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Fetching locations did not work");
            }
        });
    }

    public void getUserProfile(String authToken) {
        authToken = "gB1OIviNZY7W8tIn1Ar8GCoiLH7gnW";

        UserInterface service =
                ServiceGenerator.createAuthorizedService(UserInterface.class, authToken);

        System.out.println("Auth token:" + authToken);

        Call<User> call = service.getCurrentUser();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                System.out.println(user.getId());
                System.out.println(user.getUsername());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Fetching locations did not work");
            }
        });
    }

    public static String repairURL(String pictureURL)
    {
        String[] rez = pictureURL.split("/static/");
        return "static/" + rez[2];
    }


    public static void printLocations(List<Location> locations)
    {
        for (Location item : locations) {
            System.out.println("****************************");
            System.out.println(item.getId());
            System.out.println(item.getCreated());
            System.out.println(item.getOwner());
            System.out.println(item.getLatitude());
            System.out.println(item.getLongtitude());
            System.out.println(item.getPicture());
            System.out.println(item.getName());
            System.out.println(item.getTitle());
            System.out.println(repairURL(item.getPicture()));
            System.out.println("****************************");

        }

    }

    public void refreshToken(final String authToken, final SharedPreferences pref, final UserProfile profile)
    {
        AuthorizationInterface service = ServiceGenerator.createUnauthorizedService(AuthorizationInterface.class);
        Call<BackendToken> call = service.refreshToken(
                "refresh_token",
                ServiceGenerator.CLIENT_ID,
                ServiceGenerator.CLIENT_SECRET,
                pref.getString(authToken + "_refresh","null"));


        call.enqueue(new Callback<BackendToken>() {
            @Override
            public void onResponse(Call<BackendToken> call, Response<BackendToken> response) {
                BackendToken newToken = response.body();
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(authToken + "_token", newToken.getAccessToken());
                    editor.putString(authToken + "_refresh", newToken.getRefreshToken());
                    editor.commit();
                    profile.setRefreshToken(newToken.getRefreshToken());
                    profile.setBackendAccessToken(newToken.getAccessToken());
                    usrCall.callBack(newToken.getAccessToken(),newToken.getRefreshToken());
                }
            }

            @Override
            public void onFailure(Call<BackendToken> call, Throwable t) {

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

    public static void printUsers(List<User> users)
    {
        for (User item : users) {
            System.out.println("****************************");
            System.out.println(item.getId());
            System.out.println(item.getUsername());
            System.out.println("****************************");

        }

    }


    public void uploadFile(Uri fileUri,Float latitude, Float longtitude, String name, String address, String title, String text,String authToken,Context context)
    {
        System.out.println(authToken);
        FileUploadService service =
                ServiceGenerator.createAuthorizedService(FileUploadService.class, authToken);
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
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
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
}


