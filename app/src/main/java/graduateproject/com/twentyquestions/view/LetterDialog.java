package graduateproject.com.twentyquestions.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.ArrayList;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.item.LetterDataItem;
import graduateproject.com.twentyquestions.network.HttpNetwork;
import graduateproject.com.twentyquestions.util.BasicMethod;
import graduateproject.com.twentyquestions.util.CalculatePixel;

/**
 * Created by yrs00 on 2017-10-19.
 */

public class LetterDialog extends Activity implements BasicMethod {

    /*
    * 사용법
    * Dialog를 상속받은게 아니라
    * 액티비티에다가 Theme을 Dialog 속성으로 준거라
    * 액티비티랑 취급이 같다. 즉 다이얼로그를 사용하고 싶으면
    * 인텐트로 호출하면된다.
    * 단, 인텐트로 호출할 시, 반드시
    * 쪽지의 flag값을 엑스트라로 추가해야 한다.
    * 해당 플래그 값은 아래 참조
    * */

    // 인텐트로 플래그 값들을 받아서 저장하고,
    // switch case를 이용해서 쪽지의 뷰들을 변경한다.
    // 1. 운영자 공지사항 : NOTICE
    // 2. 친구 신청 : REQUESTFRIEND
    // 3. 받은 쪽지 : RECEIVELETTER
    // 4. 쪽지 보내기 : SENDLETTER
    private LetterDataItem LetterData;
    private RelativeLayout rlParent;
    private TextView tvTitle;
    private TextView tvDateTime;
    private TextView tvContent;
    private EditText etMessage;
    private TextView tvLeftBtn;
    private TextView tvCenterBtn;
    private TextView tvRightBtn;
    private RelativeLayout rlBtnGroup;

    View.OnClickListener closeDialog; // 닫기, 취소, ok
    View.OnClickListener deleteDialog; // 삭제
    View.OnClickListener refuseRequest;// 거절
    View.OnClickListener acceptRequest;// 수락
    View.OnClickListener blockRequest; // 차단
    View.OnClickListener openSendLetter;// 답장
    View.OnClickListener sendLetter; // 보내기
    private TextView tvLetterDialogTitle;
    private TextView tvLetterDialogContent;
    private EditText etLetterDialogContent;
    private TextView tvLDLeftBtn;
    private TextView tvLDCenterBtn;
    private TextView tvLDRightBtn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.letter_dialog);
        setValues();
        setUpEvents();
        setView();
        exchangeViewByFlag();

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.7); //Display 사이즈의 70%

        int height = (int) (display.getHeight() * 0.5);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;

        getWindow().getAttributes().height = height;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("다이얼로그 ", "종료");
    }

    @Override
    public void setValues() {

        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("LetterData");
        //경고 메세지를 없에주는 어노테이션
        @SuppressWarnings("unchecked")
        ArrayList<LetterDataItem> list = (ArrayList<LetterDataItem>)serializable;
        LetterData = (LetterDataItem)list.get(0);

    }

    @Override
    public void setUpEvents() {
        // 닫기
        closeDialog = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };

        // 답장
        openSendLetter = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LetterDialog.class);
                intent.putExtra("letterFlag","3");
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    public void setView() {
        this.tvLDRightBtn = (TextView) findViewById(R.id.tvLDRightBtn);
        this.tvLDCenterBtn = (TextView) findViewById(R.id.tvLDCenterBtn);
        this.tvLDLeftBtn = (TextView) findViewById(R.id.tvLDLeftBtn);
        this.etLetterDialogContent = (EditText) findViewById(R.id.etLetterDialogContent);
        this.tvLetterDialogContent = (TextView) findViewById(R.id.tvLetterDialogContent);
        this.tvLetterDialogTitle = (TextView) findViewById(R.id.tvLetterDialogTitle);
    }

    private void exchangeViewByFlag(){

                switch (LetterData.getLetterType()){
                    case "Notice" :{ // 공지사항
                        tvLetterDialogTitle.setText("운영자 공지사항");
                        tvLDLeftBtn.setText("삭제");
                        tvLDCenterBtn.setVisibility(View.GONE);
                        tvLDRightBtn.setText("닫기");

                        tvLDRightBtn.setOnClickListener(closeDialog);
                    }break;
                    case "Friend" :{ // 친구신청 받음
                        tvLetterDialogTitle.setText("친구신청");
                        tvLetterDialogContent.setText(LetterData.getLetterContent());
                        tvLDLeftBtn.setText("거절");
                        tvLDCenterBtn.setText("수락");
                        tvLDRightBtn.setText("차단");
                    }break;
                    case "ReceiveLetter" :{ // 받은쪽지
                        tvLetterDialogTitle.setText("쪽지 (닉네임, 나이, 성별, km) ");
                        tvLDLeftBtn.setText("삭제");
                        tvLDCenterBtn.setText("OK");
                        tvLDRightBtn.setText("답장");

                        tvLDCenterBtn.setOnClickListener(closeDialog);
                        tvLDRightBtn.setOnClickListener(openSendLetter);

                    }break;
                    case "SendLetter" :{ // 쪽지 보내기
                        tvLetterDialogTitle.setText("쪽지 보내기");
                        tvLetterDialogContent.setVisibility(View.GONE);
                        etLetterDialogContent.setVisibility(View.VISIBLE);
                        tvLDLeftBtn.setText("보내기");
                        tvLDCenterBtn.setVisibility(View.GONE);
                        tvLDRightBtn.setText("취소");

                        tvLDRightBtn.setOnClickListener(closeDialog);

                    }break;
                }


    }
}
