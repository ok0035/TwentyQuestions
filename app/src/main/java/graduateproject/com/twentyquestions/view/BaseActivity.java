package graduateproject.com.twentyquestions.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.util.CalculatePixel;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class BaseActivity extends AppCompatActivity{

    public static Context mContext;
    public static TextView titleView;
    public static ImageView backView;
    public static String serverAddress = "http://heronation.net/";

    private int firstFlag = 0;

    public BaseActivity() {

        mContext = this;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        new CalculatePixel(this);
    }
    public void setUpEvents() {
        //TODO - 이벤트 등록
    }

    public void setView() {
        //TODO  - 뷰 생성

    }

    public void setValues() {
        //TODO  - 변수 초기화
    }

    public void bindView() {
        //TODO - 레이아웃 매핑
    }


    public void setCustomActionBar() {


        // 액션바 속성 선언 //
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayShowHomeEnabled(false);
        myActionBar.setDisplayHomeAsUpEnabled(false);
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowCustomEnabled(true);
        myActionBar.setHomeButtonEnabled(true);

//        myActionBar.setHomeAsUpIndicator(R.mipmap.hambutton);

        LayoutInflater inf = LayoutInflater.from(mContext);
        View customBarView = inf.inflate(R.layout.toolbar, null);
        this.titleView = (TextView) customBarView.findViewById(R.id.titleView);
        this.backView = (ImageView) customBarView.findViewById(R.id.backView);

        myActionBar.setCustomView(customBarView);
        myActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) customBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        getSupportActionBar().setElevation(0);


    }

    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public static void ShowDoubleArray(String description, String[][] data) {

        if (data != null) {

            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {

                    System.out.println(description + " : " + data[i][j]);

                }
            }
        } else {

            Log.d("data", "data is null");

        }

    }

}
