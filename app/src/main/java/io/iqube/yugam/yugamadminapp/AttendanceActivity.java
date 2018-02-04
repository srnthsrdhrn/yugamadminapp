package io.iqube.yugam.yugamadminapp;

import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.iqube.yugam.yugamadminapp.models.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {
    int particular;
    int id;
    Realm realm;
    TextView resulttv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Realm.init(this);
        resulttv = findViewById(R.id.result);
        realm = Realm.getInstance(new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build());
        if (getIntent() != null) {
            id = getIntent().getExtras().getInt("id");
            particular = getIntent().getExtras().getInt("particular");
        } else {
            Toast.makeText(this, "No Intent", Toast.LENGTH_SHORT).show();
        }

    }

    public void enteredClick(View v) {
        EditText editText = findViewById(R.id.yugam_id);
        String yugamID = editText.getText().toString();
        if (!yugamID.equals("")) {
            postAttendanceCall(Integer.parseInt(yugamID.replace("YUG18", "")));
        }else{
            Toast.makeText(AttendanceActivity.this,"Enter a Valid Yugam ID",Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View v) {
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
                if (contents.contains("YUG18")) {
                    try {
                        postAttendanceCall(Integer.parseInt(contents.replace("YUG18", "")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(AttendanceActivity.this, "Not an Yugam ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    resulttv.setText("Please Scan Only Qr Codes with Yugam ID");

                }
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void postAttendanceCall(int yugamID) {
        User user = realm.where(User.class).findFirst();
        int adminID = user.getYugamid();
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", String.valueOf(yugamID));
        data.put("admin_id", String.valueOf(adminID));
        resulttv.setText("");
        if (particular == 0) {
            data.put("event_id", String.valueOf(id));
        } else {
            data.put("workshop_id", String.valueOf(id));
        }
        API_interface service = ServiceGenerator.getClient("", "", getApplicationContext()).create(API_interface.class);
        final Call<JsonObject> result = service.postAttendance(data);
        result.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    String message = response.body().get("message").getAsString();
                    resulttv.setText(message);

                } else {
                    resulttv.setText("Error Try Again");

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                resulttv.setText("Network Error Try Again");
            }
        });

    }
}
