package graduateproject.com.twentyquestions.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DataSync;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class SubActivity extends BaseActivity {

    private android.widget.Button test;
    private android.widget.TextView textview1;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        this.textview1 = (TextView) findViewById(R.id.textview1);
        this.test = (Button) findViewById(R.id.test);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                textview1.setText("경도 : " + MainActivity.getLongitude() + ", 위도 : " + MainActivity.getLatitude());
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
                Snackbar.make(view, "경도 : " + MainActivity.getLongitude() + ", 위도 : " + MainActivity.getLatitude(), Snackbar.LENGTH_LONG).setAction("알림", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
                DataSync.getInstance().doSync();
            }
        });

    }
}
