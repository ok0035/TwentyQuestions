package graduateproject.com.twentyquestions.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

/**
 * Created by yrs00 on 2017-08-03.
 */
public class RegisterView extends BaseActivity {

    private EditText edSignUpName;
    private TextView tvSignUpGirl;
    private TextView tvSignUpMan;
    private Spinner spSignUpAge;
    private TextView tvSignUpStart;
    private int sexFlag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        bindView();
        setValues();
        setUpEvents();
        setCustomActionBar();

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

        tvSignUpMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSignUpMan.setBackgroundResource(R.drawable.signup_checked);
                tvSignUpMan.setTextColor(ContextCompat.getColor(mContext, R.color.SignUpCheckedSexText));

                tvSignUpGirl.setBackgroundResource(R.drawable.signup_unchecked);
                tvSignUpGirl.setTextColor(ContextCompat.getColor(mContext, R.color.SignUpUnCheckedSexText));

                sexFlag = 0;
            }
        });

        tvSignUpGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvSignUpMan.setBackgroundResource(R.drawable.signup_unchecked);
                tvSignUpMan.setTextColor(ContextCompat.getColor(mContext, R.color.SignUpUnCheckedSexText));

                tvSignUpGirl.setBackgroundResource(R.drawable.signup_checked);
                tvSignUpGirl.setTextColor(ContextCompat.getColor(mContext, R.color.SignUpCheckedSexText));

                sexFlag = 1;

            }
        });

        tvSignUpStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nickName[0] = edSignUpName.getText().toString();
                gender[0] = sexFlag+"";
                age[0] = spSignUpAge.getSelectedItem().toString();

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
        });
    }

    @Override
    public void setView() {
        super.setView();

    }

    @Override
    public void bindView() {
        super.bindView();

        this.tvSignUpStart = (TextView) findViewById(R.id.tvSignUpStart);
        this.spSignUpAge = (Spinner) findViewById(R.id.spSignUpAge);
        this.tvSignUpMan = (TextView) findViewById(R.id.tvSignUpMan);
        this.tvSignUpGirl = (TextView) findViewById(R.id.tvSignUpGirl);
        this.edSignUpName = (EditText) findViewById(R.id.edSignUpName);
    }

    @Override
    public void setValues() {
        super.setValues();

        final String[] ageArray = new String[85];
        for (int i = 0; i < ageArray.length; i++) {
            ageArray[i] = (i + 15) + "";
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, ageArray);
        spSignUpAge.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @Override
    public void setCustomActionBar() {
        super.setCustomActionBar();
        titleView.setText("지근거리 스무고개");
        backView.setVisibility(GONE);

    }
}