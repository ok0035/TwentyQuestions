package graduateproject.com.twentyquestions.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by yrs00 on 2017-09-06.
 */

public class MyFirebaseInstanceIdSerivce extends FirebaseInstanceIdService {
    private final static String TAG = "FCM_ID";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FirebaseInstanceId Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);


    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

    }

}