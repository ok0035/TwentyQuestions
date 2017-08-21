package graduateproject.com.twentyquestions.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import graduateproject.com.twentyquestions.controller.DataSyncController;
import graduateproject.com.twentyquestions.util.GPSTracer;

/**
 * Created by mapl0 on 2017-08-01.
 * <p>
 * 전역으로 접근하는 DataSync 와 NetworkSI 는 DataSync.getInstance.doSync, NetworkSI.getInstance.request(data)와 같이 접근한다.
 */

public class DataSync extends Thread {
    private boolean isSyncing;
    private boolean needSyncing;
    private static DataSync sync;
    private static boolean timerFlag = false;

    public static enum DeviceType {IPHONE, ANDROID, WEB, PC, ECT}

    public static enum Command {

        TRYREGIST,
        TRYLOGIN,
        GETFULLDATA,
        GETLETTERLIST,
        GETLETTERITEM,
        GETUSERSELFDATA,
        GETINIT,
        GETRANDOMNAME,
        SETCHATROOM,
        SETGAME,
        SENDQA,
        SETMEMBER,
        FINDFRIEND,
        SETFRIEND,
        SETCOIN,
        SETPOPULARITY,
        SENDREPORT,
        LOGINFAILED,
        LOGINSUCCESS,

    }

    public DataSync(Runnable run) {
        super(run);
    }

    public DataSync() {

        needSyncing = false;
        isSyncing = false;
        timerFlag = false;

    }

    public static DataSync getInstance() {

        if (sync == null)
            sync = new DataSync();

        return sync;

    }

    public void Timer() {

//        new DataSync(new TimerRunnable()).start();

        if (timerFlag == false) {

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {

                public void run() {
                    doSync();    // 타이머가 울리면 이 곳으로 들어온다.
                    System.out.println("GPS " + "Longitude : " + GPSTracer.longitude + "   Latitude : " + GPSTracer.latitude);
                }
            };
            timer.schedule(timerTask, 0, 10000); // 첫번째 인자인 tmrTask 로 1초 뒤에 알림을 준다.

            timerFlag = true;

        }


    }

    public void doSync() {

        new DataSync(new DoSyncRunnable()).start();

    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            System.out.println("HANDLER");
//
//            needSyncing = false;
//
//        }
//    };

    private class DoSyncRunnable implements Runnable {

        @Override
        public void run() {

            String response;

            DataSyncController datasyncController = new DataSyncController();
            NetworkSI networkSI = new NetworkSI();

            if (isSyncing && needSyncing == false) {

                Log.d("Sync", "isSyncing & !needSyncing");
                needSyncing = true;

            } else if (!isSyncing) {
                Log.d("Sync", "!isSyncing");

                Log.d("Sync", "DoSync");
                isSyncing = true;
                response = networkSI.request(Command.GETFULLDATA, getFullData());
                datasyncController.updateData(response);

                Log.d("RequestResponse : ", "Data " + response);

                isSyncing = false;

                if (needSyncing) {
                    Log.d("Sync", "needSyncing");

                    isSyncing = true;
                    response = networkSI.request(Command.GETFULLDATA, getFullData());
                    datasyncController.updateData(response);
                    isSyncing = false;

                    needSyncing = false;

//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean("needSyncing", needSyncing);
//
//                        Message message = new Message();
//                        message.setData(bundle);
//
//                        handler.sendMessage(message);
                }

            } else {

                Log.d("Sync", "sync error");

            }
        }
    }

    public String getFullData() {


        JSONObject data = new JSONObject();
        JSONObject chat = new JSONObject();
        JSONObject chatMember = new JSONObject();
        JSONObject chatRoom = new JSONObject();

        try {

            chatRoom.put("PKey", "1");
            chatRoom.put("UpdatedDate", "2017-08-03 13:05:31");

            chat.put("PKey", "1");
            chat.put("CreatedDate", "2017-08-03 14:28:47");

            chatMember.put("PKey", "1");
            chatMember.put("UpdatedDate", "2017-08-03 00:30:00");

            data.put("ChatRoom", chatRoom);
            data.put("Chat", chat);
            data.put("ChatMember", chatMember);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return data.toString();
    }

//    private class TimerRunnable implements Runnable {
//
//        @Override
//        public void run() {
//
//            while(true) {
//
//                if (!isSyncing) {
//
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    System.out.println("Time Division Threading...");
//
//                    isSyncing = true;
//                    NetworkSI.getInstance().request(Command.GETFULLDATA);
//                    isSyncing = false;
//
//                    if(needSyncing) {
//                        Log.d("Sync", "needSyncing");
//
//                        isSyncing = true;
//                        NetworkSI.getInstance().request(Command.GETFULLDATA);
//                        isSyncing = false;
//
//                        needSyncing = false;
//
////                        Bundle bundle = new Bundle();
////                        bundle.putBoolean("needSyncing", needSyncing);
////
////                        Message message = new Message();
////                        message.setData(bundle);
////
////                        handler.sendMessage(message);
//                    }
//
//                } else {
//
//                    Log.d("Sync", "is syncing...");
//
//                }
//            }
//        }
//    }
}
