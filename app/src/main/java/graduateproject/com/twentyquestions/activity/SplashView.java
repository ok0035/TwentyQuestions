package graduateproject.com.twentyquestions.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DBSI;

import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelX;
import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelY;

/**
 * Created by yrs00 on 2017-08-03.
 */

public class SplashView extends BaseActivity {

    RelativeLayout parentLayout;
    DBSI db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpEvents();
        setView();
//        setCustomActionBar();
        setContentView(parentLayout);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                moveToProperActivity();
            }
        }, 2000);
    }

    void moveToProperActivity() {
//        String[][] userInfo = db.selectQuery("SELECT PKey, ID, Password FROM User");
        String[][] userInfo = db.selectQuery("SELECT PKey, ID, Password FROM User");

        if (userInfo == null) {

            // TODO - 로그인 화면으로 이동해야함
            Intent intent = new Intent(mContext, LoginView.class);
            startActivity(intent);

        }
        else {

            // 메인화면으로 이동
            Intent intent = new Intent(mContext, MainView.class);
            startActivity(intent);
        }

        finish();

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();

//        db = new DBSI(BaseActivity.mContext, "TwentyQuestions.db", null, 1);
        db = new DBSI();

//        db.query("");

    }

    @Override
    public void setView() {
        super.setView();
        parentLayout = new RelativeLayout(mContext);
        parentLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        ImageView splashImage = new ImageView(this);
        splashImage.setImageResource(R.mipmap.ic_launcher_round);
        splashImage.setLayoutParams(new ViewGroup.LayoutParams((int)calculatePixelX(300), (int)calculatePixelY(300)));
        splashImage.setX(calculatePixelX(10));
        splashImage.setY(calculatePixelY(75));

        parentLayout.addView(splashImage);

    }

    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
    }
}
