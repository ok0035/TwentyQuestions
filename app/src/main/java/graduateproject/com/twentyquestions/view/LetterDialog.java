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

import graduateproject.com.twentyquestions.R;
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
    // 3. 쪽지 보내기 : SENDLETTER
    // 4. 받은 쪽지 : RECEIVELETTER
    private String letterFlag;
    private String letterPKey;

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
        letterFlag = intent.getStringExtra("letterFlag");
        letterPKey = intent.getStringExtra("letterPKey");

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

//        rlParent = new RelativeLayout(BaseActivity.mContext);
//        tvTitle = new TextView(BaseActivity.mContext);
//        tvDateTime = new TextView(BaseActivity.mContext);
//        tvContent = new TextView(BaseActivity.mContext);
//        etMessage = new EditText(BaseActivity.mContext);
//
//        rlBtnGroup = new RelativeLayout(BaseActivity.mContext);
//        tvLeftBtn = new TextView(BaseActivity.mContext);
//        tvCenterBtn = new TextView(BaseActivity.mContext);
//        tvRightBtn = new TextView(BaseActivity.mContext);
//
//
//
////        tvTitle.setText("Title");
//        tvTitle.setText(letterFlag);
//        tvTitle.setWidth((int) (CalculatePixel.calculatePixelX(320)*0.7));
//        tvTitle.setHeight((int) (CalculatePixel.calculatePixelY(480)*0.5*0.1));
//        tvTitle.setX(0);
//        tvTitle.setY((float) (CalculatePixel.calculatePixelY(10)*0.5));
//        tvTitle.setGravity(Gravity.CENTER);
//
//        tvDateTime.setText("yyyy-mm-dd");
//        tvDateTime.setWidth((int) (CalculatePixel.calculatePixelX(320)*0.7));
//        tvDateTime.setHeight((int) (CalculatePixel.calculatePixelY(480)*0.5*0.1));
//        tvDateTime.setX(0);
//        tvDateTime.setY((float) ((CalculatePixel.calculatePixelY(10)*0.5) +(CalculatePixel.calculatePixelY(480)*0.5*0.1)));
//        tvDateTime.setGravity(Gravity.START);
//
//        tvContent.setText("Content");
//        tvContent.setBackgroundColor(Color.LTGRAY);
//        tvContent.setWidth((int) (CalculatePixel.calculatePixelX(320)*0.7));
//        tvContent.setHeight((int) (CalculatePixel.calculatePixelY(480)*0.5*0.6));
//        tvContent.setX(0);
//        tvContent.setY((float) (CalculatePixel.calculatePixelY(480)*0.5*0.1 +(CalculatePixel.calculatePixelY(50)*0.5)));
//        tvContent.setGravity(Gravity.CENTER);
//
//        etMessage.setHint("입력");
//        etMessage.setBackgroundColor(Color.LTGRAY);
//        etMessage.setWidth((int) (CalculatePixel.calculatePixelX(320)*0.7));
//        etMessage.setHeight((int) (CalculatePixel.calculatePixelY(480)*0.5*0.6));
//        etMessage.setX(0);
//        etMessage.setY((float) (CalculatePixel.calculatePixelY(480)*0.5*0.1 +(CalculatePixel.calculatePixelY(50)*0.5)));
//        etMessage.setGravity(Gravity.CENTER);
//        etMessage.setVisibility(View.GONE);
//
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (CalculatePixel.calculatePixelY(480)*0.2));
//        rlBtnGroup.setLayoutParams(params);
//        rlBtnGroup.setX(0);
//        rlBtnGroup.setY((float) (CalculatePixel.calculatePixelY(480)*0.5*0.1 +(CalculatePixel.calculatePixelY(10)*0.5) + (CalculatePixel.calculatePixelY(480)*0.5*0.7)));
////        rlBtnGroup.setGravity(Gravity.CENTER);
//
//        tvLeftBtn.setText("Left");
//        tvLeftBtn.setWidth((int) (CalculatePixel.calculatePixelX(80)*0.7));
//        tvLeftBtn.setHeight((int) (CalculatePixel.calculatePixelY(480)*0.5*0.2*0.7));
//        tvLeftBtn.setGravity(Gravity.CENTER);
//        tvLeftBtn.setBackgroundColor(Color.CYAN);
//        rlBtnGroup.addView(tvLeftBtn);
//
//        tvCenterBtn.setText("Center");
//        tvCenterBtn.setWidth((int) (CalculatePixel.calculatePixelX(80)*0.7));
//        tvCenterBtn.setHeight((int) (CalculatePixel.calculatePixelY(480)*0.5*0.2*0.7));
//        tvCenterBtn.setX((float) (CalculatePixel.calculatePixelX(150)*0.7-(CalculatePixel.calculatePixelX(80)*0.7)*0.5));
//        tvCenterBtn.setBackgroundColor(Color.CYAN);
//        tvCenterBtn.setGravity(Gravity.CENTER);
//        rlBtnGroup.addView(tvCenterBtn);
//
//        tvRightBtn.setText("Right");
//        tvRightBtn.setWidth((int) (CalculatePixel.calculatePixelX(80)*0.7));
//        tvRightBtn.setHeight((int) (CalculatePixel.calculatePixelY(480)*0.5*0.2*0.7));
//        tvRightBtn.setX((float) (CalculatePixel.calculatePixelX(300)*0.7-(CalculatePixel.calculatePixelX(80)*0.7)));
//        tvRightBtn.setBackgroundColor(Color.CYAN);
//        tvRightBtn.setGravity(Gravity.CENTER);
//        rlBtnGroup.addView(tvRightBtn);
//
//
//        exchangeViewByFlag();
//
//
//        rlParent.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        rlParent.addView(tvTitle);
//        rlParent.addView(etMessage);
//        rlParent.addView(tvContent);
//        rlParent.addView(tvDateTime);
//        rlParent.addView(rlBtnGroup);



//        rlParent.addView(llMiddleContainer);
//        rlParent.addView(llDownerContainer);
//        rlParent.setLayoutParams(new RelativeLayout.LayoutParams(500, 500));
//        getWindow().setAttributes(new WindowManager.LayoutParams((int)CalculatePixel.calculatePixelX(200),(int)CalculatePixel.calculatePixelY(300)));
    }

    private void exchangeViewByFlag(){
        switch (letterFlag){
            case "0" :{ // 공지사항
                tvLetterDialogTitle.setText("운영자 공지사항");
                tvLDLeftBtn.setText("삭제");
                tvLDCenterBtn.setVisibility(View.GONE);
                tvLDRightBtn.setText("닫기");

                tvLDRightBtn.setOnClickListener(closeDialog);
            }break;
            case "1" :{ // 친구신청 받음
                tvLetterDialogTitle.setText("친구신청");
                tvLetterDialogContent.setText(letterPKey);
                tvLDLeftBtn.setText("거절");
                tvLDCenterBtn.setText("수락");
                tvLDRightBtn.setText("차단");
            }break;
            case "2" :{ // 친구신청 요청
                tvLetterDialogTitle.setText("쪽지 (닉네임, 나이, 성별, km) ");
                tvLDLeftBtn.setText("삭제");
                tvLDCenterBtn.setText("OK");
                tvLDRightBtn.setText("답장");

                tvLDCenterBtn.setOnClickListener(closeDialog);
                tvLDRightBtn.setOnClickListener(openSendLetter);

            }break;
            case "3" :{ // 쪽지 보내기
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
