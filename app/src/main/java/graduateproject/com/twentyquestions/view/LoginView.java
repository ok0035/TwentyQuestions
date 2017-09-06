package graduateproject.com.twentyquestions.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.controller.LoginController;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;

import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelY;

/**
 * Created by yrs00 on 2017-08-04.
 */

public class LoginView extends BaseActivity {

    RelativeLayout parentLayout;
    EditText IdEditText;
    EditText PwdEditText;
    TextView LoginBtn;
    TextView LoginByGoogleBtn;
    TextView LoginByFacebookBtn;
    TextView RegistBtn;

    View.OnClickListener login;
    View.OnClickListener loginByGoogle;
    View.OnClickListener loginByFacebook;
    View.OnClickListener regist;

    public LoginView() {
        mContext = this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpEvents();
        setView();
        setContentView(parentLayout);


    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
        regist = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegisterView.class);
                startActivity(intent);
                finish();
            }
        };

        login = new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (IdEditText.getText().toString().length() < 8) {
                    Toast.makeText(LoginView.this, "ID를 입력해주십시오.", Toast.LENGTH_SHORT).show();
                } else {

                    JSONObject data = new JSONObject();
                    try {
                        data.put("UserPKey","1");
                        data.put("UserID","admin");
                        data.put("LoginType","0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NetworkSI networkSI = new NetworkSI();
                    networkSI.request(DataSync.Command.TRYLOGIN, data.toString(), new NetworkSI.AsyncResponse() {
                        @Override
                        public void onSuccess(String response) {
//                            Log.d("Login_Response",response);
                            LoginController loginController = new LoginController();
                            if(loginController.parseLoginData(response)) {
                                Intent intent = new Intent(mContext, MainView.class);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(String response) {

                        }
                    });

//                    Intent intent = new Intent(mContext, MainView.class);
//                    startActivity(intent);
//                    finish();
                }

            }
        };
    }

    @Override
    public void setView() {
        super.setView();
        parentLayout = new RelativeLayout(mContext);
        parentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        IdEditText = new EditText(mContext);
        IdEditText.setHint("ID");
        IdEditText.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        IdEditText.setY(calculatePixelY(20));

        PwdEditText = new EditText(mContext);
        PwdEditText.setHint("비밀번호");
        PwdEditText.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        PwdEditText.setY(calculatePixelY(50));

        LoginBtn = new TextView(this);
        LoginBtn.setText("로그인");
        LoginBtn.setTextColor(Color.WHITE);
        LoginBtn.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LoginBtn.setY(calculatePixelY(80));
        LoginBtn.setBackgroundColor(Color.GRAY);
        LoginBtn.setGravity(Gravity.CENTER);
        LoginBtn.setOnClickListener(login);

        LoginByGoogleBtn = new TextView(this);
        LoginByGoogleBtn.setText("Google+");
        LoginByGoogleBtn.setTextColor(Color.WHITE);
        LoginByGoogleBtn.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LoginByGoogleBtn.setY(calculatePixelY(110));
        LoginByGoogleBtn.setBackgroundColor(Color.MAGENTA);
        LoginByGoogleBtn.setGravity(Gravity.CENTER);

        LoginByFacebookBtn = new TextView(this);
        LoginByFacebookBtn.setText("FaceBook");
        LoginByFacebookBtn.setTextColor(Color.WHITE);
        LoginByFacebookBtn.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LoginByFacebookBtn.setY(calculatePixelY(140));
        LoginByFacebookBtn.setBackgroundColor(Color.BLUE);
        LoginByFacebookBtn.setGravity(Gravity.CENTER);

        RegistBtn = new TextView(this);
        RegistBtn.setText("회원가입");
        RegistBtn.setTextColor(Color.WHITE);
        RegistBtn.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RegistBtn.setY(calculatePixelY(170));
        RegistBtn.setBackgroundColor(Color.BLACK);
        RegistBtn.setGravity(Gravity.CENTER);
        RegistBtn.setOnClickListener(regist);

        parentLayout.addView(IdEditText);
        parentLayout.addView(PwdEditText);
        parentLayout.addView(LoginBtn);
        parentLayout.addView(LoginByGoogleBtn);
        parentLayout.addView(LoginByFacebookBtn);
        parentLayout.addView(RegistBtn);

    }

}
