package io.iqube.yugam.yugamadminapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import io.iqube.yugam.yugamadminapp.models.User;
import io.realm.Realm;

/**
 * Created by raja sudhan on 11/12/2016.
 */
public class YugamAdminApplication extends Application {
    public static String SHARED_PREFERENCE_NAME = "UserDetails";
    public static String prefEmail = "EmailId";
    public static String prefPassword = "Password";
    public static AlertDialog alertDialog;
    public static String menu;
    NetworkCheck networkCheck;

    @Override
    public void onCreate() {
        super.onCreate();

        networkCheck = new NetworkCheck();

        registerReceiver(networkCheck, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(networkCheck);
    }

    private static boolean Connected = false;

    public static boolean CheckNetwork(final Context context, final Activity activity, final String menu) {

        YugamAdminApplication.menu = menu;

        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                Connected = true;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                Connected = true;
        } else
            Connected = false;
        if (NetworkCheck.Connected || Connected) {
            return true;
        } else {

            final View v = View.inflate(context, R.layout.network_dialog, null);

            YugamAdminApplication.alertDialog = new AlertDialog.Builder(context)
                    .setView(v)
                    .setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Please connect to the internet and try again !!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .create();
            YugamAdminApplication.alertDialog.show();
            Switch wifi = v.findViewById(R.id.wifi_switch);
            Switch mobileData = v.findViewById(R.id.mobile_data_switch);
            wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    compoundButton.setChecked(b);
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(b);
                }
            });
            mobileData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    compoundButton.setChecked(b);
                    if (b) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        activity.startActivityForResult(intent, 10);
                        if (NetworkCheck.Connected) {
                            YugamAdminApplication.alertDialog.dismiss();
                        }
                    }
                }
            });
            return false;
        }
    }
    public static boolean checkLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String email = preferences.getString(prefEmail, "");
        return email != "";
    }
}
