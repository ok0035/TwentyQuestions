package graduateproject.com.twentyquestions.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.BasicMethod;
import graduateproject.com.twentyquestions.util.CalculatePixel;

/**
 * Created by mapl0 on 2017-09-13.
 */

public class GuessRightDialog extends Dialog implements BasicMethod {

    RelativeLayout rlParent, rlMain, rlTitle, rlQuestion, rlQuestionText,
            rlRemainder, rlHint, rlChat;

    LinearLayout llGameInfo, llRemainder, llRemainderQuestion, llRemainderRight, llRight, llProgress,
            llProgressText, llGameStatus, llChat, llSendRight, llCancelRight;

    TextView tvQuestion, tvQuestionTitle, tvRemainderQuestion, tvRemainderQuestionCount,
            tvRemainderRight, tvRemainderRightCount, tvProgress, tvProgressCount, tvGameStatus,
            tvHint1, tvHint2, tvHint3, tvRightButton, tvCancelButton, tvTitle;

    EditText edChat;
    private View.OnClickListener cancelEvent;
    private View.OnClickListener okEvent;
    private DBSI dbsi;

    public GuessRightDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setValues();
        setUpEvents();
        setView();

        setContentView(rlParent);
    }

    @Override
    public void setValues() {
        dbsi = new DBSI();
    }

    @Override
    public void setUpEvents() {
        cancelEvent = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }

        };

        okEvent = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (edChat.getText().toString().length() != 0) {


                    JSONObject data = new JSONObject();
                    try {
                        data.put("GameListPKey", dbsi.selectQuery("Select PKey from GameList")[0][0]);
                        data.put("Guess", edChat.getText().toString());
                        data.put("MemberPriority", "1");
                        data.put("ChatRoomPKey", dbsi.selectQuery("select ChatRoomPKey from GameList")[0][0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final GameRoomView gameRoomView = (GameRoomView) GameRoomView.mContext;

                    if(gameRoomView!=null){
                        final String[] beforeSendLastChatDate = {null};

                        NetworkSI networkSI = new NetworkSI();
                        networkSI.request(DataSync.Command.SENDRA, data.toString(), new NetworkSI.AsyncResponse() {
                            @Override
                            public void onSuccess(String response) {
                                DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                    @Override
                                    public void onFinished(String response) {
                                        gameRoomView.testFunc(beforeSendLastChatDate[0]);
                                        gameRoomView.exchangeView();
                                        dismiss();
                                    }

                                    @Override
                                    public void onPreExcute() {
                                        beforeSendLastChatDate[0] = gameRoomView.findLastChatDate();
                                    }
                                });

                            }

                            @Override
                            public void onFailure(String response) {
                                Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
            }

        };
    }

    @Override
    public void setView() {

        rlParent = new RelativeLayout(BaseActivity.mContext);
        rlMain = new RelativeLayout(BaseActivity.mContext);
        rlTitle = new RelativeLayout(BaseActivity.mContext);
        rlQuestion = new RelativeLayout(BaseActivity.mContext);
        rlQuestionText = new RelativeLayout(BaseActivity.mContext);
        rlRemainder = new RelativeLayout(BaseActivity.mContext);
        rlHint = new RelativeLayout(BaseActivity.mContext);
        rlChat = new RelativeLayout(BaseActivity.mContext);

        llGameInfo = new LinearLayout(BaseActivity.mContext);
        llRemainder = new LinearLayout(BaseActivity.mContext);
        llRemainderQuestion = new LinearLayout(BaseActivity.mContext);
        llRemainderRight = new LinearLayout(BaseActivity.mContext);
        llRight = new LinearLayout(BaseActivity.mContext);
        llProgress = new LinearLayout(BaseActivity.mContext);
        llProgressText = new LinearLayout(BaseActivity.mContext);
        llGameStatus = new LinearLayout(BaseActivity.mContext);
        llChat = new LinearLayout(BaseActivity.mContext);
        llSendRight = new LinearLayout(BaseActivity.mContext);
        llCancelRight = new LinearLayout(BaseActivity.mContext);

        tvQuestion = new TextView(BaseActivity.mContext);
        tvQuestionTitle = new TextView(BaseActivity.mContext);
        tvRemainderQuestion = new TextView(BaseActivity.mContext);
        tvRemainderQuestionCount = new TextView(BaseActivity.mContext);
        tvRemainderRight = new TextView(BaseActivity.mContext);
        tvRemainderRightCount = new TextView(BaseActivity.mContext);
        tvProgress = new TextView(BaseActivity.mContext);
        tvProgressCount = new TextView(BaseActivity.mContext);
        tvGameStatus = new TextView(BaseActivity.mContext);
        tvHint1 = new TextView(BaseActivity.mContext);
        tvHint2 = new TextView(BaseActivity.mContext);
        tvHint3 = new TextView(BaseActivity.mContext);
        tvRightButton = new TextView(BaseActivity.mContext);
        tvCancelButton = new TextView(BaseActivity.mContext);
        tvTitle = new TextView(BaseActivity.mContext);

        edChat = new EditText(BaseActivity.mContext);

        tvTitle.setLayoutParams(new RelativeLayout.LayoutParams((int) CalculatePixel.calculatePixelX(80), (int) CalculatePixel.calculatePixelY(20)));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextColor(Color.rgb(255, 255, 255));
        tvTitle.setText("정답 맞히기");

        RelativeLayout.LayoutParams rlTitleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlTitleParams.setMargins(0, (int) CalculatePixel.calculatePixelY(5), 0, 0);
        rlTitleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlTitle.setLayoutParams(rlTitleParams);
        rlTitle.setBackgroundResource(R.color.colorPrimaryDark);
        rlTitle.addView(tvTitle);

        tvCancelButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvCancelButton.setGravity(Gravity.CENTER);
        tvCancelButton.setBackgroundResource(R.color.colorPrimary);
        tvCancelButton.setTextColor(Color.rgb(255, 255, 255));
        tvCancelButton.setText("취소");
        tvCancelButton.setOnClickListener(cancelEvent);

        tvRightButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvRightButton.setGravity(Gravity.CENTER);
        tvRightButton.setBackgroundResource(R.color.colorPrimary);
        tvRightButton.setTextColor(Color.rgb(255, 255, 255));
        tvRightButton.setText("정답");
        tvRightButton.setOnClickListener(okEvent);

        LinearLayout.LayoutParams llCancelRightParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.15f);
        llCancelRightParams.setMargins(0, 0, (int) CalculatePixel.calculatePixelX(5), 0);
        llCancelRight.setLayoutParams(llCancelRightParams);
        llCancelRight.setGravity(Gravity.CENTER);
        llCancelRight.setBackgroundResource(R.color.colorPrimary);
        llCancelRight.addView(tvCancelButton);

        LinearLayout.LayoutParams llSendRightParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.15f);
        llSendRightParams.setMargins(0, 0, (int) CalculatePixel.calculatePixelX(5), 0);
        llSendRight.setLayoutParams(llSendRightParams);
        llSendRight.setGravity(Gravity.CENTER);
        llSendRight.setBackgroundResource(R.color.colorPrimary);
        llSendRight.addView(tvRightButton);


        LinearLayout.LayoutParams edChatParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f);
        edChatParams.setMargins((int) CalculatePixel.calculatePixelX(5), 0, (int) CalculatePixel.calculatePixelX(5), 0);
        edChat.setLayoutParams(edChatParams);
        edChat.setBackgroundColor(Color.rgb(255, 255, 255));
        edChat.setSingleLine(true);

        llChat.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llChat.setOrientation(LinearLayout.HORIZONTAL);
        llChat.setWeightSum(1);
        llChat.addView(edChat);
        llChat.addView(llSendRight);
        llChat.addView(llCancelRight);

        RelativeLayout.LayoutParams rlChatParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlChatParams.setMargins(0, (int) CalculatePixel.calculatePixelY(300), 0, (int) CalculatePixel.calculatePixelY(5));
        rlChat.setLayoutParams(rlChatParams);
//        rlChat.setY();
        rlChat.addView(llChat);

        tvHint1.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvHint1.setTextSize(CalculatePixel.calculatePixelY(5));
        tvHint1.setTextColor(Color.rgb(255, 255, 255));
        tvHint1.setText("힌트 1. 정답 글자수 힌트열기");

        RelativeLayout.LayoutParams tvHint2Params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvHint2Params.setMargins(0, (int) CalculatePixel.calculatePixelY(25), 0, 0);
        tvHint2.setLayoutParams(tvHint2Params);
        tvHint2.setTextSize(CalculatePixel.calculatePixelY(5));
//        tvHint2.setY(CalculatePixel.calculatePixelY(25));
        tvHint2.setTextColor(Color.rgb(255, 255, 255));
        tvHint2.setText("힌트 2. 정답 앞글자 힌트열기");

        RelativeLayout.LayoutParams tvHint3Params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvHint3Params.setMargins(0, (int) CalculatePixel.calculatePixelY(50), 0, 0);
//        tvHint3.setY(CalculatePixel.calculatePixelY(50));
        tvHint3.setLayoutParams(tvHint3Params);
        tvHint3.setTextSize(CalculatePixel.calculatePixelY(5));
        tvHint3.setTextColor(Color.rgb(255, 255, 255));
        tvHint3.setText("힌트 3. 정답 확인하기");

        RelativeLayout.LayoutParams rlHintParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlHintParams.setMargins((int) CalculatePixel.calculatePixelX(35), 0, (int) CalculatePixel.calculatePixelX(35), 0);
        rlHint.setLayoutParams(rlHintParams);
        rlHint.setY(CalculatePixel.calculatePixelY(200));
        rlHint.addView(tvHint1);
        rlHint.addView(tvHint2);
        rlHint.addView(tvHint3);

        tvGameStatus.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvGameStatus.setTextColor(Color.rgb(255, 255, 255));
        tvGameStatus.setTextSize(CalculatePixel.calculatePixelY(5));
        tvGameStatus.setText("질문 완료");

        llGameStatus.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
        llGameStatus.setOrientation(LinearLayout.HORIZONTAL);
        llGameStatus.setGravity(Gravity.RIGHT);
        llGameStatus.addView(tvGameStatus);

        tvProgress.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvProgress.setTextColor(Color.rgb(255, 255, 255));
        tvProgress.setTextSize(CalculatePixel.calculatePixelY(5));
        tvProgress.setText("진행상황 : ");

        tvProgressCount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvProgressCount.setTextColor(Color.rgb(255, 255, 255));
        tvProgressCount.setTextSize(CalculatePixel.calculatePixelY(5));
        tvProgressCount.setText("0 개");

        llProgressText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
        llProgressText.setOrientation(LinearLayout.HORIZONTAL);
        llProgressText.setGravity(Gravity.LEFT);
        llProgressText.addView(tvProgress);
        llProgressText.addView(tvProgressCount);

        llProgress.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llProgress.setOrientation(LinearLayout.HORIZONTAL);
        llProgress.setGravity(Gravity.CENTER);
        llProgress.setWeightSum(1);
        llProgress.addView(llProgressText);
        llProgress.addView(llGameStatus);

        tvRemainderRight.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvRemainderRight.setTextColor(Color.rgb(255, 255, 255));
        tvRemainderRight.setTextSize(CalculatePixel.calculatePixelY(5));
        tvRemainderRight.setText("남은 정답수 : ");

        tvRemainderRightCount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvRemainderRightCount.setTextColor(Color.rgb(255, 255, 255));
        tvRemainderRightCount.setTextSize(CalculatePixel.calculatePixelY(5));
        tvRemainderRightCount.setText("0");

        llRemainderRight.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
        llRemainderRight.setOrientation(LinearLayout.HORIZONTAL);
        llRemainderRight.setGravity(Gravity.RIGHT);
        llRemainderRight.addView(tvRemainderRight);
        llRemainderRight.addView(tvRemainderRightCount);

        tvRemainderQuestion.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvRemainderQuestion.setTextColor(Color.rgb(255, 255, 255));
        tvRemainderQuestion.setTextSize(CalculatePixel.calculatePixelY(5));
        tvRemainderQuestion.setText("남은 질문수 : ");

        tvRemainderQuestionCount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvRemainderQuestionCount.setTextColor(Color.rgb(255, 255, 255));
        tvRemainderQuestionCount.setTextSize(CalculatePixel.calculatePixelY(5));
        tvRemainderQuestionCount.setText("0");

        llRemainderQuestion.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
        llRemainderQuestion.setOrientation(LinearLayout.HORIZONTAL);
        llRemainderQuestion.setGravity(Gravity.LEFT);
        llRemainderQuestion.addView(tvRemainderQuestion);
        llRemainderQuestion.addView(tvRemainderQuestionCount);

        llRemainder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llRemainder.setOrientation(LinearLayout.HORIZONTAL);
        llRemainder.setWeightSum(1);
        llRemainder.addView(llRemainderQuestion);
        llRemainder.addView(llRemainderRight);

        llGameInfo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llGameInfo.setOrientation(LinearLayout.VERTICAL);
        llGameInfo.addView(llRemainder);
        llGameInfo.addView(llProgress);

        RelativeLayout.LayoutParams rlRemainderParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlRemainderParams.setMargins((int) CalculatePixel.calculatePixelX(20), 0, (int) CalculatePixel.calculatePixelX(20), 0);
        rlRemainder.setLayoutParams(rlRemainderParams);
        rlRemainder.setY(CalculatePixel.calculatePixelY(150));
        rlRemainder.addView(llGameInfo);

        tvQuestionTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvQuestionTitle.setGravity(Gravity.CENTER);
        tvQuestionTitle.setTextColor(Color.rgb(255, 255, 255));
        tvQuestionTitle.setText("문제");

        RelativeLayout.LayoutParams rlQuestionTextParams = new RelativeLayout.LayoutParams((int) CalculatePixel.calculatePixelX(80), (int) CalculatePixel.calculatePixelY(20));
        rlQuestionTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlQuestionText.setLayoutParams(rlQuestionTextParams);
        rlQuestionText.setY(CalculatePixel.calculatePixelY(40));
        rlQuestionText.setBackgroundResource(R.color.colorPrimaryDark);
        rlQuestionText.addView(tvQuestionTitle);

        tvQuestion.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvQuestion.setGravity(Gravity.CENTER);
        tvQuestion.setTextSize(CalculatePixel.calculatePixelY(20));
        tvQuestion.setText(" ??? ");

        RelativeLayout.LayoutParams rlQuestionParams = new RelativeLayout.LayoutParams((int) CalculatePixel.calculatePixelX(120), (int) CalculatePixel.calculatePixelY(70));
//        rlQuestionParams.setMargins(0,0,0,(int)CalculatePixel.calculatePixelY(55));
        rlQuestionParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlQuestion.setY(CalculatePixel.calculatePixelY(55));
        rlQuestion.setLayoutParams(rlQuestionParams);
        rlQuestion.setBackgroundColor(Color.rgb(255, 255, 255));
        rlQuestion.addView(tvQuestion);

        RelativeLayout.LayoutParams rlMainParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rlMain.setY(CalculatePixel.calculatePixelY(10));
        rlMain.setLayoutParams(rlMainParams);
        rlMain.setBackgroundResource(R.color.colorAccent);
        rlMain.addView(rlQuestion);
        rlMain.addView(rlQuestionText);
        rlMain.addView(rlRemainder);
        rlMain.addView(rlHint);
        rlMain.addView(rlChat);

        rlParent.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rlParent.setBackgroundResource(R.color.colorPrimary);
        rlParent.addView(rlMain);
        rlParent.addView(rlTitle);

    }
}
