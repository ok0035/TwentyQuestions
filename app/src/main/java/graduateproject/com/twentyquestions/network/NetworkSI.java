package graduateproject.com.twentyquestions.network;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class NetworkSI {

    private final String serverAddress = "http://heronation.net/";
    String[] userInfo;
    Handler handler;
    String response;

    public interface AsyncResponse {
        void onSuccess(String response);
        void onFailure(String response);
    }

    public AsyncResponse delegate = null;

    public String request(DataSync.Command command, String data, final AsyncResponse delegate) {

        //패킷 정보

//        final DBSI db = new DBSI(BaseActivity.mContext, "TwentyQuestions.db", null, 1);

        final DBSI db = new DBSI();

        String[][] userInfo = db.selectQuery("SELECT PKey, ID, Password FROM User");
//
//        if (userInfo == null) {
//
//            userInfo = new String[1][3];
//            userInfo[0][0] = "1";
//            userInfo[0][1] = "admin";
//            userInfo[0][2] = "admin0101";
//
//        }

//        Log.d("command", DataSync.Command.GETFULLDATA.toString() + ".............................................................................");
//        Log.d("version_baseOS", Build.VERSION.BASE_OS + "");
////        Log.d("version_codename", Build. + "");
//        Log.d("version_release", Build.VERSION.RELEASE + "");

        JSONObject packet = new JSONObject();

//        Log.d("UserPKey", checkNull(userInfo[0][0]));
//        Log.d("ID", checkNull(userInfo[0][1]));
//        Log.d("Password", checkNull(userInfo[0][2] + ".."));
//        Log.d("UDID", Build.ID);
//        Log.d("DeviceType", Build.BRAND);
//        Log.d("DeviceName", Build.MODEL);
//        Log.d("OS", Build.VERSION.RELEASE);
//        Log.d("Longitude", MainViewTest.getLongitude() + "");
//        Log.d("Latitude", MainViewTest.getLatitude() + "");
//
//        Log.d("Key","");

        try {
            if(userInfo!=null && userInfo.length>0)
            {
                System.out.println("userInfo Is NotNull");
                packet.put("PKey", checkNull(userInfo[0][0]));
                packet.put("ID", checkNull(userInfo[0][1]));
                packet.put("Password", checkNull(userInfo[0][2]));
                packet.put("UDID", checkNull(FirebaseInstanceId.getInstance().getToken()));
                packet.put("DeviceType", "1");
                packet.put("DeviceName", checkNull(Build.MODEL));
                packet.put("OS", checkNull(Build.VERSION.RELEASE));
                packet.put("Phone", "");
            }
            else
            {
                System.out.println("userInfo Is Null");
                packet.put("PKey", "");
                packet.put("ID", "");
                packet.put("Password", "");
                packet.put("UDID", checkNull(FirebaseInstanceId.getInstance().getToken()));
                packet.put("DeviceType", "1");
                packet.put("DeviceName", checkNull(Build.MODEL));
                packet.put("OS", checkNull(Build.VERSION.RELEASE));
                packet.put("Phone", "");
            }

//            packet.put("Longitude", MainViewTest.getLongitude() + "");
//            packet.put("Latitude", MainViewTest.getLatitude() + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("pakcet", packet.toString());

        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("packet", packet.toString()));
        params.add(new BasicNameValuePair("command", command.toString()));
        params.add(new BasicNameValuePair("data", data));

//        JSONArray jsonarr = new JSONArray(params);

//        handler = new Handler();

        try {
            response = new HttpNetwork(params, new HttpNetwork.AsyncResponse() {
                @Override
                public void onSuccess(String response) {
                    delegate.onSuccess(response);
                    Log.d("response", response);
                }

                @Override
                public void onFailure(String response) {
                    delegate.onFailure(response);

                }

                @Override
                public void onPreExcute() {

                }

<<<<<<< HEAD
            }).execute(serverAddress + "TQTest/twentyQuestions/Request.php").get();
=======
            }).execute(serverAddress + "android/twentyQuestions/Request.php").get();
>>>>>>> a8896de26416d9ad3a77602b4a9376164df89284

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String checkNull(String str) {
        if (str != null && str.length() > 0) {

            System.out.println("checkNull result is NotNull... "+str);
            return str;

        } else {

            System.out.println("checkNull result is Null... "+str);
            return "";

        }
    }

}
