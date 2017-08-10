package graduateproject.com.twentyquestions.network;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import graduateproject.com.twentyquestions.activity.BaseActivity;
import graduateproject.com.twentyquestions.activity.MainView;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class NetworkSI {

    private final String serverAddress = "http://heronation.net/";
    String[] userInfo;
    Handler handler;
    String response;

    public String request(DataSync.Command command, String data) {

        //패킷 정보

        final DBSI db = new DBSI(BaseActivity.mContext, "TwentyQuestions.db", null, 1);
        userInfo = db.getUserInfo().split("/");

        Log.d("command", DataSync.Command.GETFULLDATA.toString() + ".............................................................................");
//        Log.d("version_baseOS", Build.VERSION.BASE_OS + "");
////        Log.d("version_codename", Build. + "");
//        Log.d("version_release", Build.VERSION.RELEASE + "");

        JSONObject packet = new JSONObject();

        Log.d("UserPKey", userInfo[0]);
        Log.d("ID", userInfo[1]);
        Log.d("Password", userInfo[2]);
        Log.d("UDID", Build.ID);
        Log.d("DeviceType", Build.BRAND);
        Log.d("DeviceName", Build.MODEL);
        Log.d("OS", Build.VERSION.RELEASE);
        Log.d("Longitude", MainView.getLongitude() + "");
        Log.d("Latitude", MainView.getLatitude() + "");


        try {
            packet.put("PKey", userInfo[0]);
            packet.put("ID", userInfo[1]);
            packet.put("Password", userInfo[2]);
            packet.put("UDID", Build.ID);
            packet.put("DeviceType", "1");
            packet.put("DeviceName", Build.MODEL);
            packet.put("OS", Build.VERSION.RELEASE);
            packet.put("Phone", "");
            packet.put("Longitude", MainView.getLongitude() + "");
            packet.put("Latitude", MainView.getLatitude() + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("packet", packet.toString()));
        params.add(new BasicNameValuePair("command", command.toString()));
        params.add(new BasicNameValuePair("data", data));

        JSONArray jsonarr = new JSONArray(params);

//        handler = new Handler();

        try {
            response = new HttpNetwork(params, new HttpNetwork.AsyncResponse() {
                @Override
                public void onSuccess(String response) {


                    Log.d("response", response);
                }

                @Override
                public void onFailure(String response) {

                }

            }).execute(serverAddress + "android/twentyQuestions/Request.php").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

}
