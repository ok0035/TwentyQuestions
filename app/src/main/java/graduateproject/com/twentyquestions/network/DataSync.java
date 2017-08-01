package graduateproject.com.twentyquestions.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by mapl0 on 2017-08-01.
 *
 * 전역으로 접근하는 DataSync 와 NetworkSI 는 DataSync.getInstance.doSync, NetworkSI.getInstance.request(data)와 같이 접근한다.
 */

public class DataSync extends Thread {
    private boolean isSyncing;
    private boolean needSyncing;
    private static DataSync sync;

    public static enum DeviceType { IPHONE, ANDROID, WEB, PC, ECT }
    public static enum Command { REQUEST_FULL_DATA, REGIST, LOGIN }
    public static enum ResultName { LOGIN_FAILED, LOGIN_SUCCESS, REQUEST_CHAT_DATA}

    public DataSync(Runnable run) {
        super(run);
    }

    public DataSync() {

    }

    public static DataSync getInstance() {

        if(sync == null)
            sync = new DataSync();

        return sync;

    }

    public void Timer() {

        if(needSyncing == false) {

            needSyncing = true;

        }

        if (!isSyncing) {

            new DataSync(new TimerRunnable()).start();

        } else {

            Log.d("Sync", "is syncing...");

        }

    }

    public void doSync() {

        if(isSyncing && needSyncing == false) {

            needSyncing = true;

        }

        if (!isSyncing) {

            new DataSync(new DoSyncRunnable()).start();

        } else {

            Log.d("Sync", "is syncing...");

        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("HANDLER");
//            TestData testData = new TestData();
//            testData.setPKey(msg.getData().getString("pkey"));
//            testData.setNameKR(msg.getData().getString("name"));

        }
    };

    private class DoSyncRunnable implements Runnable {

        @Override
        public void run() {

            System.out.println("THREADING...");

            Log.d("Sync", "need sync");
            isSyncing = true;
            NetworkSI.getInstance().request(Command.REQUEST_FULL_DATA);
            isSyncing = false;

            if(needSyncing) needSyncing = false;

        }
    }

    private class TimerRunnable implements Runnable {

        @Override
        public void run() {

            while(true) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Time Division Threading...");

                Log.d("Sync", "need sync");
                isSyncing = true;
                NetworkSI.getInstance().request(Command.REQUEST_FULL_DATA);
                isSyncing = false;

                if(needSyncing) doSync();

            }
        }
    }
}
