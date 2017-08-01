package graduateproject.com.twentyquestions.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import graduateproject.com.twentyquestions.network.DataSync;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class BaseActivity extends Activity{

    public static Context mContext;

    public BaseActivity() {

        mContext = this;
        DataSync.getInstance().Timer();
        DataSync.getInstance().doSync();


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
