package graduateproject.com.twentyquestions.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.view.MainView;
import graduateproject.com.twentyquestions.item.GameListViewItem;

/**
 * Created by mapl0 on 2017-08-19.
 */

public class GameListViewAdapter extends BaseAdapter {

    private LinearLayout parentLayout, llRoomValues, llRoomName, llDesc, llRoomTitles, llRoomNameTitle, llDescTitle, llbtnIsPlayingGame;
    private TextView tvRoomNumber, tvRoomName, tvDesc, tvIsPlayingGame, tvRoomNumberTitle, tvRoomNameTitle, tvDescTitle, tvIsPlayingGameTitle;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<GameListViewItem> listViewItemList;

    // ListViewAdapter의 생성자
    public GameListViewAdapter() {
        listViewItemList = new ArrayList<GameListViewItem>();
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            setView();
//            convertView = inflater.inflate((XmlPullParser) parentLayout, parent, false);
        }

        setView();

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
//        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
//        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
//        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;

//         Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        GameListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tvRoomNumber.setText(listViewItem.getRoomNumber() + "");
        tvRoomName.setText(listViewItem.getRoomName());
        tvDesc.setText(listViewItem.getDescription());

        return parentLayout;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String roomNumber, String roomName, String sex) {
        GameListViewItem item = new GameListViewItem();

        item.setRoomNumber(roomNumber);
        item.setRoomName(roomName);
        item.setDescription(sex);

        listViewItemList.add(item);
    }

    public void setView() {


        tvRoomNumberTitle = new TextView(MainView.mContext);
        tvRoomNumberTitle.setGravity(Gravity.CENTER);
        tvRoomNumberTitle.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.3f));
        tvRoomNumberTitle.setText("번호");

        tvRoomNameTitle = new TextView(MainView.mContext);
        tvRoomNameTitle.setGravity(Gravity.CENTER);
        tvRoomNameTitle.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        tvRoomNameTitle.setText("제목");

        tvDescTitle = new TextView(MainView.mContext);
        tvDescTitle.setGravity(Gravity.CENTER);
        tvDescTitle.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        tvDescTitle.setText("설명");

        tvIsPlayingGameTitle = new TextView(MainView.mContext);
        tvIsPlayingGameTitle.setGravity(Gravity.CENTER);
        tvIsPlayingGameTitle.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        tvIsPlayingGameTitle.setText("상태");

        llRoomTitles = new LinearLayout(MainView.mContext);
        llRoomTitles.setOrientation(LinearLayout.HORIZONTAL);
        llRoomTitles.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llRoomTitles.addView(tvRoomNumberTitle);
        llRoomTitles.addView(tvRoomNameTitle);
        llRoomTitles.addView(tvDescTitle);
        llRoomTitles.addView(tvIsPlayingGameTitle);


        tvRoomNumber = new TextView(MainView.mContext);
        tvRoomNumber.setGravity(Gravity.CENTER);
        tvRoomNumber.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.3f));

        tvRoomName = new TextView(MainView.mContext);
        tvRoomName.setGravity(Gravity.CENTER);
        tvRoomName.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        tvDesc = new TextView(MainView.mContext);
        tvDesc.setGravity(Gravity.CENTER);
        tvDesc.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        tvIsPlayingGame = new TextView(MainView.mContext);
        tvIsPlayingGame.setGravity(Gravity.CENTER);
        tvIsPlayingGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        llRoomValues = new LinearLayout(MainView.mContext);
        llRoomValues.setOrientation(LinearLayout.HORIZONTAL);
        llRoomValues.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llRoomValues.addView(tvRoomNumber);
        llRoomValues.addView(tvRoomName);
        llRoomValues.addView(tvDesc);
        llRoomValues.addView(tvIsPlayingGame);

//        llRoomName = new LinearLayout(MainView.mContext);
//        llRoomName.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
//        llRoomName.addView(tvRoomName);
//
//        llDesc = new LinearLayout(MainView.mContext);
//        llDesc.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
//        llDesc.addView(tvDesc);

        parentLayout = new LinearLayout(MainView.mContext);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        parentLayout.addView(llRoomTitles);
        parentLayout.addView(llRoomValues);
//        parentLayout.addView(llRoomName);
//        parentLayout.addView(llDesc);

    }
}
