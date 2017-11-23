package graduateproject.com.twentyquestions.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.GPSTracer;

/**
 * Created by Heronation on 2017-08-21.
 */

public class CreateRoom extends BaseActivity {

    RelativeLayout parentLayout;
    LinearLayout llBorder, llRoomName, llDescription, llQuestion, llPassword, llButton, llExample;
    TextView tvRoomName, tvDescription, tvQuestion, tvPassword, tvExample, btnCreateRoomStartGame, btnCreateRoomCancel;
    EditText edCreateRoomName, edCreateRoomQuestion, edCreateRoomPassword;

    public View.OnClickListener clickStartGame, clickCancel;
    private android.widget.ImageView ivBack;
    private TextView tvCreateRoomDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_room);

        hideActionBar();
        bindView();
        setUpEvents();
    }

    public void setRoomName(String name) {

        edCreateRoomName.setText(name);

    }

    public void setQuestion(String question) {

        edCreateRoomQuestion.setText(question);
    }

    public void setPassword(String password) {

        edCreateRoomPassword.setText(password);

    }

    public String getRoomName() {

        return edCreateRoomName.getText().toString();
    }

    public String getQuestion() {

        return edCreateRoomQuestion.getText().toString();
    }

    public String getPassword() {

        return edCreateRoomPassword.getText().toString();
    }

    @Override
    public void setValues() {
//        tvCreateRoomDesc.setText("지나치게 어려운 문제는\n게임의\n" + "재미를 오히려 떨어뜨립니다.\n" +
//                "세세한 것보다 큰 카테고리를 문제로 내주세요.\n\n" +
//                "예) 씨없는 수박 (X) 수박(O)\n" +
//                "    드럼 세탁기 (X) 세탁기 (O)\n");
    }

    @Override
    public void setUpEvents() {


        btnCreateRoomStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject params = new JSONObject();
                try {
                    params.put("RoomName", edCreateRoomName.getText().toString());
                    params.put("Description", "");
                    params.put("Question", edCreateRoomQuestion.getText().toString());
                    params.put("Password", edCreateRoomPassword.getText().toString());
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
                                finish();
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
        });

        btnCreateRoomCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void bindView() {
        this.btnCreateRoomCancel = (TextView) findViewById(R.id.btnCreateRoomCancel);
        this.btnCreateRoomStartGame = (TextView) findViewById(R.id.btnCreateRoomStartGame);
        this.edCreateRoomPassword = (EditText) findViewById(R.id.edCreateRoomPassword);
        this.edCreateRoomQuestion = (EditText) findViewById(R.id.edCreateRoomQuestion);
        this.edCreateRoomName = (EditText) findViewById(R.id.edCreateRoomName);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.tvCreateRoomDesc = (TextView) findViewById(R.id.tvCreateRoomDesc);
    }

}
