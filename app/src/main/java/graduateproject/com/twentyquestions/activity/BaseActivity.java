package graduateproject.com.twentyquestions.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class BaseActivity extends Activity {

    public static Context mContext;


    private int firstFlag = 0;

    public BaseActivity() {

        mContext = this;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



}
