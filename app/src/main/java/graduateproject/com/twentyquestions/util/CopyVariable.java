package graduateproject.com.twentyquestions.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import graduateproject.com.twentyquestions.R;

/**
 * Created by mapl0 on 2017-11-13.
 */

public class CopyVariable extends Activity {

    private android.widget.TextView tvRoomNumber;
    private android.widget.TextView tvRoomName;
    private android.widget.TextView tvRoomDesc;
    private android.widget.TextView tvGameStatus;
    private android.widget.ImageView ivProfile;
    private android.widget.TextView tvChatText;
    private android.widget.LinearLayout llChatItem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_item);
        this.llChatItem = (LinearLayout) findViewById(R.id.llChatItem);
        this.tvChatText = (TextView) findViewById(R.id.tvChatText);
        this.ivProfile = (ImageView) findViewById(R.id.ivProfile);


    }
}
