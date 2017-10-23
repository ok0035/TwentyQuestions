package graduateproject.com.twentyquestions.Firebase;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.view.GameRoomView;
import graduateproject.com.twentyquestions.view.MainView;
import graduateproject.com.twentyquestions.view.SplashView;

/**
 * Created by yrs00 on 2017-09-06.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final static String TAG = "FirebaseMsgService";
    private String msg;
    private String msgTitle;
    String body = null;
    String pushDataRoomPKey = null;
    String content = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());


        // 받은 푸시 메세지가 null인지 판별
        // 어차피 공백 상태에선 채팅을 못보내므로 항상 true
        if (remoteMessage.getData().size() > 0) {
            pushDataRoomPKey = remoteMessage.getData().get("RoomPKey");

            body = remoteMessage.getData().get("body");

            content = remoteMessage.getData().get("content");

            // 푸시 메세지가 왔을때, 어느 액티비티가 열려있는지 확인
            String callerName = getRunActivity();
            // 게임룸뷰(채팅창)에서 콜이 왔을때
            if (callerName.contains("GameRoomView")) {
                final GameRoomView gameRoomView = (GameRoomView) GameRoomView.mContext;

                // 혹시나 액티비티 상태에 의해 null 일수도 있으니까 null 체크
                if (gameRoomView != null) {
                    // gameRoomView의 알람은 두가지로 나뉜다.
                    // 1. 자기방에 있을때 온 자기방 알람 -> notificationAlram(X) + dosync(O)
                    // 2. 자기방에 있을때 온 다른방 알람 -> notificationAlram(O) + dosync(X) => 어차피 방 들어갈때 dosync 발생
                    // 위의 두 상황은 알람 데이터에 넣은 RoomPKey를 로컬DB의 GameList와 비교하여 구분할 수 있다.
                    switch (content) {
                        case "Chat":
                            contentChatFunc(gameRoomView);
                            break;
                        case "QA":
                            contentChatFunc(gameRoomView);
                            break;
                        case "RA":
                            contentChatFunc(gameRoomView);
                            break;
                    }

                }
            } else { // 게임룸뷰가 아닌 경우에도 알람만 띄워주면 된다.

                Log.d("CallerName", callerName);
                makeNotificationAlert(body);
            }


        }
    }


    private void contentChatFunc(final GameRoomView gameRoomView) {

        DBSI dbsi = new DBSI();
        String[][] getLocalGameList = dbsi.selectQuery("SELECT * FROM GameList");
        if (getLocalGameList[0][1].equals(pushDataRoomPKey)) { // 1번 상황 : dosync만 진행
            // 현재 localDB(dosync 전 상태)에서 해당 chatroom의 가장 마지막 chatPKey를 가져온다.
            final String lastChatDate = gameRoomView.findLastChatDate();

            DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                @Override
                public void onFinished(String response) {
                    gameRoomView.testFunc(lastChatDate);
                    gameRoomView.getGameChatListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    gameRoomView.exchangeView();
                    Toast.makeText(getApplicationContext(), "Push 알람 도착 + Dosync 완료", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPreExcute() {

                }
            });

        } else { // 2번 상황
            makeNotificationAlert(body);
        }
    }

    private void makeNotificationAlert(String body) {
        Intent intent = new Intent(this, MainView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), SplashView.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setContentTitle("TwentyQuestions")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000});

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, mBuilder.build());

    }


    private String getRunActivity() {

        ActivityManager activity_manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> task_info = activity_manager.getRunningTasks(9999);


        for (int i = 0; i < task_info.size(); i++) {

            Log.d("getRunActivity", "[" + i + "] activity:" + task_info.get(i).topActivity.getPackageName() + " >> " + task_info.get(i).topActivity.getClassName());

        }

        return task_info.get(0).topActivity.getClassName();

    }


}
