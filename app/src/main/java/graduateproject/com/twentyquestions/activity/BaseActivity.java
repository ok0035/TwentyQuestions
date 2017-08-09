package graduateproject.com.twentyquestions.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.util.CalculatePixel;

import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelX;
import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelY;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class BaseActivity extends AppCompatActivity{

    public static Context mContext;
    public static TextView titleView;
    public static ImageView backView;

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

    public void setCustomActionBar() {

        // 액션바 뷰 디자인 //
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(params);
        relativeLayout.setBackgroundColor(Color.GRAY);

        // 타이틀 텍스트 //
        titleView = new TextView(this);
        titleView.setText("CustomActionBar");
        titleView.setWidth((int)calculatePixelX(320));
        titleView.setHeight((int)calculatePixelY(50));
        titleView.setX(0);
        titleView.setY(0);
        titleView.setTextColor(Color.WHITE);
        titleView.setGravity(Gravity.CENTER);

        relativeLayout.addView(titleView);

        // 뒤로가기 버튼 //
        backView = new ImageView(this);

        backView.setImageResource(R.mipmap.ic_launcher_round);
        backView.setX(0);
        backView.setY(calculatePixelY(3));

        relativeLayout.addView(backView);

        // 액션바 속성 선언 //
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayShowHomeEnabled(false);
        myActionBar.setDisplayHomeAsUpEnabled(false);
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowCustomEnabled(true);
        myActionBar.setHomeButtonEnabled(true);

//        myActionBar.setHomeAsUpIndicator(R.mipmap.hambutton);

//        LayoutInflater inf = LayoutInflater.from(mContext);
//        View customBarView = inf.inflate(R.layout.actionbar_main, null);

        View customBarView = relativeLayout;

        myActionBar.setCustomView(customBarView);
        myActionBar.setDisplayShowCustomEnabled(true);

        Log.i("customBarView Width",customBarView.getWidth()+"");
        Log.i("customBarView Height",customBarView.getHeight()+"");

        Toolbar parent = (Toolbar) customBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        getSupportActionBar().setElevation(0);


    }

    public static void ShowDoubleArray(String[][] data) {

        if(data != null) {

            for(int i = 0; i< data.length; i++) {
                for(int j =0; j<data[i].length; j++) {

                    Log.d("DoubleArrayResult[" + i + "][" + j + "]", data[i][j]);

                }
            }
        } else {

            Log.d("data", "data is null");

        }



    }



}
