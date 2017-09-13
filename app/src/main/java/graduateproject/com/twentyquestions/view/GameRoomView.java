package graduateproject.com.twentyquestions.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.adapter.GameChatListViewAdapter;
import graduateproject.com.twentyquestions.item.ChatDataItem;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.CalculatePixel;

/**
 * Created by mapl0 on 2017-08-22.
 */

public class GameRoomView extends BaseActivity {

    EditText edChat;
    LinearLayout parentView, llChat, llChatList, llGame, llQuestion, llEditChat, llBtnChat, llQuestionBox, llGuessRight, llPlayingInfo, llLeftQuestion, llPlayigQuestion;
    private LinearLayout divisionLine;
    TextView btnSendMessage, btnQAMode, tvChatTest, tvQuestion, tvQuestionLabel, tvGuessRight, tvQuestionCountLabel, tvRightCountLabel, tvPlayingCountLabel;
    TextView tvQeustionCount, tvRightCount, tvPlayingCount;
    private String gameListKey;

    public ListView getGameChatListView() {
        return gameChatListView;
    }

    ListView gameChatListView;
    GameChatListViewAdapter gameChatListViewAdapter;
    String chat = "";
    DBSI dbsi;
    String[][] localGameList;
    String[][] localChat;
    String memberCount;
    JSONObject data = new JSONObject();
    private boolean QAFlag = false;

    View.OnClickListener send;
    View.OnClickListener send_chat;
    View.OnClickListener send_QA;
    View.OnClickListener QAMode;


    ArrayList<ChatDataItem> chatDataItemlist;
    private Context context;

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

        send_chat = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat = edChat.getText().toString();
//                tvChatTest.setText(chat);
//                System.out.println("StartCli  ckEvent");

                Log.d("getScrollY", gameChatListView.getScrollY() + "//");
                final String beforeSendLastChatPKey = findLastChatPKey();

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
                            Log.d("onSUccessresponse", response);
                            System.out.println("/////////////////////////////////////////////");
                            DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                @Override
                                public void onFinished(String response) {
                                    testFunc(beforeSendLastChatPKey);
                                    gameChatListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                                }

