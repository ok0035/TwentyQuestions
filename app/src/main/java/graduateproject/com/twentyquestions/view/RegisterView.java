package graduateproject.com.twentyquestions.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.controller.LoginController;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;

import static android.view.View.GONE;
import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelX;
import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelY;

/**
 * Created by yrs00 on 2017-08-03.
 */
public class RegisterView extends BaseActivity {

    RelativeLayout parentLayout;
    TextView NickNameLabel;
    EditText NickNameTextField;
    TextView GenderTextView;
    RadioGroup GenderRadioGroup;
    RadioButton maleButton;
    RadioButton femaleButton;
    TextView AgeTextView;
    Spinner AgeSpinner;
    TextView startGameButton;
    View.OnClickListener startGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpEvents();
        setView();
        setCustomActionBar();
        setContentView(parentLayout);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(mContext, LoginView.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
        final String[] nickName = new String[1];
        final String[] gender = new String[1];
        final String[] age= new String[1];
        startGame = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName[0] = NickNameTextField.getText().toString();
                gender[0] = GenderRadioGroup.getCheckedRadioButtonId()+"";
                age[0] = AgeSpinner.getSelectedItem().toString();

                Toast.makeText(RegisterView.this, nickName[0]+"/"+gender[0]+"/"+age[0], Toast.LENGTH_SHORT).show();

                JSONObject User = new JSONObject();
                try {
                    User.put("LoginType","0");
                    User.put("NickName",nickName[0]);
                    User.put("Gender",gender[0]);
                    User.put("Birthday",age[0]);
                    User.put("DeviceID", Build.ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                NetworkSI networkSI = new NetworkSI();
                String response = networkSI.request(DataSync.Command.TRYREGIST, User.toString(), new NetworkSI.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {

                        LoginController loginController = new LoginController();
//                loginController.parseLoginData(response);
                        if(loginController.parseLoginData(response)) {
//                    for(int i = 0 ; i < loginController.getParseList().size() ; i++){
//                        // getParseList는 이중 리스트 구조
//                        // 즉, List<BasicNameValuePair>를 가지고 있는 List
//                        Log.d(i+"번째 list","....");
//                        ArrayList<ArrayList<BasicNameValuePair>> doubleList = loginController.getParseList();
//                        for(int j = 0 ; j < doubleList .get(i).size() ; j++){
//                            ArrayList<BasicNameValuePair> pairList = doubleList.get(i);
//                            Log.d("KEY",pairList.get(j).getName());
//                            Log.d("Value",pairList.get(j).getValue());
//                        }
//                        // DBSI호출 후, query 실행//
//
//                    }
                            Intent intent = new Intent(mContext, MainView.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterView.this, "회원가입 도중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }

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
        parentLayout = new RelativeLayout(mContext);
        parentLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        NickNameLabel = new TextView(mContext);
        NickNameLabel.setText("닉네임");
        NickNameLabel.setTextSize(11);
        NickNameLabel.setWidth((int) calculatePixelX(30));
        NickNameLabel.setHeight((int) calculatePixelY(15));
        NickNameLabel.setX(calculatePixelX(10));
        NickNameLabel.setY(calculatePixelY(40));
        NickNameLabel.setGravity(Gravity.CENTER);

        NickNameTextField = new EditText(mContext);
        NickNameTextField.setHint("중복사용이 가능합니다.");
        NickNameTextField.setTextSize(11);
//        NickNameTextField.setWidth((int) calculatePixelX(200));
//        NickNameTextField.setHeight((int) calculatePixelY(15));
        NickNameTextField.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        NickNameTextField.setX(calculatePixelX(45));
        NickNameTextField.setY(calculatePixelY(40));

        GenderTextView = new TextView(mContext);
        GenderTextView.setText("성별");
        GenderTextView.setTextSize(11);
        GenderTextView.setWidth((int) calculatePixelX(30));
        GenderTextView.setHeight((int) calculatePixelY(15));
        GenderTextView.setX(calculatePixelX(10));
        GenderTextView.setY(calculatePixelY(70));
        GenderTextView.setGravity(Gravity.CENTER);

        GenderRadioGroup = new RadioGroup(mContext);
        GenderRadioGroup.setX(calculatePixelX(45));
        GenderRadioGroup.setY(calculatePixelY(70));
        GenderRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        GenderRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT

        ));

        maleButton = new RadioButton(mContext);
        maleButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        maleButton.setText("남성");
        maleButton.setTextSize(11);
        maleButton.setId(0);
        femaleButton = new RadioButton(mContext);
        femaleButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        femaleButton.setText("여성");
        femaleButton.setTextSize(11);
        femaleButton.setId(1);
        GenderRadioGroup.addView(maleButton);
        GenderRadioGroup.addView(femaleButton);


        AgeTextView = new TextView(mContext);
        AgeTextView.setText("나이");
        AgeTextView.setTextSize(11);
        AgeTextView.setWidth((int) calculatePixelX(30));
        AgeTextView.setHeight((int) calculatePixelY(15));
        AgeTextView.setX(calculatePixelX(10));
        AgeTextView.setY(calculatePixelY(110));
        AgeTextView.setGravity(Gravity.CENTER);

        AgeSpinner = new Spinner(mContext);
        AgeSpinner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AgeSpinner.setX(calculatePixelX(45));
        AgeSpinner.setY(calculatePixelY(110));

        final String[] ageArray = new String[85];
        for (int i = 0; i < ageArray.length; i++) {
            ageArray[i] = (i + 15) + "";
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, ageArray);


        AgeSpinner.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        startGameButton = new TextView(mContext);
        startGameButton.setText("게임 시작");
        startGameButton.setTextSize(14);
        startGameButton.setTextColor(Color.BLACK);
        startGameButton.setBackgroundColor(Color.YELLOW);
        startGameButton.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) calculatePixelY(40)));
        startGameButton.setY(calculatePixelY(350));
        startGameButton.setGravity(Gravity.CENTER);
        startGameButton.setOnClickListener(startGame);

        parentLayout.addView(NickNameLabel);
        parentLayout.addView(NickNameTextField);
        parentLayout.addView(GenderTextView);
        parentLayout.addView(GenderRadioGroup);
        parentLayout.addView(AgeTextView);
        parentLayout.addView(AgeSpinner);
        parentLayout.addView(startGameButton);
    }

    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        titleView.setText("지근거리 스무고개");
        backView.setVisibility(GONE);

    }
}