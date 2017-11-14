package graduateproject.com.twentyquestions.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.item.GameListViewData;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.view.BaseActivity;
import graduateproject.com.twentyquestions.view.GameRoomView;
import graduateproject.com.twentyquestions.view.MainView;

/**
 * Created by mapl0 on 2017-08-19.
 */

public class GameListViewAdapter extends BaseAdapter {

    private LinearLayout parentLayout, llRoomValues, llRoomName, llDesc, llRoomTitles, llRoomNameTitle, llDescTitle, llbtnIsPlayingGame;
    private TextView tvRoomNumber, tvRoomName, tvDesc, tvIsPlayingGame, tvRoomNumberTitle, tvRoomNameTitle, tvDescTitle, tvIsPlayingGameTitle;
    private DBSI dbsi;
    private String userPKey;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<GameListViewData> listViewItemList;

    // ListViewAdapter의 생성자
    public GameListViewAdapter() {
        listViewItemList = new ArrayList<GameListViewData>();
        dbsi = new DBSI();
        userPKey = dbsi.selectQuery("select PKey from User where MySelf = 0")[0][0];
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        final GameListViewData listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tvRoomNumber.setText(listViewItem.getRoomNumber() + "");
        tvRoomName.setText(listViewItem.getRoomName());
        tvDesc.setText(listViewItem.getDescription());


        // 방입장 구현 위한 onClickListener
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DBSI dbsi = new DBSI();

                // 로컬 DB GameList 정보 있는 지 확인
                final NetworkSI networkSI = new NetworkSI();
                JSONObject checkJson = new JSONObject();
                final String roomNumber = listViewItem.getRoomNumber();
                final String chatRoomPKey = listViewItem.getChatRoomPKey();

                try {
                    checkJson.put("ChatRoomPKey", chatRoomPKey);
                    checkJson.put("UserPKey", userPKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                networkSI.request(DataSync.Command.CHECKUSERNUMBER, checkJson.toString(), new NetworkSI.AsyncResponse() {
                    @Override
                    public void onSuccess(String response) {
                        switch (response) {

                            case "success":

                                Log.d("CheckUserCount", "success");

                                JSONObject entergamedata = new JSONObject();
                                String[][] selectGameList = dbsi.selectQuery("SELECT * FROM GameList");

                                if (selectGameList == null) {
                                    // GameList 없으면, 최초 입장 -> 서버 DB GameMember, ChatMember만 추가 : Flag 1
                                    try {
                                        entergamedata.put("GameListPKey", roomNumber);
                                        entergamedata.put("ChatRoomPKey", chatRoomPKey);
                                        entergamedata.put("Flag", "1");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    networkSI.request(DataSync.Command.ENTERGAMEROOM, entergamedata.toString(), new NetworkSI.AsyncResponse() {
                                        @Override
                                        public void onSuccess(String response) {
                                            Log.d("EnterResponse", response + "/");
                                            if (response.contains("ENTER_SUCCESS"))
                                                DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                                    @Override
                                                    public void onFinished(String response) {
                                                        Intent intent = new Intent(MainView.mContext, GameRoomView.class);
                                                        MainView.mContext.startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onPreExcute() {

                                                    }
                                                });
                                            else
                                                Toast.makeText(context, "EnterFail", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onFailure(String response) {
                                            Log.d("EnterFailResponse", response);
                                        }
                                    });

                                } else if (selectGameList[0][0].equals(listViewItem.getRoomNumber()) &&
                                        selectGameList[0][1].equals(listViewItem.getChatRoomPKey())) {

                                    // 들어갔던방 또 들어가는경우
                                    // 그냥 dosync만 한번 때려주자
                                    System.out.println("다시 입장");
                                    Log.d("selectGameList[0][0] ", selectGameList[0][0]);
                                    Log.d("Item.getRoomNumber()", listViewItem.getRoomNumber());

                                    DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                        @Override
                                        public void onFinished(String response) {
                                            Intent intent = new Intent(MainView.mContext, GameRoomView.class);
                                            MainView.mContext.startActivity(intent);
                                        }

                                        @Override
                                        public void onPreExcute() {

                                        }
                                    });

                                } else {
                                    Log.d("selectGameList[0][0] ", selectGameList[0][0]);
                                    Log.d("Item.getRoomNumber()", listViewItem.getRoomNumber());
                                    Log.d("selectGameList[0][0] ", selectGameList[0][1]);
                                    Log.d("Item.getChatRoomPKey()", listViewItem.getChatRoomPKey());

                                    // GameList 있으면, 서버 DB(GameMember, ChatMember) 해당 레코드 status -1로 수정 Flag : 2
                                    // 이때, GameList, ChatRoom의 PKey들로 GameMember, ChatMember의 레코드를 찾을수 있다. -> 어차피 방은 한번밖에 안들어가고 각각 자기의 UserPKey의 정보밖에 가지고 있지 않아서 무조건 한개의 결과만 나온다.
                                    // 여기서 서버 DB GameMemeber에 들어가는 방의 GameListPKey를 확인해서 전에 들어갔던 방이면 status를 다시 0으로
                                    // 처음들어가는 방이면 Insert 만 실시
                                    try {
                                        entergamedata.put("GameMemberPKeyOld", dbsi.selectQuery("SELECT * FROM GameMember where UserPKey = "+userPKey+" AND GameListPKey = " + selectGameList[0][0])[0][0]);
                                        entergamedata.put("ChatMemberPKeyOld", dbsi.selectQuery("SELECT * FROM ChatMember where UserPKey = "+userPKey+" AND RoomPKey = " + selectGameList[0][1])[0][0]);
                                        entergamedata.put("GameListPKeyNew", listViewItem.getRoomNumber());
                                        entergamedata.put("ChatRoomPKeyNew", listViewItem.getChatRoomPKey());
                                        entergamedata.put("Flag", "2");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    networkSI.request(DataSync.Command.ENTERGAMEROOM, entergamedata.toString(), new NetworkSI.AsyncResponse() {
                                        @Override
                                        public void onSuccess(String response) {
                                            Log.d("EnterRoom2Response", response + "?");
                                            dbsi.query("Delete from GameList");
                                            if (response.contains("ENTER_SUCCESS")) {
                                                DataSync.getInstance().doSync(new DataSync.AsyncResponse() {
                                                    @Override
                                                    public void onFinished(String response) {
                                                        Intent intent = new Intent(MainView.mContext, GameRoomView.class);
                                                        MainView.mContext.startActivity(intent);

                                                    }

                                                    @Override
                                                    public void onPreExcute() {

                                                    }
                                                });

                                            } else {
                                                System.out.println("EnterFail Whene Other Room");
                                            }

                                        }

                                        @Override
                                        public void onFailure(String response) {

                                        }
                                    });

                                }

                                break;
                            case "fail":
                                Log.d("CheckUserCount", "fail");
                                Toast.makeText(BaseActivity.mContext, "인원이 초과되어 입장할 수 없습니다.", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(String response) {

                    }
                });

            }
        });

        return parentLayout;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String roomNumber, String roomName, String roomDescription, String chatRoomPKey) {
        GameListViewData item = new GameListViewData();

        item.setRoomNumber(roomNumber);
        item.setRoomName(roomName);
        item.setDescription(roomDescription);
        item.setChatRoomPKey(chatRoomPKey);

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
        llRoomTitles.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        llRoomValues.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        parentLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        parentLayout.addView(llRoomTitles);
        parentLayout.addView(llRoomValues);
//        parentLayout.addView(llRoomName);
//        parentLayout.addView(llDesc);

    }
}
