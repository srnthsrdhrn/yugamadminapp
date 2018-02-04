package io.iqube.yugam.yugamadminapp;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import io.iqube.yugam.yugamadminapp.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by raja sudhan on 11/12/2016.
 */

public interface API_interface {
    @POST("login/")
    Call<User> userLogin(@Body HashMap<String, String> values);

    @POST("post_attendance/")
    Call<JsonObject> postAttendance(@Body HashMap<String,String> values);

}
