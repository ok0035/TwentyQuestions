package graduateproject.com.twentyquestions.util;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static graduateproject.com.twentyquestions.activity.BaseActivity.mContext;

/**
 * Created by mapl0 on 2017-08-21.
 */

public class GPSTracer {

    private LocationListener locationListener;
    private android.location.LocationManager locationManager;

    public static GPSTracer sTracer;

    public static double longitude;
    public static double latitude;

    public GPSTracer() {

    }

    public static GPSTracer getInstance() {

        if(sTracer == null) {
            sTracer = new GPSTracer();
        }

        return sTracer;
    }



    public void getLocation() {

        locationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

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


                    Log.d("경도 : ", location.getLongitude() + "\n 위도 : " + location.getLatitude());
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

            for (String name : m_lstProviders) {

                locationManager.requestLocationUpdates(name, 1, 0, locationListener);
            }

            new Runnable() {
                @Override
                public void run() {


                    Log.d("test", "location");

                }
            };

        } else {
            Log.e("GPS Enable", "false");

            Toast.makeText(mContext, "GPS가 꺼져있습니다. GPS를 켜주시기 바랍니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);

        }
    }

}
