package graduateproject.com.twentyquestions.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.adapter.GameChatListViewAdapter;
import graduateproject.com.twentyquestions.item.ChatDataItem;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.CalculatePixel;
import graduateproject.com.twentyquestions.util.GPSTracer;
import graduateproject.com.twentyquestions.util.ParseData;

import static graduateproject.com.twentyquestions.view.MainViewTest.mContext;

/**
 * Created by mapl0 on 2017-08-22.
 */

public class GameRoomView extends BaseActivity {

    EditText edChat;
    LinearLayout parentView, llChat, llChatList, llGame, llQuestion, llEditChat, llBtnChat, llQuestionBox, llGuessRight, llPlayingInfo, llLeftQuestion, llPlayigQuestion;
    private LinearLayout divisionLine;
    TextView btnSendMessage, btnQAMode, tvChatTest, tvQuestion, tvQuestionLabel, tvGuessRight, tvQuestionCountLabel, tvRightCountLabel, tvPlayingCountLabel;
    TextView tvQeustionCount, tvRightCount, tvPlayingCount;
    TextView endingTextView;
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
    String myUserPKey;
    JSONObject data = new JSONObject();
    private boolean QAFlag = false;

    private View.OnClickListener send;
    private View.OnClickListener send_chat;
    private View.OnClickListener send_QA;
    private View.OnClickListener QAMode;
    private View.OnClickListener sendReceiveRA;
    private View.OnClickListener createNewGame;


    private JudgeRightDialog judgeRightDialog;
    private GuessRightDialog guessRightDialog;


    ArrayList<ChatDataItem> chatDataItemlist;


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
                final String beforeSendLastChatDate = findLastChatDate();

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
                                    testFunc(beforeSendLastChatDate);
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

                String twentyquestionsPKey = dbsi.selectQuery("Select PKey from TwentyQuestions")[0][0];

