package com.example.nejcvesel.pazikjehodis.retrofitAPI;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nejcvesel on 06/12/16.
 */

public class ServiceGenerator {
    public static final String CLIENT_ID = "RYFCDG354uvYApozGririaCtqshA2bPS40SO4qof";
    public static String CLIENT_SECRET = "Mg8xZwcTcVsn3lZ3JofSigTo0LcpDXNXbsTpxY2dRqpWDhCvfrOuqqpvvJRqVAjzALZdh4zTnRSYKCdqfUTSUJSTQFzwx7ito5cOETcQL7ihTjxDxrtLRv8oyYEN5zz3";

    public static final String API_BASE_URL = "http://gis.fri.uni-lj.si/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createAuthorizedService(Class<S> serviceClass, final String token) {
        if (token != null) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization",
                                    "Bearer" + " " +  token)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createUnauthorizedService(Class<S> serviceClass) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

}
