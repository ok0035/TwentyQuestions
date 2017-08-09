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
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;


public class MainView extends BaseActivity {

    private static double longitude;
    private static double latitude;
    public static Context mContext;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private android.widget.Button test;
    private android.widget.TextView textview1;
    Handler handler;

    public MainView() {

        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);



        DataSync.getInstance().Timer();
        getLocation();

        final DBSI db = new DBSI(mContext, "TwentyQuestions.db", null, 1);

        db.query("insert into Chat(Pkey, ChatRoomPkey, UserPkey, ChatText, CreatedDate) Values(1, 1, 3, 'asdf', datetime('now','localtime'))");

        String[][] test1 = db.selectQuery("select * from Chat");

        for(int i = 0; i> test1.length; i++) {
            for(int j = 0; j< test1[i].length; j++) {
                System.out.println("i : " + i + ", j : " + test1[i][j]);
            }
        }

        db.insertUserInfo();

        this.textview1 = (TextView) findViewById(R.id.textview1);
        this.test = (Button) findViewById(R.id.test);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                textview1.setText("경도 : " + MainView.getLongitude() + ", 위도 : " + MainView.getLatitude());
            }
        };

        Timer timer = new Timer();
        TimerTask tsk = new TimerTask() {
            @Override
            public void run() {

                Bundle bundle = new Bundle();

                Message message = new Message();
                message.setData(bundle);

                handler.sendMessage(message);

            }
        };
        timer.schedule(tsk, 0, 2000);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "경도 : " + MainView.getLongitude() + ", 위도 : " + MainView.getLatitude(), Snackbar.LENGTH_LONG).setAction("알림", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
                DataSync.getInstance().doSync();
            }
        });

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

                    Snackbar.make(getWindow().getDecorView().getRootView(), "경도 : " + MainView.getLongitude() + "\n 위도 : " + MainView.getLatitude(), Snackbar.LENGTH_LONG).setAction("알림", new View.OnClickListener() {
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
