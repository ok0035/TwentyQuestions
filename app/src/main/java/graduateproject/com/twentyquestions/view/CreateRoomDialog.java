package graduateproject.com.twentyquestions.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.BasicMethod;
import graduateproject.com.twentyquestions.util.GPSTracer;

import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelX;
import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelY;

/**
 * Created by Heronation on 2017-08-21.
 */

public class CreateRoomDialog extends Dialog implements BasicMethod {

    RelativeLayout parentLayout;
    LinearLayout llBorder, llRoomName, llDescription, llQuestion, llPassword, llButton, llExample;
    TextView tvRoomName, tvDescription, tvQuestion, tvPassword, tvExample, btnStartGame, btnCancel;
    EditText edRoomName, edQuestion, edPassword, edDescription;

    View.OnClickListener clickStartGame, clickCancel;

    public CreateRoomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setUpEvents();
        setView();
        setContentView(parentLayout);

    }

    public void setRoomName(String name) {

        edRoomName.setText(name);

    }

    public void setQuestion(String question) {

        edQuestion.setText(question);
    }

    public void setPassword(String password) {

        edPassword.setText(password);

    }

    public String getRoomName() {

        return edRoomName.getText().toString();
    }

    public String getQuestion() {

        return edQuestion.getText().toString();
    }

    public String getPassword() {

        return edPassword.getText().toString();
    }

    @Override
    public void setValues() {

    }

    @Override
    public void setUpEvents() {

        clickStartGame = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                String response = network.request(DataSync.Command.SETGAME, params.toString(), new NetworkSI.AsyncResponse() {
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
        };

        clickCancel = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };

    }

    public void setView() {

        tvRoomName = new TextView(MainView.mContext);
        tvRoomName.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.7f));
        tvRoomName.setText("방 이 름 : ");

        tvDescription = new TextView(MainView.mContext);
        tvDescription.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.7f));
        tvDescription.setText("설    명: ");

        tvQuestion = new TextView(MainView.mContext);
        tvQuestion.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.7f));
        tvQuestion.setText("문    제 : ");

        tvPassword = new TextView(MainView.mContext);
        tvPassword.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.7f));
        tvPassword.setText("비밀번호 : ");

        edRoomName = new EditText(MainView.mContext);
        edRoomName.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        edDescription = new EditText(MainView.mContext);
        edDescription.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        edQuestion = new EditText(MainView.mContext);
        edQuestion.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        edPassword = new EditText(MainView.mContext);
        edPassword.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        llRoomName = new LinearLayout(MainView.mContext);
        llRoomName.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llRoomNameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llRoomNameParams.setMargins((int)calculatePixelX(10),(int)calculatePixelY(10),(int)calculatePixelX(10),(int)calculatePixelY(10));
        llRoomName.setLayoutParams(llRoomNameParams);
        llRoomName.addView(tvRoomName);
        llRoomName.addView(edRoomName);

        llDescription = new LinearLayout(MainView.mContext);
        llDescription.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llDescriptionParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llRoomNameParams.setMargins((int)calculatePixelX(10),(int)calculatePixelY(10),(int)calculatePixelX(10),(int)calculatePixelY(10));
        llDescription.setLayoutParams(llDescriptionParams);
        llDescription.addView(tvDescription);
        llDescription.addView(edDescription);

        llQuestion = new LinearLayout(MainView.mContext);
        llQuestion.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llQuestionParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llQuestionParams.setMargins((int)calculatePixelX(10),(int)calculatePixelY(10),(int)calculatePixelX(10),(int)calculatePixelY(10));
        llQuestion.setLayoutParams(llQuestionParams);
        llQuestion.addView(tvQuestion);
        llQuestion.addView(edQuestion);

        llPassword = new LinearLayout(MainView.mContext);
        llPassword.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llPasswordParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llPasswordParams.setMargins((int)calculatePixelX(10),(int)calculatePixelY(10),(int)calculatePixelX(10),(int)calculatePixelY(20));
        llPassword.setLayoutParams(llPasswordParams);
        llPassword.addView(tvPassword);
        llPassword.addView(edPassword);

        tvExample = new TextView(MainView.mContext);
        tvExample.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvExample.setText("지나치게 어려운 문제는 게임의\n" + "재미를 오히려 떨어뜨립니다.\n" +
                "세세한 것보다 큰 카테고리를 문제로 내주세요.\n\n" +
                "예) 씨없는 수박 (X) 수박(O)\n" +
                "      드럼 세탁기 (X) 세탁기 (O)\n");

        llExample = new LinearLayout(MainView.mContext);
        LinearLayout.LayoutParams llExampleParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llDescriptionParams.setMargins((int)calculatePixelX(20),0,0,(int)calculatePixelY(20));
        llExample.setLayoutParams(llExampleParams);
        llExample.addView(tvExample);

        btnStartGame = new TextView(MainView.mContext);
        btnStartGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        btnStartGame.setText("게임 시작");
        btnStartGame.setGravity(Gravity.CENTER);
        btnStartGame.setOnClickListener(clickStartGame);

        btnCancel = new TextView(MainView.mContext);
        btnCancel.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        btnCancel.setText("취소");
        btnCancel.setGravity(Gravity.CENTER);
        btnCancel.setOnClickListener(clickCancel);

        llButton = new LinearLayout(MainView.mContext);
        llButton.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams llButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llButtonParams.setMargins((int)calculatePixelX(20),0,0,(int)calculatePixelY(20));
        llButton.setLayoutParams(llButtonParams);
        llButton.addView(btnStartGame);
        llButton.addView(btnCancel);

        llBorder = new LinearLayout(MainView.mContext);
        llBorder.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams llBorderParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llBorderParams.setMargins((int)calculatePixelX(20),(int)calculatePixelY(20),(int)calculatePixelX(20),(int)calculatePixelY(20));
        llBorder.setLayoutParams(llBorderParams);
        llBorder.addView(llRoomName);
        llBorder.addView(llDescription);
        llBorder.addView(llQuestion);
        llBorder.addView(llPassword);
        llBorder.addView(llExample);
        llBorder.addView(llButton);

        parentLayout = new RelativeLayout(MainView.mContext);
        parentLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parentLayout.addView(llBorder);

    }

}
