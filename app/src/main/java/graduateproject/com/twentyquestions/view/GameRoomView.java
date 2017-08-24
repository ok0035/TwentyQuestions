package graduateproject.com.twentyquestions.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.network.HttpNetwork;

/**
 * Created by mapl0 on 2017-08-22.
 */

public class GameRoomView extends BaseActivity {
    LinearLayout parentView, llChat, llChatList;
    EditText edChat;
    TextView btnSendMessage, tvChatTest;
    String chat = "";

    View.OnClickListener send;



    public GameRoomView() {
        super();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setValues();
        setUpEvents();
        setView();
        setCustomActionBar();
        setContentView(parentView);

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();

        send = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat += edChat.getText().toString();
                tvChatTest.setText(chat);
                new HttpNetwork(new ArrayList<NameValuePair>(), new HttpNetwork.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onFailure(String response) {

                    }
                });
            }
        };

    }

    @Override
    public void setView() {
        super.setView();

        tvChatTest = new TextView(MainView.mContext);
        tvChatTest.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        llChatList = new LinearLayout(MainView.mContext);
        llChatList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        llChatList.addView(tvChatTest);

        edChat = new EditText(MainView.mContext);
        edChat.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        btnSendMessage = new TextView(MainView.mContext);
        btnSendMessage.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3f));
        btnSendMessage.setText("보내기");

        llChat = new LinearLayout(MainView.mContext);
        llChat.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5f));
        llChat.setOrientation(LinearLayout.HORIZONTAL);
        llChat.addView(edChat);
        llChat.addView(btnSendMessage);

        parentView = new LinearLayout(MainView.mContext);
        parentView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parentView.setOrientation(LinearLayout.VERTICAL);
        parentView.addView(llChatList);
        parentView.addView(llChat);

    }

    @Override
    public void setValues() {

    }

}
