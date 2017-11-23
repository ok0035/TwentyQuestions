package graduateproject.com.twentyquestions.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.item.ChatData;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.util.BasicMethod;

/**
 * Created by yrs00 on 2017-08-29.
 */

public class GameChatListViewAdapter extends ArrayAdapter implements BasicMethod {

    Context mContext;
    ArrayList<ChatData> mList;
    private android.widget.ImageView ivProfile;
    private android.widget.TextView tvChatText;
    private android.widget.LinearLayout llChatItem;
    DBSI dbsi;
    View view;
    LayoutInflater inflater;
    private ChatData chatData;

    public GameChatListViewAdapter(Context context, ArrayList<ChatData> list){
        super(context, -1, list);
        mContext = context;
        mList = list;
        dbsi = new DBSI();
        System.out.println("리스트뷰 생성 완료");
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View row = setView(position);

        view = convertView;

        if(convertView == null) {

            view = inflater.inflate(R.layout.chat_list_item, parent, false);

        }

        bindView();
        setUpEvents();

        tvChatText.setText(mList.get(position).getChattingText());
        chatData = mList.get(position);

        return view;
    }


    private View setView(int position){

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tvChatText = new TextView(mContext);

//
//        String localUserPKey = dbsi.getUserInfo().split("/")[0];
//
//        Log.d("getUserPKey",mList.get(position).getUserPKey());
//        Log.d("dbUserPKey", localUserPKey);


        return linearLayout;
    }

    @Override
    public void setValues() {

    }

    @Override
    public void setUpEvents() {

        Log.d("어레이리스트 길이 in Adapter",mList.size()+"/");

        if(chatData.getUserMySelf().equals("0")){

            System.out.println("My ID");

            ivProfile.setForegroundGravity(Gravity.RIGHT);
            tvChatText.setBackgroundResource(R.drawable.outbox);
            tvChatText.setGravity(Gravity.RIGHT);


        }else{
            System.out.println("Other ID");
            ivProfile.setForegroundGravity(Gravity.LEFT);
            tvChatText.setBackgroundResource(R.drawable.inbox);
            tvChatText.setGravity(Gravity.LEFT);


        }

        if(chatData.getChatFlag().contains("askans")){
            llChatItem.setBackgroundColor(Color.LTGRAY);
        }else if(chatData.getChatFlag().contains("right")){
            llChatItem.setBackgroundColor(Color.YELLOW);
        } else {
            llChatItem.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public void bindView() {
        this.tvChatText = (TextView) view.findViewById(R.id.tvChatText);
        this.ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
        this.llChatItem = (LinearLayout) view.findViewById(R.id.llChatItem);
    }
}
