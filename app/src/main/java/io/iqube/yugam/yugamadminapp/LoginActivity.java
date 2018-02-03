package io.iqube.yugam.yugamadminapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import io.iqube.yugam.yugamadminapp.models.User;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button Login;
    String email, password;
    EditText emailet, passwordet;
    boolean Connected = false;
    boolean flag = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailet = findViewById(R.id.email);
        passwordet = findViewById(R.id.password);
        Login = findViewById(R.id.login);
        Login.setText("Login");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new ChangeBounds());
            getWindow().setSharedElementExitTransition(new ChangeBounds());
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                Connected = true;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                Connected = true;
        } else
            Connected = false;

        Login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         if (YugamAdminApplication.CheckNetwork(LoginActivity.this, LoginActivity.this, "login")) {
                                             checkLogin();
                                         }
                                     }
                                 }
        );

    }


    @Override
    public void onBackPressed() {
        if (flag) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press Again to Exit App", Toast.LENGTH_SHORT).show();
            flag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag = false;
                }
            }, 3000);
        }
    }

    public void checkLogin() {

        API_interface service = ServiceGenerator.getClient("", "", getApplicationContext()).create(API_interface.class);
        email = emailet.getText().toString();
        password = passwordet.getText().toString();
        if (email.trim().length() > 0 && password.trim().length() > 0) {
            if (!email.equals("iqube@kct.ac.in")) {
                Login.setText("Logging In...");
                HashMap<String, String> credentials = new HashMap<>();
                credentials.put("username", email);
                credentials.put("password", password);
                Call<User> data = service.userLogin(credentials);
                data.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            ((YugamAdminApplication) getApplication()).saveUser(email, password, LoginActivity.this);

                            startActivity(new Intent(LoginActivity.this, AttendanceActivity.class).putExtra("isLoggedIn", true));
                            finish();
                        } else {
                            Login.setText("Login");
                            Toast.makeText(LoginActivity.this, "Username/Password is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Login.setText("Login");
                        emailet.setText(email);
                        passwordet.setText(password);
                        Toast.makeText(LoginActivity.this, "Sorry !! check your internet connection or Try Again", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "This email id is not valid", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Please Enter Email Id and Password", Toast.LENGTH_SHORT).show();
        }
    }
}
