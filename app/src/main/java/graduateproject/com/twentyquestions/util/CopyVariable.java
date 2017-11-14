package graduateproject.com.twentyquestions.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import graduateproject.com.twentyquestions.R;

/**
 * Created by mapl0 on 2017-11-13.
 */

public class CopyVariable extends Activity {

    private android.widget.ListView lvFriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);

    }
}
