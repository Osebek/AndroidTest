package com.example.nejcvesel.pazikjehodis.retrofitAPI;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.AuthorizationInterface;
import com.example.nejcvesel.pazikjehodis.retrofitAPI.Models.BackendToken;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nejcvesel on 06/12/16.
 */

public class FileUpload {

    public void uploadFile(Uri fileUri,Float latitude, Float longtitude, String name, String address, String title, String text,String authToken,Context context)
    {


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

    public void convertTokenAndUploadFile(Uri fileUri,Float latitude, Float longtitude, String name, String address, String title, String text,final String authToken,Context context, final SharedPreferences sharedPref)
    {

        AuthorizationInterface auth = ServiceGenerator.createUnauthorizedService(AuthorizationInterface.class);
        Call<BackendToken> klic = auth.convertToken(
                "convert_token",
                ServiceGenerator.CLIENT_ID,
                ServiceGenerator.CLIENT_SECRET,
                "facebook",
                authToken
        );



        String filePath = getRealPathFromURI(context,fileUri);

        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        final String latitude_string = latitude.toString();
        final RequestBody latitude_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), latitude_string);

        final String longtitudeString = longtitude.toString();
        final RequestBody longtitude_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), longtitudeString);

        final RequestBody text_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), text);

        final RequestBody title_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), title);

        final RequestBody name_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), name);

        final  RequestBody address_body =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), address);

        klic.enqueue(new Callback<BackendToken>() {
            @Override
            public void onResponse(Call<BackendToken> call, Response<BackendToken> response) {
                System.out.println(response.body().getRefreshToken());
                BackendToken bat = response.body();
                String at = bat.getAccessToken();
                String refresh = bat.getRefreshToken();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(authToken + "_token", at);
                editor.putString(authToken + "_refresh",refresh);
                editor.commit();

                final FileUploadService service =
                        ServiceGenerator.createAuthorizedService(FileUploadService.class, at);


                Call<ResponseBody> poklici = service.upload(latitude_body,longtitude_body,name_body,address_body,title_body,text_body,body);
                poklici.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> poklici,
                                           Response<ResponseBody> odgovor) {
                        Log.v("Upload", "success");


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                    }
                });




            }

            @Override
            public void onFailure(Call<BackendToken> call, Throwable t) {

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
