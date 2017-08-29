package graduateproject.com.twentyquestions.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.GPSTracer;
import graduateproject.com.twentyquestions.util.ParseData;

import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelY;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class MainView extends BaseActivity {

    //View
    private RelativeLayout parentLayout;
    private LinearLayout tabRowLayout, tabLayout;
    private LinearLayout pagerLayout, divisionLine;
    private TextView btnStartGame, btnFriendList, btnLetterList, btnChatList, btnCreateGame, btnFastGame, btnOnlyEmptyRoom, btnLatestOrder;
    private ViewPager pager;
    private View.OnClickListener movePageListener, startGameListener;
    private GPSTracer locationManager;

    private GameListView gameListView = new GameListView();
    private FriendListView friendListView = new FriendListView();
    private ChatListView chatListView = new ChatListView();
    private LetterListView letterListView = new LetterListView();

    private NetworkSI network;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCustomActionBar();

        setValues();
        setUpEvents();
        setView();
        setContentView(parentLayout);

    }

    @Override
    public void setUpEvents() {

        movePageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                pager.setCurrentItem(tag);
                Log.d("Click", tag + "");
                System.out.println("Click ............ " + tag);
            }
        };

//        startGameListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gameListView.updateAdapter();
//                int tag = (int) view.getTag();
//                pager.setCurrentItem(tag);
//            }
//        };
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    gameListView.updateAdapter();
                }
        }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initUserData();
        GPSTracer.getInstance().getLocation();
        DataSync.getInstance().Timer();

    }

    public void initUserData() {

        JSONArray jsonArray;
        network = new NetworkSI();
        DBSI db = new DBSI();

        String response = network.request(DataSync.Command.GETINIT, "");

        ParseData parse = new ParseData();
        try {

            jsonArray = parse.jsonArrayInObject(response, "ChatRoom");

            for(int i=0; i<jsonArray.length(); i++) {

                JSONObject json = parse.doubleJsonObject(jsonArray.get(i).toString(), "chatroom");
                int pkey = json.getInt("PKey");
                String name = json.getString("Name");
                String longitude = json.getString("Longitude");
                String latitude = json.getString("Latitude");
                String createdDate = json.getString("CreatedDate");
                String updatedDate = json.getString("UpdatedDate");
                String description = json.getString("Description");

                String[][] selectChatRoom = db.selectQuery("select * from ChatRoom");

                if(selectChatRoom == null || Integer.parseInt(selectChatRoom[selectChatRoom.length-1][0]) < pkey) {
                    db.query("insert into ChatRoom values(\'" + pkey + "\', \'" + name + "\', \'" + longitude + "\', \'" + latitude+ "\', \'" + createdDate + "\', \'" + updatedDate + "\', \'" + description + "\')");
                }

            }

            jsonArray = parse.jsonArrayInObject(response, "Chat");

            for(int i=0; i<jsonArray.length(); i++) {

                JSONObject json = parse.doubleJsonObject(jsonArray.get(i).toString(), "chat");

                int pkey = json.getInt("PKey");
                int chatRoomPKey = json.getInt("ChatRoomPKey");
                int userPKey = json.getInt("UserPKey");
                String chatText = json.getString("ChatText");
                int count = json.getInt("Count");
                int type = json.getInt("Type");
                String createdDate = json.getString("CreatedDate");

                String[][] selectChat = db.selectQuery("select * from Chat");
                if(selectChat == null || Integer.parseInt(selectChat[selectChat.length-1][0]) < pkey) {
                    db.query("insert into Chat values(\'" + pkey + "\', " + chatRoomPKey + ", " + userPKey + ", \'" + chatText + "\', " + count + ", " + type + ", \'" + createdDate + "\')");
                }

            }

            jsonArray = parse.jsonArrayInObject(response, "ChatMember");

            for(int i=0; i<jsonArray.length(); i++) {

                JSONObject json = parse.doubleJsonObject(jsonArray.get(i).toString(), "chatmember");

                int pkey = json.getInt("PKey");
                int userPKey = json.getInt("UserPKey");
                int roomPKey = json.getInt("RoomPKey");
                int status = json.getInt("Status");
                int notify = json.getInt("Notify");
                String createdDate = json.getString("CreatedDate");
                String updatedDate = json.getString("UpdatedDate");

                String[][] selectChatMember = db.selectQuery("select * from ChatMember");
                if(selectChatMember == null || Integer.parseInt(selectChatMember[selectChatMember.length-1][0]) < pkey) {
                    db.query("insert into ChatMember values(" + pkey + ", " + userPKey + ", " + roomPKey + ", " + status + ", " + notify + ", \'" + createdDate + "\', \'" + updatedDate + "\')");
                }

            }

            jsonArray = parse.jsonArrayInObject(response, "GameList");

            for(int i=0; i<jsonArray.length(); i++) {

                JSONObject json = parse.doubleJsonObject(jsonArray.get(i).toString(), "gamelist");

                int pkey = json.getInt("PKey");
                int chatRoomPKey = json.getInt("ChatRoomPKey");
                int gameTypePKey = json.getInt("GameTypePKey");
                String name = json.getString("Name");
                String password = json.getString("Password");
                String description = json.getString("Description");
                int status = json.getInt("Status");
                int gameStatus = json.getInt("GameStatus");
                int minMember = json.getInt("MinMember");
                int maxMember = json.getInt("MaxMember");
                double longitude = json.getDouble("Longitude");
                double latitude = json.getDouble("Latitude");
                String createdDate = json.getString("CreatedDate");
                String updatedDate = json.getString("UpdatedDate");

                String[][] selectGameList = db.selectQuery("select * from GameList");
                if(selectGameList == null || Integer.parseInt(selectGameList[selectGameList.length-1][0]) < pkey) {
                    db.query("insert into GameList values( " + pkey + ", " + chatRoomPKey + ", " + gameTypePKey + ", \'" + name + "\', \'" + password + "\', \'" + description + "\', " +
                    status + ", " + gameStatus + ", " + minMember + ", " + maxMember + ", " + longitude + ", " + latitude + ", \'" + createdDate + "\', \'" + updatedDate + "\')");
                }

            }

            jsonArray = parse.jsonArrayInObject(response, "GameMember");

            for(int i=0; i<jsonArray.length(); i++) {

                JSONObject json = parse.doubleJsonObject(jsonArray.get(i).toString(), "gamemember");
                int pkey = json.getInt("PKey");
                int gameListPKey = json.getInt("GameListPKey");
                int userPKey = json.getInt("UserPKey");
                int memberPriority = json.getInt("MemberPriority");
                int status = json.getInt("Status");
                int isWinner = json.getInt("IsWinner");
                String createdDate = json.getString("CreatedDate");
                String updatedDate = json.getString("UpdatedDate");

                String[][] selectGameMember = db.selectQuery("select * from GameMember");
                if(selectGameMember == null || Integer.parseInt(selectGameMember[selectGameMember.length-1][0]) < pkey) {
                    db.query("insert into GameMember values( " + pkey + ", " + gameListPKey + ", " + userPKey + ", " + memberPriority + ", " + status + ", " + isWinner + ", \'" + createdDate + "\', \'" + updatedDate + "\')");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setView() {

        btnStartGame = new TextView(this);
        btnStartGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnStartGame.setText("게임하기");
        btnStartGame.setGravity(Gravity.CENTER);
        btnStartGame.setTag(0);
        btnStartGame.setOnClickListener(movePageListener);

        btnFriendList = new TextView(this);
        btnFriendList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnFriendList.setText("친구");
        btnFriendList.setGravity(Gravity.CENTER);
        btnFriendList.setTag(1);
        btnFriendList.setOnClickListener(movePageListener);

        btnChatList = new TextView(this);
        btnChatList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnChatList.setText("대화");
        btnChatList.setGravity(Gravity.CENTER);
        btnChatList.setTag(2);
        btnChatList.setOnClickListener(movePageListener);

        btnLetterList = new TextView(this);
        btnLetterList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnLetterList.setText("쪽지함");
        btnLetterList.setGravity(Gravity.CENTER);
        btnLetterList.setTag(3);
        btnLetterList.setOnClickListener(movePageListener);

        tabRowLayout = new LinearLayout(this);
        tabRowLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)calculatePixelY(30)));
        tabRowLayout.setOrientation(LinearLayout.HORIZONTAL);

        tabRowLayout.addView(btnStartGame);
        tabRowLayout.addView(btnFriendList);
        tabRowLayout.addView(btnChatList);
        tabRowLayout.addView(btnLetterList);

        tabLayout = new LinearLayout(this);
        LinearLayout.LayoutParams tabParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabParams.setMargins(0,0,0,0);
        tabLayout.setLayoutParams(tabParams);
        tabLayout.setOrientation(LinearLayout.VERTICAL);

        tabLayout.addView(tabRowLayout);

        divisionLine = new LinearLayout(MainView.mContext);
        divisionLine.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        layoutParams.setMargins(0, 0, 0, 0);
        divisionLine.setLayoutParams(layoutParams);

        tabLayout.addView(divisionLine);

        pager.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            pager.setId(pager.hashCode());
        } else {
            pager.setId(View.generateViewId());
        }

        pager.setAdapter(new MainViewAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        pagerLayout = new LinearLayout(this);
        pagerLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        pagerLayout.addView(pager);

        tabLayout.addView(pagerLayout);

        parentLayout = new RelativeLayout(this);
        parentLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        parentLayout.addView(tabLayout);

    }

    @Override
    public void setValues() {

        pager = new ViewPager(this);

    }

    private class MainViewAdapter extends FragmentStatePagerAdapter {
        @Override
        public int getItemPosition(Object object) {
            return POSITION_UNCHANGED;
        }

        public MainViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return gameListView;
                case 1:
                    return friendListView;
                case 2:
                    return chatListView;
                case 3:
                    return letterListView;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