                                @Override
                                public void onPreExcute() {

                                }
                            });
                        }

                        @Override
                        public void onFailure(String response) {

                        }
                    });
                    edChat.setText(null);


                }


            }
        };

        send_QA = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getMemberPriority().equals("0")) {
                    Toast.makeText(mContext, "질답모드_출시자", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(mContext, "질답모드_참가자", Toast.LENGTH_SHORT).show();
                    JSONObject data = new JSONObject();
                    try {
                        data.put("GameListPKey",dbsi.selectQuery("Select PKey from GameList")[0][0]);
                        data.put("Guess",edChat.getText().toString());
                        data.put("MemberPriority","1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NetworkSI networkSI = new NetworkSI();
                    networkSI.request(DataSync.Command.SENDQA, data.toString(), new NetworkSI.AsyncResponse() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String response) {
                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        };

        send = send_chat;

        QAMode = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!QAFlag) { // QAFlag = false -> 질답 모드
                    edChat.setBackgroundColor(Color.MAGENTA);
                    if (getMemberPriority().equals("0")) {
                        btnSendMessage.setText("답변");
                    } else {
                        btnSendMessage.setText("질문");
                    }
                    send = send_QA;
                    btnSendMessage.setOnClickListener(send);
                    QAFlag = true;
                } else {
                    edChat.setBackgroundColor(Color.CYAN);
                    btnSendMessage.setText("보내기");
                    send = send_chat;
                    btnSendMessage.setOnClickListener(send);
                    QAFlag = false;
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

        edChat = new EditText(MainView.mContext);
        edChat.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        edChat.setSingleLine(true);

        btnSendMessage = new TextView(MainView.mContext);
        btnSendMessage.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
        btnSendMessage.setText("보내기");
        btnSendMessage.setGravity(Gravity.CENTER);
        btnSendMessage.setOnClickListener(send);

        btnQAMode = new TextView(MainView.mContext);
        btnQAMode.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
        btnQAMode.setText("?");
        btnQAMode.setGravity(Gravity.CENTER);
        btnQAMode.setBackgroundColor(Color.MAGENTA);
        btnQAMode.setOnClickListener(QAMode);

        llEditChat = new LinearLayout(MainView.mContext);
        llEditChat.setOrientation(LinearLayout.HORIZONTAL);
        llEditChat.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 8f));
        llEditChat.addView(edChat);

        llBtnChat = new LinearLayout(MainView.mContext);
        llBtnChat.setOrientation(LinearLayout.HORIZONTAL);
        llBtnChat.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
        llBtnChat.addView(btnSendMessage);
        llBtnChat.addView(btnQAMode);

        tvQuestionLabel = new TextView(MainView.mContext);
        tvQuestionLabel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f));
        tvQuestionLabel.setGravity(Gravity.CENTER);
        tvQuestionLabel.setText("문제");

        tvQuestion = new TextView(MainView.mContext);
        tvQuestion.setLayoutParams(new LinearLayout.LayoutParams((int) CalculatePixel.calculatePixelX(150), 0, 8f));
        tvQuestion.setBackgroundColor(Color.CYAN);
        tvQuestion.setGravity(Gravity.CENTER);
        if (getMemberPriority().equals("0")) { // 문제 출시자
            String questionObj = dbsi.selectQuery("SELECT Object FROM TwentyQuestions Where GameListPKey = " + gameListKey)[0][0];
            tvQuestion.setText(questionObj);
        } else {
            tvQuestion.setText("???");
        }

        llQuestion = new LinearLayout(MainView.mContext);
        llQuestion.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 7f));
        llQuestion.setOrientation(LinearLayout.VERTICAL);
        llQuestion.setGravity(Gravity.CENTER);
        llQuestion.setWeightSum(10);
        llQuestion.addView(tvQuestionLabel);
        llQuestion.addView(tvQuestion);

        tvGuessRight = new TextView(MainView.mContext);
        tvGuessRight.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvGuessRight.setGravity(Gravity.CENTER);
        if (getMemberPriority().equals("0")) {
            tvGuessRight.setText("정답이 올라왔어요");
        } else {
            tvGuessRight.setText("정답 맞히기");
        }


        llGuessRight = new LinearLayout(MainView.mContext);
        llGuessRight.setOrientation(LinearLayout.VERTICAL);
        llGuessRight.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3f));
        llGuessRight.addView(tvGuessRight);

        llQuestionBox = new LinearLayout(MainView.mContext);
        llQuestionBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 6f));
        llQuestionBox.setOrientation(LinearLayout.HORIZONTAL);
        llQuestionBox.setWeightSum(10);
        llQuestionBox.addView(llQuestion);
        llQuestionBox.addView(llGuessRight);

        tvQuestionCountLabel = new TextView(MainView.mContext);
        tvQuestionCountLabel.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4f));
        tvQuestionCountLabel.setGravity(Gravity.CENTER);
        tvQuestionCountLabel.setText("남은질문수 : ");

        tvQeustionCount = new TextView(MainView.mContext);
        tvQeustionCount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        tvQeustionCount.setText(dbsi.selectQuery("Select MaxAskable From TwentyQuestions")[0][0]);

        tvRightCountLabel = new TextView(MainView.mContext);
        tvRightCountLabel.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4f));
        tvRightCountLabel.setGravity(Gravity.CENTER);
        tvRightCountLabel.setText("남은정답수 : ");

        tvRightCount = new TextView(MainView.mContext);
        tvRightCount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        tvRightCount.setText(dbsi.selectQuery("Select MaxGuessable From TwentyQuestions")[0][0]);

        llLeftQuestion = new LinearLayout(MainView.mContext);
        llLeftQuestion.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        llLeftQuestion.setOrientation(LinearLayout.HORIZONTAL);
        llLeftQuestion.setWeightSum(10);
        llLeftQuestion.setGravity(Gravity.CENTER);
        llLeftQuestion.addView(tvQuestionCountLabel);
        llLeftQuestion.addView(tvQeustionCount);
        llLeftQuestion.addView(tvRightCountLabel);
        llLeftQuestion.addView(tvRightCount);

        tvPlayingCountLabel = new TextView(MainView.mContext);
        tvPlayingCountLabel.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 4f));
        tvPlayingCountLabel.setGravity(Gravity.CENTER);
        tvPlayingCountLabel.setText("진행상황 : ");

        tvPlayingCount = new TextView(MainView.mContext);
        tvPlayingCount.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6f));
        tvPlayingCount.setGravity(Gravity.CENTER_VERTICAL);
        tvPlayingCount.setText("0 번 질문 대기중");

        llPlayigQuestion = new LinearLayout(MainView.mContext);
        llPlayigQuestion.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        llPlayigQuestion.setOrientation(LinearLayout.HORIZONTAL);
        llPlayigQuestion.setGravity(Gravity.CENTER);
        llPlayigQuestion.setWeightSum(10);
        llPlayigQuestion.addView(tvPlayingCountLabel);
        llPlayigQuestion.addView(tvPlayingCount);

        llPlayingInfo = new LinearLayout(MainView.mContext);
        llPlayingInfo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4f));
        llPlayingInfo.setOrientation(LinearLayout.VERTICAL);
        llPlayingInfo.setGravity(Gravity.CENTER);
        llPlayingInfo.addView(llLeftQuestion);
        llPlayingInfo.addView(llPlayigQuestion);

        divisionLine = new LinearLayout(MainView.mContext);
        divisionLine.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        layoutParams.setMargins(0, 0, 0, 0);
        divisionLine.setLayoutParams(layoutParams);

        llGame = new LinearLayout(MainView.mContext);
        llGame.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams llGameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
        llGameParams.setMargins(0, (int) CalculatePixel.calculatePixelY(10), 0, (int) CalculatePixel.calculatePixelY(10));
        llGame.setLayoutParams(llGameParams);
        llGame.setWeightSum(10);
        llGame.setGravity(Gravity.CENTER);
        llGame.addView(llQuestionBox);
        llGame.addView(llPlayingInfo);

        llChatList = new LinearLayout(MainView.mContext);
        llChatList.setOrientation(LinearLayout.VERTICAL);
        llChatList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 7.5f));
        llChatList.addView(divisionLine);
        llChatList.addView(gameChatListView);

        llChat = new LinearLayout(MainView.mContext);
        llChat.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.5f));
        llChat.setOrientation(LinearLayout.HORIZONTAL);
        llChat.setWeightSum(10);
        llChat.setGravity(Gravity.CENTER_VERTICAL);
        llChat.setBackgroundColor(Color.CYAN);
        llChat.addView(llEditChat);
        llChat.addView(llBtnChat);

        parentView = new LinearLayout(MainView.mContext);
        parentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parentView.setOrientation(LinearLayout.VERTICAL);
        parentView.setWeightSum(10);
        parentView.addView(llGame);
        parentView.addView(llChatList);
        parentView.addView(llChat);
    }

    @Override
    public void setValues() {


        context = this;

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
                chatDataItem.setChatPKey(localChat[i][0]);
                chatDataItemlist.add(chatDataItem);
            }
        }

        gameChatListViewAdapter = new GameChatListViewAdapter(mContext, chatDataItemlist);


        DataSync.getInstance().Timer(new DataSync.AsyncResponse() {
            String beforeChatPKey;

            @Override
            public void onFinished(String response) {


                Log.d("chatDataItemlist.size()", chatDataItemlist.size() + "");
                if (chatDataItemlist.size() > 0) {

                    testFunc(beforeChatPKey);
                    System.out.println("TestFuncLoadFinish");
                }

            }

            @Override
            public void onPreExcute() {
                beforeChatPKey = findLastChatPKey();
            }
        });


    }

    public void testFunc(String lastChatPkey) {

        Log.d("Start ", "testFunc");
        String[][] localGameList = dbsi.selectQuery("SELECT * FROM GameList");
        String[][] localChat = dbsi.selectQuery("SELECT * FROM Chat WHERE ChatRoomPKey = " + localGameList[0][1]);
        String[][] addedChatData = dbsi.selectQuery("select * from Chat where ChatRoomPKey = " + localGameList[0][1] + " and PKey > " + lastChatPkey);
        int localSyncChatLength = (addedChatData != null) ? addedChatData.length : 0;
        Log.d("addedChatData.length", localSyncChatLength + "");

        for (int i = 0; i < localSyncChatLength; i++) {
            ChatDataItem chatDataItem = new ChatDataItem();
            chatDataItem.setUserPKey(addedChatData[i][2]);
            String[][] userNameAndFlag = dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + addedChatData[i][2]);
            chatDataItem.setUserName(userNameAndFlag[0][0]);
            chatDataItem.setUserMySelf(userNameAndFlag[0][1]);
            chatDataItem.setChattingText(addedChatData[i][3]);
            chatDataItem.setChatPKey(localChat[i][0]);
            chatDataItemlist.add(chatDataItem);
        }


        gameChatListViewAdapter.notifyDataSetChanged();


//        gameChatListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        titleView.setText(localGameList[0][3]);
    }

    public String findLastChatPKey() {
        String[][] localGameList = dbsi.selectQuery("SELECT * FROM GameList");
        String[][] localChatData = dbsi.selectQuery("SELECT * FROM Chat Where ChatRoomPKey = " + localGameList[0][1]);
        if (localChatData != null) {
            return localChatData[localChatData.length - 1][0];
        } else {
            return "0";
        }
    }

    public String getMemberPriority() {

        String memberPriority = null;

        gameListKey = dbsi.selectQuery("SELECT PKey FROM GameList")[0][0];
        memberPriority = dbsi.selectQuery("SELECT MemberPriority FROM GameMember WHERE GameListPKey = " + gameListKey)[0][0];

        return memberPriority;
    }

}
