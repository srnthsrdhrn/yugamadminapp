package io.iqube.yugam.yugamadminapp;

import android.content.Context;
import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by raja sudhan on 11/12/2016.
 */

public class ServiceGenerator {
    public static String BASE_URL="https://kctyugam.com";

    public static String API_BASE_URL=BASE_URL+"/api/v1/";

    private static Retrofit retrofit = null;

    private static String user="username";
    private static String pass="password";

    static boolean isNeeded = false;
    static HttpLoggingInterceptor interceptor = (new HttpLoggingInterceptor()).setLevel(HttpLoggingInterceptor.Level.BODY);


   private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original=chain.request();
            String credentials = user+":"+pass;
            final String basic = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            if(isNeeded) {
                Request request = original.newBuilder()
                        .header("Authorization", basic)
                        .header("verification", "123")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
            else
            {
                Request request = original.newBuilder()
                        .header("verification", "123")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        }
    });

  public static Retrofit getClient(String username , String password , Context context)
  {

      if(!YugamAdminApplication.checkLogin(context)){

          if ((username.equals(""))&&(password.equals(""))){
              isNeeded = false;
          }
          else
          {
              user = "iqube@kct.ac.in";
              pass = "02adec78885a3d7945429d3214d8c892";
              isNeeded = true;
          }
      }else{
          if ((username.equals(""))&&(password.equals(""))){
              isNeeded = false;
          }
          else
          {
              user = username;
              pass = password;
              isNeeded = true;
          }
      }

      if(retrofit==null){
          retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();
      }
      return retrofit;
  }


}
