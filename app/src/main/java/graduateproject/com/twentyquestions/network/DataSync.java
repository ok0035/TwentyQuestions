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
        JSONObject gameList = new JSONObject();
        JSONObject gameMember = new JSONObject();

        DBSI db = new DBSI();

        String[][] selectChatRoom = db.selectQuery("select * from ChatRoom");
        String[][] selectChat = db.selectQuery("select * from Chat");
        String[][] selectChatMember = db.selectQuery("select * from ChatMember");
        String[][] selectGameList = db.selectQuery("select * from GameList");
        String[][] selectGameMember = db.selectQuery("select * from GameMember");

        String chatRoomPKey = (selectChatRoom != null) ? selectChatRoom[selectChatRoom.length-1][0] : null;
        String chatRoomUpdatedDate = (selectChatRoom != null) ? selectChatRoom[selectChatRoom.length-1][5] : null;

        String chatPKey = (selectChat != null) ? selectChat[selectChat.length-1][0] : null;
        String chatCreatedDate = (selectChat != null) ? selectChat[selectChat.length-1][6] : null;

        String chatMemberPKey = (selectChatMember != null) ? selectChatMember[selectChatMember.length-1][0] : null;
        String chatMemberUpdatedDate = (selectChatMember != null) ? selectChatMember[selectChatMember.length-1][6] : null;

        String gameListPKey = (selectGameList != null) ? selectGameList[selectGameList.length-1][0] : null;
        String gameListUpdatedDate = (selectGameList != null) ? selectGameList[selectGameList.length-1][13] : null;

        String gameMemberPKey = (selectGameMember != null) ? selectGameMember[selectGameMember.length-1][0] : null;
        String gameMemberUpdatedDate = (selectGameMember != null) ? selectGameMember[selectGameMember.length-1][7] : null;

        try {

            chatRoom.put("PKey", chatRoomPKey);
            chatRoom.put("UpdatedDate", chatRoomUpdatedDate);

            chat.put("PKey", chatPKey);
            chat.put("CreatedDate", chatCreatedDate);

            chatMember.put("PKey", chatMemberPKey);
            chatMember.put("UpdatedDate", chatMemberUpdatedDate);

            gameList.put("PKey", gameListPKey);
            gameList.put("UpdatedDate", gameListUpdatedDate);

            gameMember.put("PKey", gameMemberPKey);
            gameMember.put("UpdatedDate", gameMemberUpdatedDate);

            data.put("ChatRoom", chatRoom);
            data.put("Chat", chat);
            data.put("ChatMember", chatMember);
            data.put("GameList", gameList);
            data.put("GameMember", gameMember);

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
