package graduateproject.com.twentyquestions.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.adapter.GameChatListViewAdapter;
import graduateproject.com.twentyquestions.item.ChatDataItem;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;

/**
 * Created by mapl0 on 2017-08-22.
 */

public class GameRoomView extends BaseActivity {
    LinearLayout parentView, llChat, llChatList;
    EditText edChat;
    TextView btnSendMessage, tvChatTest;
    ListView gameChatListView;
    GameChatListViewAdapter gameChatListViewAdapter;
    String chat = "";
    DBSI dbsi;
    String[][] localGameList;
    String[][] localChat;
    String memberCount;
    JSONObject data = new JSONObject();
    View.OnClickListener send;
    ArrayList<ChatDataItem> chatDataItemlist;
    public static Context context;

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
                chat = edChat.getText().toString();
//                tvChatTest.setText(chat);
//                System.out.println("StartClickEvent");
                if (chat.length() != 0) { // 텍스트 입력안하면 메세지 전송x -> 카톡 따라함
                    try {
                        data.put("ChatRoomPKey", localGameList[0][1]);
                        data.put("ChatText", chat);
                        data.put("Count", (Integer.parseInt(memberCount) - 1) + "");
                        data.put("Type", "0");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NetworkSI networkSI = new NetworkSI();
                    String response = networkSI.request(DataSync.Command.SENDCHATDATA, data.toString(), new NetworkSI.AsyncResponse() {
                        @Override
                        public void onSuccess(String response) {
                            System.out.println("/////////////////////////////////////////////");
//                            String[][] getLocalChatTable = dbsi.selectQuery("Select Max(PKey) from Chat");
//                            final String lastChatPkey = (getLocalChatTable[0][0] != null) ? getLocalChatTable[0][0] : "0";
                            DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                @Override
                                public void onFinished(String response) {
//                                    String[][] addedChatData = dbsi.selectQuery("select * from Chat where PKey > " + lastChatPkey);
//
//                                    Log.d("addedChatData.length", addedChatData.length + "");
//
//                                    for (int i = 0; i < addedChatData.length; i++) {
//                                        ChatDataItem chatDataItem = new ChatDataItem();
//                                        chatDataItem.setUserPKey(addedChatData[i][2]);
//                                        String[][] userNameAndFlag = dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + addedChatData[i][2]);
//                                        chatDataItem.setUserName(userNameAndFlag[0][0]);
//                                        chatDataItem.setUserMySelf(userNameAndFlag[0][1]);
//                                        chatDataItem.setChattingText(addedChatData[i][3]);
//                                        chatDataItemlist.add(chatDataItem);
//                                    }
//
//                                    gameChatListViewAdapter.notifyDataSetChanged();

                                    edChat.setText(null);
                                }
                            });
                        }

                        @Override
                        public void onFailure(String response) {

                        }
                    });

                }


            }
        };

    }

    @Override
    public void setView() {
        super.setView();

//        tvChatTest = new TextView(MainView.mContext);
//        tvChatTest.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gameChatListView = new ListView(mContext);
        gameChatListView.setAdapter(gameChatListViewAdapter);
        gameChatListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gameChatListViewAdapter.notifyDataSetChanged();

        llChatList = new LinearLayout(MainView.mContext);
        llChatList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

        llChatList.addView(gameChatListView);

        edChat = new EditText(MainView.mContext);
        edChat.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        btnSendMessage = new TextView(MainView.mContext);
        btnSendMessage.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3f));
        btnSendMessage.setText("보내기");
        btnSendMessage.setOnClickListener(send);

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
        context = this;
        DataSync.getInstance().setDSContext(context);
        //  localDB의 GameMember와 GameList를 이용해서 게임방의 데이터들을 가져온다.
        dbsi = new DBSI();
        Log.d("GameRoomView", " setValues()");

        localGameList = dbsi.selectQuery("SELECT * FROM GameList");
        Log.d("GameListPKey", localGameList[0][0]);
        Log.d("ChatRoomPKey", localGameList[0][1]);
        memberCount = dbsi.selectQuery("SELECT Count(PKey) FROM GameMember WHERE GameListPKey = " + localGameList[0][0])[0][0];
        localChat = dbsi.selectQuery("SELECT * FROM Chat WHERE ChatRoomPKey = " + localGameList[0][1]);
        int localChatLength = (localChat != null) ? localChat.length : 0;

        chatDataItemlist = new ArrayList<>();
        if (localChatLength != 0) {
            for (int i = 0; i < localChatLength; i++) {
                ChatDataItem chatDataItem = new ChatDataItem();
                chatDataItem.setUserPKey(localChat[i][2]);
                String[][] userNameAndFlag = dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + localChat[i][2]);
                chatDataItem.setUserName(userNameAndFlag[0][0]);
                chatDataItem.setUserMySelf(userNameAndFlag[0][1]);
                chatDataItem.setChattingText(localChat[i][3]);
                chatDataItemlist.add(chatDataItem);
            }
        }

        gameChatListViewAdapter = new GameChatListViewAdapter(mContext, chatDataItemlist);


        /// 임시로 친구 데이터 삽입
        // 나중엔 지우자
        if ((dbsi.selectQuery("SELECT * FROM User WHERE PKey = 1") == null) ? true : false) {
            dbsi.query("INSERT INTO User('PKey', 'ID', 'LoginType' ,'NickName','Gender','BirthDay','MySelf', 'CreatedDate', 'UpdatedDate') " +
                    "VALUES('1','admin','0','toby','1','1993-12-06 12:00:00','1','2017-08-03 12:27:47','17-08-11 11:54:09')");
        }
        dbsi.selectQuery("SELECT * FROM User");


    }

    public void testFunc(String lastChatPkey) {

        String[][] addedChatData = dbsi.selectQuery("select * from Chat where PKey > " + lastChatPkey);
        int localSyncChatLength = (addedChatData != null) ? addedChatData.length : 0;
        Log.d("addedChatData.length", localSyncChatLength  + "");

        for (int i = 0; i < localSyncChatLength ; i++) {
            ChatDataItem chatDataItem = new ChatDataItem();
            chatDataItem.setUserPKey(addedChatData[i][2]);
            String[][] userNameAndFlag = dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + addedChatData[i][2]);
            chatDataItem.setUserName(userNameAndFlag[0][0]);
            chatDataItem.setUserMySelf(userNameAndFlag[0][1]);
            chatDataItem.setChattingText(addedChatData[i][3]);
            chatDataItemlist.add(chatDataItem);
        }

        gameChatListViewAdapter.notifyDataSetChanged();
    }

}
