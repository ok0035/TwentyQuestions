package graduateproject.com.twentyquestions.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by mapl0 on 2017-09-15.
 */

public class JudgeRightDialog extends Dialog implements BasicMethod {

    RelativeLayout rlParent, rlMain, rlTitle, rlQuestion, rlQuestionText,
            rlRemainder, rlJudgeRight, rlButton, rlRight, rlEnd;

    LinearLayout llGameInfo, llRemainder, llRemainderQuestion, llRemainderRight, llRight, llProgress,
            llProgressText, llGameStatus, llButton, llButtonRow1, llButtonRow2;

    TextView tvQuestion, tvQuestionTitle, tvRemainderQuestion, tvRemainderQuestionCount,
            tvRemainderRight, tvRemainderRightCount, tvProgress, tvProgressCount, tvGameStatus,
            tvIsRight, tvGuessRight,  tvRightButton, tvCancelButton, tvTitle, tvDescription,
            tvRightAnswer, tvWrongAnswer, tvSimilarAnswer, tvNoSelect;

    EditText edChat;
    private View.OnClickListener cancelEvent;
    private View.OnClickListener okEvent;
    private DBSI dbsi;
    private String guessObj;

    public JudgeRightDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setValues();
        setUpEvents();
        setView();
        setContentView(rlParent);
    }

    @Override
    public void setValues() {
        dbsi = new DBSI();
        guessObj = dbsi.selectQuery("Select Guess from RightAnswerList Order by PKey desc")[0][0];
    }

    @Override
    public void setUpEvents() {
        cancelEvent = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };

        okEvent = new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                    String twentyquestionsPKey = dbsi.selectQuery("Select PKey from TwentyQuestions")[0][0];
                    String userPKey = dbsi.selectQuery("Select PKey from User where MySelf = 0")[0][0];
                    String[][] findlocalRightAnswerList_Ans = dbsi.selectQuery("select * from RightAnswerList where TwentyQuestionsPKey = " + twentyquestionsPKey + " and Answerer = " + userPKey);

                    JSONObject data = new JSONObject();

                    try {
                        data.put("MemberPriority","0");
                        data.put("ChatRoomPKey",dbsi.selectQuery("select ChatRoomPKey from GameList")[0][0]);
                        data.put("RightAnswerListPKey",findlocalRightAnswerList_Ans[findlocalRightAnswerList_Ans.length-1][0]);
                        if(view.getId() == 0){ // 땡
                            data.put("Answer","Wrong");
                        }else if(view.getId() == 1){ // 아쉽
                            data.put("Answer","Similar");
                        }else{
                            data.put("Answer","Right");
                        }
                        data.put("GameListPKey",dbsi.selectQuery("select PKey from GameList")[0][0]);
                        data.put("Guesser",dbsi.selectQuery("select Guesser from RightAnswerList Order By UpdatedDate Desc")[0][0]);
                        data.put("MaxGuessable",dbsi.selectQuery("select MaxGuessable from TwentyQuestions")[0][0]);


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
                                        gameRoomView.makeChat(beforeSendLastChatDate[0]);
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
                                Toast.makeText(getOwnerActivity(), response, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }


        };
    }

    @Override
    public void bindView() {

    }

    public void setView() {



        rlParent = new RelativeLayout(BaseActivity.mContext);
        rlMain = new RelativeLayout(BaseActivity.mContext);
        rlTitle = new RelativeLayout(BaseActivity.mContext);
        rlQuestion = new RelativeLayout(BaseActivity.mContext);
        rlQuestionText = new RelativeLayout(BaseActivity.mContext);
        rlRight = new RelativeLayout(BaseActivity.mContext);
        rlRemainder = new RelativeLayout(BaseActivity.mContext);
        rlJudgeRight = new RelativeLayout(BaseActivity.mContext);
        rlButton = new RelativeLayout(BaseActivity.mContext);
        rlEnd = new RelativeLayout(BaseActivity.mContext);

        llGameInfo = new LinearLayout(BaseActivity.mContext);
        llRemainder = new LinearLayout(BaseActivity.mContext);
        llRemainderQuestion = new LinearLayout(BaseActivity.mContext);
        llRemainderRight = new LinearLayout(BaseActivity.mContext);
        llRight = new LinearLayout(BaseActivity.mContext);
        llProgress = new LinearLayout(BaseActivity.mContext);
        llProgressText = new LinearLayout(BaseActivity.mContext);
        llGameStatus = new LinearLayout(BaseActivity.mContext);
        llButton = new LinearLayout(BaseActivity.mContext);
        llButtonRow1 = new LinearLayout(BaseActivity.mContext);
        llButtonRow2 = new LinearLayout(BaseActivity.mContext);

        tvQuestion = new TextView(BaseActivity.mContext);
        tvQuestionTitle = new TextView(BaseActivity.mContext);
        tvRemainderQuestion = new TextView(BaseActivity.mContext);
        tvRemainderQuestionCount = new TextView(BaseActivity.mContext);
        tvRemainderRight = new TextView(BaseActivity.mContext);
        tvRemainderRightCount = new TextView(BaseActivity.mContext);
        tvProgress = new TextView(BaseActivity.mContext);
        tvProgressCount = new TextView(BaseActivity.mContext);
        tvGameStatus = new TextView(BaseActivity.mContext);
        tvIsRight = new TextView(BaseActivity.mContext);
        tvGuessRight = new TextView(BaseActivity.mContext);
        tvRightButton = new TextView(BaseActivity.mContext);
        tvCancelButton = new TextView(BaseActivity.mContext);
        tvTitle = new TextView(BaseActivity.mContext);
        tvDescription = new TextView(BaseActivity.mContext);
        tvRightAnswer = new TextView(BaseActivity.mContext);
        tvWrongAnswer = new TextView(BaseActivity.mContext);
        tvNoSelect = new TextView(BaseActivity.mContext);
        tvSimilarAnswer = new TextView(BaseActivity.mContext);

        edChat = new EditText(BaseActivity.mContext);

        tvTitle.setLayoutParams(new RelativeLayout.LayoutParams((int) CalculatePixel.calculatePixelX(80), (int) CalculatePixel.calculatePixelY(30)));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextColor(Color.rgb(255,255,255));
        tvTitle.setText("정답이 \n올라왔어요");

        RelativeLayout.LayoutParams rlTitleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlTitleParams.setMargins(0,(int) CalculatePixel.calculatePixelY(5),0,0);
        rlTitleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlTitle.setLayoutParams(rlTitleParams);
        rlTitle.setBackgroundResource(R.color.colorPrimaryDark);
        rlTitle.addView(tvTitle);

        tvCancelButton.setLayoutParams(new LinearLayout.LayoutParams(0, (int) CalculatePixel.calculatePixelY(25), 0.3f));
        tvCancelButton.setGravity(Gravity.CENTER);
        tvCancelButton.setBackgroundResource(R.color.colorPrimaryDark);
        tvCancelButton.setTextColor(Color.rgb(255,255,255));
        tvCancelButton.setTextSize(CalculatePixel.calculatePixelY(5));
        tvCancelButton.setText("선택하지 않고\n 돌아가기");
        tvCancelButton.setOnClickListener(cancelEvent);

        LinearLayout.LayoutParams llCancelRightParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llCancelRightParams.setMargins(0, 0, (int) CalculatePixel.calculatePixelX(5), 0);
        llButtonRow2.setLayoutParams(llCancelRightParams);
        llButtonRow2.setGravity(Gravity.CENTER);
        llButtonRow2.setWeightSum(1);
//        llButtonRow2.setBackgroundResource(R.color.colorPrimary);
        llButtonRow2.addView(tvCancelButton);

        LinearLayout.LayoutParams tvRightAnswerParams = new LinearLayout.LayoutParams(0, (int) CalculatePixel.calculatePixelY(25), 0.3f);
//        tvRightAnswerParams.setMargins(0,0,(int)CalculatePixel.calculatePixelX(5), 0);
        tvRightAnswer.setLayoutParams(tvRightAnswerParams);
        tvRightAnswer.setGravity(Gravity.CENTER);
        tvRightAnswer.setTextSize(CalculatePixel.calculatePixelY(5));
        tvRightAnswer.setTextColor(Color.rgb(255,255,255));
        tvRightAnswer.setBackgroundResource(R.color.colorPrimaryDark);
        tvRightAnswer.setText("정답!");

        LinearLayout.LayoutParams tvWrongAnswerParams = new LinearLayout.LayoutParams(0, (int) CalculatePixel.calculatePixelY(25), 0.2f);
        tvWrongAnswerParams.setMargins(0,0,(int) CalculatePixel.calculatePixelX(5), 0);
        tvWrongAnswer.setLayoutParams(tvWrongAnswerParams);
        tvWrongAnswer.setTextSize(CalculatePixel.calculatePixelY(6));
        tvWrongAnswer.setBackgroundResource(R.color.colorPrimaryDark);
        tvWrongAnswer.setTextColor(Color.rgb(255,255,255));
        tvWrongAnswer.setGravity(Gravity.CENTER);
        tvWrongAnswer.setText("땡!");
        tvWrongAnswer.setId(0);
        tvWrongAnswer.setOnClickListener(okEvent);


        LinearLayout.LayoutParams tvSimilarAnswerParams = new LinearLayout.LayoutParams(0, (int) CalculatePixel.calculatePixelY(25), 0.5f);
        tvSimilarAnswerParams.setMargins(0,0,(int) CalculatePixel.calculatePixelX(5), 0);
        tvSimilarAnswer.setLayoutParams(tvSimilarAnswerParams);
        tvSimilarAnswer.setTextSize(CalculatePixel.calculatePixelY(5));
        tvSimilarAnswer.setTextColor(Color.rgb(255,255,255));
        tvSimilarAnswer.setBackgroundResource(R.color.colorPrimaryDark);
        tvSimilarAnswer.setGravity(Gravity.CENTER);
        tvSimilarAnswer.setText("땡!\n아쉬웠어요!");
        tvSimilarAnswer.setId(1);
        tvSimilarAnswer.setOnClickListener(okEvent);

        LinearLayout.LayoutParams llButtonRow1Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llButtonRow1Params.setMargins((int) CalculatePixel.calculatePixelX(5), 0, 0, (int) CalculatePixel.calculatePixelY(5));
        llButtonRow1.setOrientation(LinearLayout.HORIZONTAL);
        llButtonRow1.setLayoutParams(llButtonRow1Params);
        llButtonRow1.setWeightSum(1);
        llButtonRow1.setGravity(Gravity.CENTER);
//        llButtonRow1.setBackgroundResource(R.color.colorPrimary);
        llButtonRow1.addView(tvRightButton);
        llButtonRow1.addView(tvWrongAnswer);
        llButtonRow1.addView(tvSimilarAnswer);

        llButton.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llButton.setOrientation(LinearLayout.VERTICAL);
        llButton.setWeightSum(1);
        llButton.addView(llButtonRow1);
        llButton.addView(llButtonRow2);

        RelativeLayout.LayoutParams rlButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlButtonParams.setMargins(0,(int) CalculatePixel.calculatePixelY(270), 0, (int) CalculatePixel.calculatePixelY(5));
        rlButton.setLayoutParams(rlButtonParams);
//        rlButton.setY();
        rlButton.addView(llButton);

        tvIsRight.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvIsRight.setTextSize(CalculatePixel.calculatePixelY(5));
        tvIsRight.setTextColor(Color.rgb(255,255,255));
        tvIsRight.setText("정오답 처리 : ");
        tvIsRight.setId(2);
        tvIsRight.setOnClickListener(okEvent);

        tvGuessRight.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvGuessRight.setTextSize(CalculatePixel.calculatePixelY(5));
        tvGuessRight.setTextColor(Color.rgb(255,255,255));
        tvGuessRight.setText(guessObj);

        llRight.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) CalculatePixel.calculatePixelY(30)));
        llRight.setOrientation(LinearLayout.HORIZONTAL);
        llRight.setGravity(Gravity.CENTER);
        llRight.setBackgroundResource(R.color.colorPrimaryDark);
        llRight.addView(tvIsRight);
        llRight.addView(tvGuessRight);

        RelativeLayout.LayoutParams tvDescriptionParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvDescriptionParams.setMargins(0,(int) CalculatePixel.calculatePixelY(35), 0, 0);
        tvDescription.setLayoutParams(tvDescriptionParams);
        tvDescription.setGravity(Gravity.CENTER);
        tvDescription.setTextSize(CalculatePixel.calculatePixelY(5));
        tvDescription.setTextColor(Color.rgb(255,255,255));
        tvDescription.setText("정오답 처리에 대해서는\n아래 버튼 중 선택해 주세요.");

        RelativeLayout.LayoutParams rlJudgeRightParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlJudgeRightParams.setMargins((int) CalculatePixel.calculatePixelX(35), 0, (int) CalculatePixel.calculatePixelX(35), 0);
        rlJudgeRightParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlJudgeRight.setGravity(Gravity.CENTER);
        rlJudgeRight.setLayoutParams(rlJudgeRightParams);
        rlJudgeRight.setY(CalculatePixel.calculatePixelY(200));
        rlJudgeRight.addView(llRight);
        rlJudgeRight.addView(tvDescription);


        tvGameStatus.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvGameStatus.setTextColor(Color.rgb(255,255,255));
        tvGameStatus.setTextSize(CalculatePixel.calculatePixelY(5));
        tvGameStatus.setText("질문 완료");

        llGameStatus.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
        llGameStatus.setOrientation(LinearLayout.HORIZONTAL);
        llGameStatus.setGravity(Gravity.RIGHT);
        llGameStatus.addView(tvGameStatus);

        tvProgress.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvProgress.setTextColor(Color.rgb(255,255,255));
        tvProgress.setTextSize(CalculatePixel.calculatePixelY(5));
        tvProgress.setText("진행상황 : ");

        tvProgressCount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvProgressCount.setTextColor(Color.rgb(255,255,255));
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
        tvRemainderRight.setTextColor(Color.rgb(255,255,255));
        tvRemainderRight.setTextSize(CalculatePixel.calculatePixelY(5));
        tvRemainderRight.setText("남은 정답수 : ");

        tvRemainderRightCount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvRemainderRightCount.setTextColor(Color.rgb(255,255,255));
        tvRemainderRightCount.setTextSize(CalculatePixel.calculatePixelY(5));
        tvRemainderRightCount.setText("0");

        llRemainderRight.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
        llRemainderRight.setOrientation(LinearLayout.HORIZONTAL);
        llRemainderRight.setGravity(Gravity.RIGHT);
        llRemainderRight.addView(tvRemainderRight);
        llRemainderRight.addView(tvRemainderRightCount);

        tvRemainderQuestion.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvRemainderQuestion.setTextColor(Color.rgb(255,255,255));
        tvRemainderQuestion.setTextSize(CalculatePixel.calculatePixelY(5));
        tvRemainderQuestion.setText("남은 질문수 : ");

        tvRemainderQuestionCount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvRemainderQuestionCount.setTextColor(Color.rgb(255,255,255));
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
        tvQuestion.setText(dbsi.selectQuery("Select Object From TwentyQuestions")[0][0]);

        RelativeLayout.LayoutParams rlQuestionParams = new RelativeLayout.LayoutParams((int) CalculatePixel.calculatePixelX(120), (int) CalculatePixel.calculatePixelY(70));
//        rlQuestionParams.setMargins(0,0,0,(int)CalculatePixel.calculatePixelY(55));
        rlQuestionParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlQuestion.setY(CalculatePixel.calculatePixelY(55));
        rlQuestion.setLayoutParams(rlQuestionParams);
        rlQuestion.setBackgroundColor(Color.rgb(255,255,255));
        rlQuestion.addView(tvQuestion);

//        RelativeLayout.LayoutParams rlEndParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rlEndParams.setMargins(0, (int)CalculatePixel.calculatePixelY(300), 0, 0);

        RelativeLayout.LayoutParams rlMainParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rlMain.setY(CalculatePixel.calculatePixelY(10));
        rlMain.setLayoutParams(rlMainParams);
        rlMain.setBackgroundResource(R.color.colorAccent);
        rlMain.addView(rlQuestion);
        rlMain.addView(rlQuestionText);
        rlMain.addView(rlRemainder);
        rlMain.addView(rlJudgeRight);
        rlMain.addView(rlButton);




        rlParent.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rlParent.setBackgroundResource(R.color.colorPrimary);
        rlParent.addView(rlMain);
        rlParent.addView(rlTitle);

    }
}