                if (dbsi.selectQuery("Select GameStatus from GameList")[0][0].equals("2")) {
                    Toast.makeText(mContext, "게임이 종료되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (getMemberPriority().equals("0")) {
//                    Toast.makeText(mContext, "질답모드_출시자", Toast.LENGTH_SHORT).show();
                        // 출시자에게는 2가지의 상황이있다.
                        // 질문에 대해서 답을 할때와, 새 질문이 없을때
                        String[][] findlocalAskAnswerList_Ans = dbsi.selectQuery("select * from AskAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " and Answerer = " + myUserPKey);
                        final String beforeSendLastChatDate = findLastChatDate();
                        if (findlocalAskAnswerList_Ans == null || !findlocalAskAnswerList_Ans[findlocalAskAnswerList_Ans.length - 1][5].isEmpty()) {
                            // 답변을 AskAnswerList에 입력해주자.
//                        Toast.makeText(mContext,"답변",Toast.LENGTH_SHORT).show();

                            Toast.makeText(mContext, "아직 질문이 올라오지 않았습니다.", Toast.LENGTH_SHORT).show();
//
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put("AskAnswerListPKey", findlocalAskAnswerList_Ans[findlocalAskAnswerList_Ans.length - 1][0]);
//                            data.put("Answer", edChat.getText().toString());
//                            data.put("MemberPriority", getMemberPriority());
//                            data.put("ChatRoomPKey", dbsi.selectQuery("select ChatRoomPKey from GameList")[0][0]);
//                            data.put("GameListPKey", dbsi.selectQuery("select PKey from GameList")[0][0]);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        NetworkSI networkSI = new NetworkSI();
//                        networkSI.request(DataSync.Command.SENDQA, data.toString(), new NetworkSI.AsyncResponse() {
//                            @Override
//                            public void onSuccess(String response) {
////                                Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
//
//                                if (response.contains("AAList_UPDATE_SUCCESS")) {
//                                    DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
//                                        @Override
//                                        public void onFinished(String response) {
//                                            edChat.setText(null);
//                                            testFunc(beforeSendLastChatDate);
//
//                                        }
//
//                                        @Override
//                                        public void onPreExcute() {
//
//                                        }
//                                    });
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onFailure(String response) {
//
//                            }
//                        });

                        } else {


                            // 답변을 AskAnswerList에 입력해주자.
//                        Toast.makeText(mContext,"답변",Toast.LENGTH_SHORT).show();

                            JSONObject data = new JSONObject();
                            try {
                                data.put("AskAnswerListPKey", findlocalAskAnswerList_Ans[findlocalAskAnswerList_Ans.length - 1][0]);
                                data.put("Answer", edChat.getText().toString());
                                data.put("MemberPriority", getMemberPriority());
                                data.put("ChatRoomPKey", dbsi.selectQuery("select ChatRoomPKey from GameList")[0][0]);
                                data.put("GameListPKey", dbsi.selectQuery("select PKey from GameList")[0][0]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            NetworkSI networkSI = new NetworkSI();
                            networkSI.request(DataSync.Command.SENDQA, data.toString(), new NetworkSI.AsyncResponse() {
                                @Override
                                public void onSuccess(String response) {
//                                Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();

                                    if (response.contains("AAList_UPDATE_SUCCESS")) {
                                        DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                            @Override
                                            public void onFinished(String response) {
                                                edChat.setText(null);
                                                testFunc(beforeSendLastChatDate);

                                            }

                                            @Override
                                            public void onPreExcute() {

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onFailure(String response) {

                                }
                            });

                        }


                    } else {
                        // MaxAskable이 0이 되면 버튼을 막아버린다.
                        if (dbsi.selectQuery("Select MaxAskable From TwentyQuestions")[0][0].equals("0")) {
                            Toast.makeText(mContext, "모든 질문 기회를 소비했습니다 \n 정답을 맞춰주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            // local AskAnswerList에서 마지막 항의 질문에 대답이 없다 -> 질문을 막는다.

                            String[][] findlocalAskAnswerList_guess = dbsi.selectQuery("select * from AskAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " and Guesser = " + myUserPKey);
                            final String beforeSendLastChatDate = findLastChatDate();
                            if (findlocalAskAnswerList_guess == null || !findlocalAskAnswerList_guess[findlocalAskAnswerList_guess.length - 1][5].isEmpty()) {
                                // 질문한적이 없거나, 전의 질문에 대답이 되었다.
                                JSONObject data = new JSONObject();
                                try {
                                    data.put("GameListPKey", dbsi.selectQuery("Select PKey from GameList")[0][0]);
                                    data.put("Guess", edChat.getText().toString());
                                    data.put("MemberPriority", "1");
                                    data.put("ChatRoomPKey", dbsi.selectQuery("select ChatRoomPKey from GameList")[0][0]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                NetworkSI networkSI = new NetworkSI();
                                networkSI.request(DataSync.Command.SENDQA, data.toString(), new NetworkSI.AsyncResponse() {
                                    @Override
                                    public void onSuccess(String response) {
//                            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                                        if (response.contains("AAList_INSERT_SUCCESS")) {
                                            DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                                @Override
                                                public void onFinished(String response) {
                                                    edChat.setText(null);
                                                    testFunc(beforeSendLastChatDate);
                                                    exchangeView();
                                                }

                                                @Override
                                                public void onPreExcute() {

                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onFailure(String response) {
                                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {

                                Toast.makeText(mContext, "아직 지난 질문의 답변이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();

                            }
                        }


                    }
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

        // 정답맞추기 클릭
        sendReceiveRA = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String twentyquestionsPKey = dbsi.selectQuery("Select PKey from TwentyQuestions")[0][0];

                if (dbsi.selectQuery("Select GameStatus from GameList")[0][0].equals("2")) {
                    Toast.makeText(mContext, "게임이 종료되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (getMemberPriority().equals("0")) { // 방장
                        String[][] findlocalRightAnswerList_Ans = dbsi.selectQuery("select * from RightAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " and Answerer = " + myUserPKey);

                        if (findlocalRightAnswerList_Ans[findlocalRightAnswerList_Ans.length - 1][5].isEmpty() && findlocalRightAnswerList_Ans != null) {
                            judgeRightDialog = new JudgeRightDialog(mContext);
                            judgeRightDialog.show();
                        } else {
                            Toast.makeText(mContext, "상대방이 답을 작성하지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        // MaxGuessable이 0이면 버튼을 막아버린다.
                        if (dbsi.selectQuery("Select MaxGuessable From TwentyQuestions")[0][0].equals("0")) {
                            Toast.makeText(mContext, "모든 기회를 소진했습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            String[][] findlocalRightAnswerList_guess = dbsi.selectQuery("select * from RightAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " and Guesser = " + myUserPKey);
                            if (findlocalRightAnswerList_guess != null) {
                                if (findlocalRightAnswerList_guess[findlocalRightAnswerList_guess.length - 1][5].isEmpty()) {
                                    Toast.makeText(mContext, "아직 지난 정답의 답변이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    guessRightDialog = new GuessRightDialog(mContext);
                                    guessRightDialog.show();
                                }
                            } else {
                                guessRightDialog = new GuessRightDialog(mContext);
                                guessRightDialog.show();
                            }

                        }


                    }
                }
            }
        };

        createNewGame = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCreateRoomDialog myCreateRoomDialog = new MyCreateRoomDialog(mContext);
                myCreateRoomDialog.show();
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
            String questionObj = dbsi.selectQuery("SELECT Object FROM TwentyQuestions Where GameListPKey = " + gameListKey)[0][0];
            if (dbsi.selectQuery("SELECT GameStatus FROM GameList")[0][0].equals("2")) {
                tvQuestion.setText(questionObj);
            } else {
                tvQuestion.setText("???");
            }

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
            if (dbsi.selectQuery("Select Guess from RightAnswerList where Answer == \"\" Order by PKey desc ") != null)
                tvGuessRight.setText("정답이 올라왔어요");
            else
                tvGuessRight.setText("정답을 기다리는 중...");
        } else {
            tvGuessRight.setText("정답 맞히기");
        }
        tvGuessRight.setOnClickListener(sendReceiveRA);

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

        String twentyquestionsPKey = dbsi.selectQuery("Select PKey from TwentyQuestions")[0][0];
        if (dbsi.selectQuery("Select Answer from AskAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " Order by PKey desc") == null ||
                dbsi.selectQuery("Select Answer from AskAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " Order by PKey desc")[0][0].length() != 0) {
            int askNum = 20 - Integer.parseInt(dbsi.selectQuery("Select MaxAskable From TwentyQuestions")[0][0]) + 1;
            tvPlayingCount.setText(askNum + " 번 질문 대기중");
        } else {
            int askNum = 20 - Integer.parseInt(dbsi.selectQuery("Select MaxAskable From TwentyQuestions")[0][0]);
            tvPlayingCount.setText(askNum + " 번 질문 완료, 답변 대기중");

        }


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

        endingTextView = new TextView(MainView.mContext);
        endingTextView.setWidth((int) CalculatePixel.calculatePixelX(320));
        endingTextView.setHeight((int) CalculatePixel.calculatePixelY(40));
//        endingTextView.setX(0);
//        endingTextView.setY(400);
        endingTextView.setBackgroundColor(Color.WHITE);
        String endingMent = "게임이 종료되었습니다. \n 새로운 게임을 시작하려면 클릭해주세요";
        if (dbsi.selectQuery("Select isWinner From GameMember Where GameListPKey = (Select PKey From GameList)")[0][0].equals("0")) {
            endingMent = "패배\n" + endingMent;
        } else {
            endingMent = "승리\n" + endingMent;
        }
        endingTextView.setText(endingMent);
        endingTextView.setGravity(Gravity.CENTER);
        if (dbsi.selectQuery("select GameStatus from GameList")[0][0].equals("2")) {
            endingTextView.setVisibility(View.VISIBLE);
        } else {
            endingTextView.setVisibility(View.GONE);
        }
//        endingTextView.setOnClickListener(createNewGame);

        parentView = new LinearLayout(MainView.mContext);
        parentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parentView.setOrientation(LinearLayout.VERTICAL);
        parentView.setWeightSum(10);
        parentView.addView(llGame);
        parentView.addView(llChatList);
        parentView.addView(llChat);
        parentView.addView(endingTextView);
    }

    @Override
    public void setValues() {


        //  localDB의 GameMember와 GameList를 이용해서 게임방의 데이터들을 가져온다.
        dbsi = new DBSI();
        Log.d("GameRoomView", " setValues()");

        myUserPKey = dbsi.selectQuery("Select PKey from User where MySelf = 0")[0][0];

        localGameList = dbsi.selectQuery("SELECT * FROM GameList");
        Log.d("GameListPKey", localGameList[0][0]);
        Log.d("ChatRoomPKey", localGameList[0][1]);
        memberCount = dbsi.selectQuery("SELECT Count(PKey) FROM GameMember WHERE GameListPKey = " + localGameList[0][0])[0][0];


//        localChat = dbsi.selectQuery("SELECT * FROM Chat WHERE ChatRoomPKey = " + localGameList[0][1]);


        localChat = dbsi.selectQuery(
                makeChatQuery(localGameList[0][0], localGameList[0][1]) + "order by U.Date, U.Flag;"
        );


        int localChatLength = (localChat != null) ? localChat.length : 0;
        System.out.println("localChat.length" + localChatLength);

        chatDataItemlist = new ArrayList<>();
        if (localChatLength != 0) {
            for (int i = 0; i < localChatLength; i++) {
                Log.d("Flag 내용", " chat");
                final ChatDataItem chatDataItem = new ChatDataItem();
                chatDataItem.setUserPKey(localChat[i][1]);

                if (dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + localChat[i][1]) == null) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("UserPKey", localChat[i][1]);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    NetworkSI networksi = new NetworkSI();
                    final int finalI = i;
                    networksi.request(DataSync.Command.TEMPUSERDATA, data.toString(), new NetworkSI.AsyncResponse() {
                        @Override
                        public void onSuccess(String response) {
                            System.out.println("tempUserData : " + response);
                            ParseData parse = new ParseData();
                            try {
//                                   JSONArray userArray = parse.jsonArrayInObject(response, "User");

//                                   for (int i = 0; i < userArray.length(); i++) {
                                JSONObject userData = new JSONArray(response).getJSONObject(0);
                                String query = "insert into User values (\'" +
                                        userData.getString("PKey") + "\', \'" + userData.getString("ID") + "\', \'" +
                                        userData.getString("SNSAccessToken") + "\', \'" + userData.getString("Password") + "\', \'" + userData.getString("LoginType") + "\', \'" +
                                        userData.getString("NickName") + "\', \'" + userData.getString("Phone") + "\', \'" + userData.getString("Gender") + "\', \'" +
                                        userData.getString("Birthday") + "\', \'" + userData.getString("Longitude") + "\', \'" + userData.getString("Latitude") + "\', 1, \'" +
                                        userData.getString("ConditionMessage") + "\', \'" + userData.getString("Introduction") + "\', \'" + userData.getString("IsVerification") + "\', \'" +
                                        userData.getString("Status") + "\', \'" + userData.getString("LatestLogin") + "\', \'" + userData.getString("UDID") + "\', \'" +
                                        userData.getString("DeviceType") + "\', \'" + userData.getString("DeviceName") + "\', \'" + userData.getString("OS") + "\', \'" +
                                        userData.getString("CreatedDate") + "\', \'" + userData.getString("UpdatedDate") + "\')";
                                dbsi.query(query);

                                String[][] userNameAndFlag = dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + localChat[finalI][1]);
                                chatDataItem.setUserName(userNameAndFlag[0][0]);
                                chatDataItem.setUserMySelf(userNameAndFlag[0][1]);
                                chatDataItem.setChattingText(localChat[finalI][2]);
                                chatDataItem.setObjPKey(localChat[finalI][0]);
                                if (localChat[finalI][4].contains("askans")) {
                                    chatDataItem.setChatFlag("askans");
                                } else if (localChat[finalI][4].contains("right")) {
                                    chatDataItem.setChatFlag("right");
                                } else {
                                    chatDataItem.setChatFlag("chat");
                                }
                                chatDataItemlist.add(chatDataItem);

//                                   }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(String response) {

                        }
                    });


                } else {
                    String[][] userNameAndFlag = dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + localChat[i][1]);
                    chatDataItem.setUserName(userNameAndFlag[0][0]);
                    chatDataItem.setUserMySelf(userNameAndFlag[0][1]);
                    chatDataItem.setChattingText(localChat[i][2]);
                    chatDataItem.setObjPKey(localChat[i][0]);
                    if (localChat[i][4].contains("askans")) {
                        chatDataItem.setChatFlag("askans");
                    } else if (localChat[i][4].contains("right")) {
                        chatDataItem.setChatFlag("right");
                    } else {
                        chatDataItem.setChatFlag("chat");
                    }
                    chatDataItemlist.add(chatDataItem);
                }


            }
        }

        Log.d("어레이리스트 길이 ", chatDataItemlist.size() + " / ");
        gameChatListViewAdapter = new GameChatListViewAdapter(mContext, chatDataItemlist);

        DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
            @Override
            public void onFinished(String response) {
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
                        beforeChatPKey = findLastChatDate();
                    }
                });
            }

            @Override
            public void onPreExcute() {

            }
        });


    }

    public String makeChatQuery(String GameListPKey, String ChatRoomPKey) {

        return "select * from \n" +
                "(select C.PKey as PKey, C.UserPKey as User, C.ChatText as Text, C.CreatedDate as Date, 'chat' as Flag\n" +
                "from Chat as C where ChatRoomPKey = " + ChatRoomPKey + "\n" +
                "union\n" +
                "select A.PKey as PKey, A.Guesser as User, A.Guess as Text, A.UpdatedDate as Date, 'askans1' as Flag \n" +
                "from AskAnswerList as A where A.TwentyQuestionsPKey = (Select PKey from TwentyQuestions where GameListPKey = " + GameListPKey + ")\n" +
                "union\n" +
                "select A.PKey as PKey, A.Answerer as User, A.Answer as Text, A.UpdatedDate as Date, 'askans2' as Flag\n" +
                "from AskAnswerList as A where A.TwentyQuestionsPKey = (Select PKey from TwentyQuestions where GameListPKey = " + GameListPKey + ") and A.Answer != \"\"\n" +
                "union\n" +
                "select R.PKey as PKey, R.Guesser as User, R.Guess as Text, R.UpdatedDate as Date, 'right1' as Flag \n" +
                "from RightAnswerList as R where R.TwentyQuestionsPKey = (Select PKey from TwentyQuestions where GameListPKey = " + GameListPKey + ")\n" +
                "union\n" +
                "select R.PKey as PKey, R.Answerer as User, R.Answer as Text, R.UpdatedDate as Date, 'right2' as Flag\n" +
                "from RightAnswerList as R where R.TwentyQuestionsPKey = (Select PKey from TwentyQuestions where GameListPKey = " + GameListPKey + ") and R.Answer != \"\")As U \n";
    }

    public void testFunc(String lastChatDate) {

        Log.d("Start ", "testFunc");
        String[][] localGameList = dbsi.selectQuery("SELECT * FROM GameList");
//        String[][] addedChatData = dbsi.selectQuery("select * from Chat where ChatRoomPKey = " + localGameList[0][1] + " and PKey > " + lastChatDate);

        final String[][] addedChatData = dbsi.selectQuery(
                makeChatQuery(localGameList[0][0], localGameList[0][1]) + "where U.Date > " + lastChatDate + " order by U.Date, U.Flag;"
        );

        int localSyncChatLength = (addedChatData != null) ? addedChatData.length : 0;
        Log.d("addedChatData.length", localSyncChatLength + "");

        for (int i = 0; i < localSyncChatLength; i++) {
            Log.d("Flag 내용", " chat");
            final ChatDataItem chatDataItem = new ChatDataItem();
            chatDataItem.setUserPKey(addedChatData[i][1]);

            if (dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + addedChatData[i][1]) == null) {
                JSONObject data = new JSONObject();
                try {
                    data.put("UserPKey", addedChatData[i][1]);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                NetworkSI networksi = new NetworkSI();
                final int finalI = i;
                networksi.request(DataSync.Command.TEMPUSERDATA, data.toString(), new NetworkSI.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println("tempUserData : " + response);
                        ParseData parse = new ParseData();
                        try {
                            JSONArray userArray = parse.jsonArrayInObject(response, "User");

//                                   for (int i = 0; i < userArray.length(); i++) {
                            JSONObject userData = new JSONArray(response).getJSONObject(0);
                            String query = "insert into User values (\'" +
                                    userData.getString("PKey") + "\', \'" + userData.getString("ID") + "\', \'" +
                                    userData.getString("SNSAccessToken") + "\', \'" + userData.getString("Password") + "\', \'" + userData.getString("LoginType") + "\', \'" +
                                    userData.getString("NickName") + "\', \'" + userData.getString("Phone") + "\', \'" + userData.getString("Gender") + "\', \'" +
                                    userData.getString("Birthday") + "\', \'" + userData.getString("Longitude") + "\', \'" + userData.getString("Latitude") + "\', 1, \'" +
                                    userData.getString("ConditionMessage") + "\', \'" + userData.getString("Introduction") + "\', \'" + userData.getString("IsVerification") + "\', \'" +
                                    userData.getString("Status") + "\', \'" + userData.getString("LatestLogin") + "\', \'" + userData.getString("UDID") + "\', \'" +
                                    userData.getString("DeviceType") + "\', \'" + userData.getString("DeviceName") + "\', \'" + userData.getString("OS") + "\', \'" +
                                    userData.getString("CreatedDate") + "\', \'" + userData.getString("UpdatedDate") + "\')";
                            dbsi.query(query);

                            String[][] userNameAndFlag = dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + addedChatData[finalI][1]);
                            chatDataItem.setUserName(userNameAndFlag[0][0]);
                            chatDataItem.setUserMySelf(userNameAndFlag[0][1]);
                            chatDataItem.setChattingText(addedChatData[finalI][2]);
                            chatDataItem.setObjPKey(addedChatData[finalI][0]);
                            if (addedChatData[finalI][4].contains("askans")) {
                                chatDataItem.setChatFlag("askans");
                            } else if (addedChatData[finalI][4].contains("right")) {
                                chatDataItem.setChatFlag("right");
                            } else {
                                chatDataItem.setChatFlag("chat");
                            }
                            chatDataItemlist.add(chatDataItem);

//                                   }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(String response) {

                    }
                });


            } else {
                String[][] userNameAndFlag = dbsi.selectQuery("SELECT NickName,MySelf FROM User WHERE PKey = " + addedChatData[i][1]);
                chatDataItem.setUserName(userNameAndFlag[0][0]);
                chatDataItem.setUserMySelf(userNameAndFlag[0][1]);
                chatDataItem.setChattingText(addedChatData[i][2]);
                chatDataItem.setObjPKey(addedChatData[i][0]);
                if (addedChatData[i][4].contains("askans")) {
                    chatDataItem.setChatFlag("askans");
                } else if (addedChatData[i][4].contains("right")) {
                    chatDataItem.setChatFlag("right");
                } else {
                    chatDataItem.setChatFlag("chat");
                }


                if (chatDataItemlist.size() == 0) {
                    chatDataItemlist.add(chatDataItem);
                } else if (chatDataItemlist.get(chatDataItemlist.size() - 1).getObjPKey().equals(addedChatData[i][0])
                        && (addedChatData[i][4].equals("askans1") || (addedChatData[i][4].equals("right1")))) {

                } else {
                    chatDataItemlist.add(chatDataItem);
                }

            }


        }

        gameChatListViewAdapter.notifyDataSetChanged();


        gameChatListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        titleView.setText(localGameList[0][3]);
    }

    public String findLastChatDate() {
        String[][] localGameList = dbsi.selectQuery("SELECT * FROM GameList");
        String[][] localChatData = dbsi.selectQuery(makeChatQuery(localGameList[0][0], localGameList[0][1]) + "order by U.Date, U.Flag;");

        if (localChatData != null) {
            return "'" + localChatData[localChatData.length - 1][3] + "'";
        } else {
            return "datetime('now')";
        }
    }

    public String getMemberPriority() {

        String memberPriority = null;

        gameListKey = dbsi.selectQuery("SELECT PKey FROM GameList")[0][0];
        memberPriority = dbsi.selectQuery("SELECT MemberPriority FROM GameMember WHERE GameListPKey = " + gameListKey)[0][0];

        return memberPriority;
    }

    public void exchangeView() {
        if (getMemberPriority().equals("0")) {
            // 정답맞히기 textView
            if (dbsi.selectQuery("Select Guess from RightAnswerList where Answer == \"\" Order by PKey desc ") != null)
                tvGuessRight.setText("정답이 올라왔어요");
            else
                tvGuessRight.setText("정답을 기다리는 중...");


        } else {
            tvGuessRight.setText("정답 맞히기");
        }

        // 남은 질문 갯수 textView
        tvQeustionCount.setText(dbsi.selectQuery("Select MaxAskable From TwentyQuestions")[0][0]);
        tvRightCount.setText(dbsi.selectQuery("Select MaxGuessable From TwentyQuestions")[0][0]);

        // 남은 기회 갯수 textView
        String twentyquestionsPKey = dbsi.selectQuery("Select PKey from TwentyQuestions")[0][0];
        if (dbsi.selectQuery("Select Answer from AskAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " Order by PKey desc") == null ||
                dbsi.selectQuery("Select Answer from AskAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " Order by PKey desc")[0][0].length() == 0) {
            int askNum = 20 - Integer.parseInt(dbsi.selectQuery("Select MaxAskable From TwentyQuestions")[0][0]);
            tvPlayingCount.setText(askNum + " 번 질문 완료, 답변 대기중");
        } else {
            int askNum = 20 - Integer.parseInt(dbsi.selectQuery("Select MaxAskable From TwentyQuestions")[0][0]) + 1;
            tvPlayingCount.setText(askNum + " 번 질문 대기중");
        }


        if (getMemberPriority().equals("0")) { // 문제 출시자
            String questionObj = dbsi.selectQuery("SELECT Object FROM TwentyQuestions Where GameListPKey = " + gameListKey)[0][0];
            tvQuestion.setText(questionObj);
        } else {
            if (dbsi.selectQuery("SELECT GameStatus FROM GameList")[0][0].equals("2")) {
                String questionObj = dbsi.selectQuery("SELECT Object FROM TwentyQuestions Where GameListPKey = " + gameListKey)[0][0];
                tvQuestion.setText(questionObj);
            } else {
                tvQuestion.setText("???");
            }

        }

        // ending멘트 뷰
        if (dbsi.selectQuery("select GameStatus from GameList")[0][0].equals("2")) {
            endingTextView.setVisibility(View.VISIBLE);
        } else {
            endingTextView.setVisibility(View.GONE);
        }

        String endingMent = "게임이 종료되었습니다. \n 새로운 게임을 시작하려면 클릭해주세요";
        if (dbsi.selectQuery("Select isWinner From GameMember Where GameListPKey = (Select PKey From GameList)")[0][0].equals("0")) {
            endingMent = "패배\n" + endingMent;
        } else {
            endingMent = "승리\n" + endingMent;
        }

        endingTextView.setText(endingMent);

    }

}


class MyCreateRoomDialog extends CreateRoomDialog {

    public MyCreateRoomDialog(@NonNull Context context) {
        super(context);
    }

    private String myUserPKey;
    DBSI dbsi;

    @Override
    public void setUpEvents() {
        super.setUpEvents();
        dbsi = new DBSI();
        myUserPKey = dbsi.selectQuery("Select PKey from User where MySelf = 0")[0][0];

                clickStartGame = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                Toast.makeText(getContext(), "TEST", Toast.LENGTH_SHORT).show();
                        //1.서버디비에서 게임 관련된거 전부 수정


                        JSONObject data = new JSONObject();
                        try {
                            data.put("GameMemberPKey", dbsi.selectQuery("Select PKey From GameMember Where Status != -1 And UserPKey = " + myUserPKey)[0][0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        NetworkSI networkSI = new NetworkSI();
                        networkSI.request(DataSync.Command.LEAVEGAMEROOM, data.toString(), new NetworkSI.AsyncResponse() {
                            @Override
                            public void onSuccess(String response) {
                                //2. 로컬디비에서 게임 관련 테이블 죄다 delete
                                if (response.contains("LEAVE_SUCCESS")) {

                                    dbsi.query("Delete from GameList; Delete from TwentyQuestions; Delete from AskAnswerList; Delete from RightAnswerList;");
                                    //단, GameMember는 -1로 되있는걸로 dosync를한다.
                                    DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                        @Override
                                        public void onFinished(String response) {
                                            JSONObject params = new JSONObject();
                                            try {
                                                params.put("RoomName", edRoomName.getText().toString());
                                                params.put("Description", edDescription.getText().toString());
                                                params.put("Question", edQuestion.getText().toString());
                                                params.put("Password", edPassword.getText().toString());
                                                params.put("Longitude", GPSTracer.longitude + "");
                                                params.put("Latitude", GPSTracer.latitude + "");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            NetworkSI network = new NetworkSI();
                                            network.request(DataSync.Command.SETGAME, params.toString(), new NetworkSI.AsyncResponse() {
                                                @Override
                                                public void onSuccess(String response) {
                                                    DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                                        @Override
                                                        public void onFinished(String response) {
                                                            Log.d("GameRoomData", response);

                                                            Intent intent = new Intent(MainView.mContext, GameRoomView.class);
                                                            MainView.mContext.startActivity(intent);
                                                            dismiss();

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

                                        }

                                        @Override
                                        public void onPreExcute() {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(String response) {

                            }
                        });

                    }

                };
    }
}
