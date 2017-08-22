package graduateproject.com.twentyquestions.util;

import android.content.Context;
import android.util.DisplayMetrics;

import graduateproject.com.twentyquestions.view.BaseActivity;

/**
 * Created by Heronation on 2017-07-24.
 */

public class CalculatePixel {

    public static Context mContext = null;
    public static DisplayMetrics dm;
    public static int deviceWidth;
    public static int deviceHeight;
    public static CalculatePixel calpixel;


    public CalculatePixel(Context context) {
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
        deviceWidth = dm.widthPixels;
        deviceHeight = dm.heightPixels;
    }

    public static CalculatePixel getInstance() {

        if(calpixel == null) {
            calpixel = new CalculatePixel(BaseActivity.mContext);
        }

        return calpixel;
    }


    public static float calculatePixelX(float pixel) {

        float afterPixelX = 0;

        afterPixelX = (pixel * deviceWidth) / 320;

        return afterPixelX;

    }

    public static float calculatePixelY(float pixel) {

        float afterPixelY = 0;

        afterPixelY = (pixel * deviceHeight) / 480;

        return afterPixelY;

    }

}
