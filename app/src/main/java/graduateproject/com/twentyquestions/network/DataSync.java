package graduateproject.com.twentyquestions.network;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import graduateproject.com.twentyquestions.controller.DataSyncController;
import graduateproject.com.twentyquestions.util.GPSTracer;

/**
 * Created by mapl0 on 2017-08-01.
 * <p>
 * 전역으로 접근하는 DataSync 와 NetworkSI 는 DataSync.getInstance.doSync, NetworkSI.getInstance.requestSync(data)와 같이 접근한다.
 */

public class DataSync extends Thread {
    private static boolean isSyncing;
    private static boolean needSyncing;
    private static DataSync sync;
    private static boolean timerFlag = false;
    public static boolean endingFlag = false;
    DataSyncController datasyncController;
    private Timer timer;

    // added by CHS

    private Context dsContext;

    public interface AsyncResponse {
        void onFinished(String response);
        void onPreExcute();
    }

    public AsyncResponse delegate = null;

    static ActivityManager am;
    static ComponentName cn;

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
        SENDCHATDATA,
        SETGAME,
        SENDQA,
        SENDRA,
        SETMEMBER,
        FINDFRIEND,
        SETFRIEND,
        SETCOIN,
        SETPOPULARITY,
        SENDREPORT,
        LOGINFAILED,
        LOGINSUCCESS,
        ENTERGAMEROOM,
        LEAVEGAMEROOM,
        TEMPUSERDATA,
        CHECKUSERNUMBER

    }

    public DataSync(Runnable run) {
        super(run);
    }

    public DataSync() {

        needSyncing = false;
        isSyncing = false;
        timerFlag = false;
//        dsContext = null;


    }

    public void setDSContext(Context context) {
        dsContext = context;
    }


    public static DataSync getInstance() {

        if (sync == null)
            sync = new DataSync();

        return sync;

    }

    public void Timer(final AsyncResponse delegate) {

//        new DataSync(new TimerRunnable()).start();
        if(timer != null) {
            timer.cancel();
            timer = null;
            timerFlag = false;
        }

        if (timerFlag == false) {
            timerFlag = true;
            timer = new Timer();
            final TimerTask timerTask = new TimerTask() {

                public void run() {
                    doSync(new AsyncResponse() {
                        @Override
                        public void onFinished(String response) {
                            delegate.onFinished(response);

                        }

                        @Override
                        public void onPreExcute() {
                            delegate.onPreExcute();
                        }
                    });    // 타이머가 울리면 이 곳으로 들어온다.
                    endingFlag = false;
                    System.out.println("GPS " + "Longitude : " + GPSTracer.longitude + "   Latitude : " + GPSTracer.latitude);

                }
            };
            timer.schedule(timerTask, 0, 200000); // 첫번째 인자인 tmrTask 로 1초 뒤에 알림을 준다.

            timerFlag = false;

        }


    }

    public void cancelTimer() {
        timer.cancel();
        timer  = null;
        timerFlag = false;
    }

    public void doSync(final AsyncResponse delegate) {

        new DataSync(new DoSyncRunnable(new AsyncResponse() {
            @Override
            public void onFinished(String response) {
                delegate.onFinished(response);
            }

            @Override
            public void onPreExcute() {
                delegate.onPreExcute();
            }
        })).start();

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

        AsyncResponse delegate = null;

        public DoSyncRunnable(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {

            if (isSyncing && needSyncing == false) {

                Log.d("Sync", "isSyncing & !needSyncing");
                needSyncing = true;

            } else if (!isSyncing) {
                Log.d("Sync", "!isSyncing");
                Log.d("Sync", "DoSync");

                requestSync(Command.GETFULLDATA, getFullData());

            } else {

                Log.d("Sync", "sync error");

            }
        }

        public void requestSync(DataSync.Command command, final String data) {

            isSyncing = true;

            final DBSI db = new DBSI();
            datasyncController = new DataSyncController();

            String[][] userInfo = db.selectQuery("SELECT PKey, ID, Password FROM User");
            JSONObject packet = new JSONObject();

            try {
                if (userInfo != null && userInfo.length > 0) {
                    System.out.println("userInfo Is NotNull");
                    packet.put("PKey", checkNull(userInfo[0][0]));
                    packet.put("ID", checkNull(userInfo[0][1]));
                    packet.put("Password", checkNull(userInfo[0][2]));
                    packet.put("UDID", checkNull(FirebaseInstanceId.getInstance().getToken()));
                    packet.put("DeviceType", "1");
                    packet.put("DeviceName", checkNull(Build.MODEL));
                    packet.put("OS", checkNull(Build.VERSION.RELEASE));
                    packet.put("Phone", "");
                } else {
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


            new HttpNetwork(params, new HttpNetwork.AsyncResponse() {
                @Override
                public void onSuccess(String response) {

                    datasyncController.updateData(response);
                    Log.d("response", response);

                    if (needSyncing) {
                        requestNeedSync(Command.GETFULLDATA, data);

                    } else {
                        isSyncing = false;
                        delegate.onFinished(response);
//                        traceActivityClass();
                    }


                }

                @Override
                public void onFailure(String response) {

                }

                @Override
                public void onPreExcute() {
                    delegate.onPreExcute();
                }


            }).execute("http://heronation.net/android/twentyQuestions/Request.php");

        }

        public void requestNeedSync(DataSync.Command command, String data) {

            isSyncing = true;

            final DBSI db = new DBSI();
            datasyncController = new DataSyncController();

            String[][] userInfo = db.selectQuery("SELECT PKey, ID, Password FROM User");
            JSONObject packet = new JSONObject();

            try {
                if (userInfo != null && userInfo.length > 0) {
                    System.out.println("userInfo Is NotNull");
                    packet.put("PKey", checkNull(userInfo[0][0]));
                    packet.put("ID", checkNull(userInfo[0][1]));
                    packet.put("Password", checkNull(userInfo[0][2]));
                    packet.put("UDID", checkNull(FirebaseInstanceId.getInstance().getToken()));
                    packet.put("DeviceType", "1");
                    packet.put("DeviceName", checkNull(Build.MODEL));
                    packet.put("OS", checkNull(Build.VERSION.RELEASE));
                    packet.put("Phone", "");
                } else {
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


            new HttpNetwork(params, new HttpNetwork.AsyncResponse() {
                @Override
                public void onSuccess(String response) {

                    datasyncController.updateData(response);
                    isSyncing = false;
                    needSyncing = false;
                    Log.d("response", response);
                    delegate.onFinished(response);
//                    traceActivityClass();

                }

                @Override
                public void onFailure(String response) {

                }

                @Override
                public void onPreExcute() {
                    delegate.onPreExcute();
                }

            }).execute("http://heronation.net/android/twentyQuestions/Request.php");

        }

        public String getFullData() {
            JSONObject data = new JSONObject();
            JSONObject chat = new JSONObject();
            JSONObject chatMember = new JSONObject();
            JSONObject chatRoom = new JSONObject();
            JSONObject gameList = new JSONObject();
            JSONObject gameMember = new JSONObject();
            JSONObject twentyQuestions = new JSONObject();
            JSONObject askAnswerList = new JSONObject();
            JSONObject rightAnswerList = new JSONObject();

            DBSI db = new DBSI();

            String[][] selectChatRoom = db.selectQuery("select * from ChatRoom");
            String[][] selectChat = db.selectQuery("select * from Chat");
            String[][] selectChatMember = db.selectQuery("select * from ChatMember");
            String[][] selectGameList = db.selectQuery("select * from GameList");
            String[][] selectGameMember = db.selectQuery("select * from GameMember");
            String[][] selectTwentyQuestions = db.selectQuery("select * from TwentyQuestions");
            String[][] selectAskAnswerList = db.selectQuery("select * from AskAnswerList");
            String[][] selectRightAnswerList= db.selectQuery("select * from RightAnswerList");

            String chatRoomPKey = (selectChatRoom != null) ? selectChatRoom[selectChatRoom.length - 1][0] : "0";
            String chatRoomUpdatedDate = (selectChatRoom != null) ? selectChatRoom[selectChatRoom.length - 1][5] : "NOW()";

            String chatPKey = (selectChat != null) ? selectChat[selectChat.length - 1][0] : "0";
            String chatCreatedDate = (selectChat != null) ? selectChat[selectChat.length - 1][6] : "NOW()";

            String chatMemberPKey = (selectChatMember != null) ? selectChatMember[selectChatMember.length - 1][0] : "0";
            String chatMemberUpdatedDate = (selectChatMember != null) ? selectChatMember[selectChatMember.length - 1][6] : "NOW()";

            String gameListPKey = (selectGameList != null) ? selectGameList[selectGameList.length - 1][0] : "0";
            String gameListUpdatedDate = (selectGameList != null) ? selectGameList[selectGameList.length - 1][13] : "NOW()";

            String gameMemberPKey = (selectGameMember != null) ? selectGameMember[selectGameMember.length - 1][0] : "0";
            String gameMemberUpdatedDate = (selectGameMember != null) ? selectGameMember[selectGameMember.length - 1][7] : "NOW()";

            String twentyQuestionsPKey = (selectTwentyQuestions != null) ? selectTwentyQuestions[selectTwentyQuestions.length - 1][0] : "0";
            String twentyUpdatedDate = (selectTwentyQuestions != null) ? selectTwentyQuestions[selectTwentyQuestions.length - 1][6] : "NOW()";

            String askAnswerListPKey = (selectAskAnswerList != null) ? selectAskAnswerList[selectAskAnswerList.length - 1][0] : "0";
            String askAnswerUpdatedDate = (selectAskAnswerList != null) ? selectAskAnswerList[selectAskAnswerList.length - 1][8] : "NOW()";

            String rightAnswerListPKey = (selectRightAnswerList != null) ? selectRightAnswerList[selectRightAnswerList.length - 1][0] : "0";
            String rightAnswerUpdatedDate = (selectRightAnswerList != null) ? selectRightAnswerList[selectRightAnswerList.length - 1][9] : "NOW()";

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

                twentyQuestions.put("PKey", twentyQuestionsPKey);
                twentyQuestions.put("UpdatedDate", twentyUpdatedDate);

                askAnswerList.put("PKey", askAnswerListPKey);
                askAnswerList.put("UpdatedDate", askAnswerUpdatedDate);

                rightAnswerList.put("PKey", rightAnswerListPKey);
                rightAnswerList.put("UpdatedDate", rightAnswerUpdatedDate);

                data.put("ChatRoom", chatRoom);
                data.put("Chat", chat);
                data.put("ChatMember", chatMember);
                data.put("GameList", gameList);
                data.put("GameMember", gameMember);
                data.put("TwentyQuetions",twentyQuestions);
                data.put("AskAnswerList",askAnswerList);
                data.put("RightAnswerList",rightAnswerList);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return data.toString();
        }

        public String checkNull(String str) {
            if (str != null && str.length() > 0) {

                System.out.println("checkNull result is NotNull... " + str);
                return str;

            } else {

                System.out.println("checkNull result is Null... " + str);
                return "";

            }
        }

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
//                    NetworkSI.getInstance().requestSync(Command.GETFULLDATA);
//                    isSyncing = false;
//
//                    if(needSyncing) {
//                        Log.d("Sync", "needSyncing");
//
//                        isSyncing = true;
//                        NetworkSI.getInstance().requestSync(Command.GETFULLDATA);
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
