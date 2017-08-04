package graduateproject.com.twentyquestions.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;


public class MainActivity extends BaseActivity {

    private static double longitude;
    private static double latitude;
    public static Context mContext;

    private LocationListener locationListener;
    private LocationManager locationManager;

    public MainActivity() {

        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataSync.getInstance().Timer();
        getLocation();

        final DBSI db = new DBSI(mContext, "TwentyQuestions.db", null, 1);

        db.insertUserInfo();

        Intent intent = new Intent(getApplicationContext(), SubActivity.class);
        startActivity(intent);

    }

    public void getLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled || isNetworkEnabled) {
            Log.e("GPS Enable", "true");

            final List<String> m_lstProviders = locationManager.getProviders(false);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.e("onLocationChanged", "onLocationChanged");
                    Log.e("location", "[" + location.getProvider() + "] (" + location.getLatitude() + "," + location.getLongitude() + ")");
//                    locationManager.removeUpdates(locationListener);

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    Snackbar.make(getWindow().getDecorView().getRootView(), "경도 : " + MainActivity.getLongitude() + "\n 위도 : " + MainActivity.getLatitude(), Snackbar.LENGTH_LONG).setAction("알림", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {



                        }
                    }).show();


                    Toast.makeText(mContext, "경도 : " + location.getLongitude() + "\n 위도 : " + location.getLatitude(), Toast.LENGTH_LONG);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e("onStatusChanged", "onStatusChanged");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e("onProviderEnabled", "onProviderEnabled");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e("onProviderDisabled", "onProviderDisabled");
                }
            };

            // QQQ: 시간, 거리를 0 으로 설정하면 가급적 자주 위치 정보가 갱신되지만 베터리 소모가 많을 수 있다.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for (String name : m_lstProviders) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        }
                        locationManager.requestLocationUpdates(name, 1, 0, locationListener);
                    }
                    Log.d("test", "location");

                }
            });

        } else {
            Log.e("GPS Enable", "false");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "GPS가 꺼져있습니다. GPS를 켜주시기발바니다.", Snackbar.LENGTH_INDEFINITE).setAction("GPS켜기", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
                }
            });
        }
    }

    public static double getLongitude() {
        return longitude;
    }

    public static double getLatitude() {
        return latitude;
    }
}
