package graduateproject.com.twentyquestions.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.item.ChatDataItem;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.util.CalculatePixel;

/**
 * Created by yrs00 on 2017-08-29.
 */

public class GameChatListViewAdapter extends ArrayAdapter {

    Context mContext;
    ArrayList<ChatDataItem> mList;
    TextView nameView;
    TextView chatView;
    DBSI dbsi;

    public GameChatListViewAdapter(Context context, ArrayList<ChatDataItem> list){
        super(context, -1, list);
        mContext = context;
        mList = list;
        dbsi = new DBSI();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View row = setView(position);

        nameView.setText(mList.get(position).getUserName());
        chatView.setText(mList.get(position).getChattingText());

        return row;
    }


    private View setView(int position){

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        nameView = new TextView(mContext);
        chatView = new TextView(mContext);


        String localUserPKey = dbsi.getUserInfo().split("/")[0];

        Log.d("getUserPKey",mList.get(position).getUserPKey());
        Log.d("dbUserPKey", localUserPKey);


        if(mList.get(position).getUserPKey().equals(localUserPKey)){

            System.out.println("My ID");

            nameView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            nameView.setGravity(Gravity.RIGHT);
            chatView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            chatView.setGravity(Gravity.RIGHT);


            linearLayout.addView(nameView);
            linearLayout.addView(chatView);




        }else{
            System.out.println("Other ID");
            nameView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            nameView.setGravity(Gravity.LEFT);
            chatView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            chatView.setGravity(Gravity.LEFT);

            linearLayout.addView(nameView);
            linearLayout.addView(chatView);


        }


        return linearLayout;
    }
}
