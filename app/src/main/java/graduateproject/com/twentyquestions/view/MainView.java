package graduateproject.com.twentyquestions.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.GPSTracer;
import graduateproject.com.twentyquestions.util.ParseData;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class MainView extends BaseActivity {

    //View
    private RelativeLayout parentLayout;
    private LinearLayout tabRowLayout, tabLayout;
    private LinearLayout pagerLayout, divisionLine;
    private ViewPager vpMain;
    private View.OnClickListener movePageListener, startGameListener;
    private GPSTracer locationManager;

    private GameListView gameListView = new GameListView();
    private FriendListView friendListView = new FriendListView();
    private ChatListView chatListView = new ChatListView();
    private LetterListView letterListView = new LetterListView();

    private NetworkSI network;
    private LinearLayout btnStartGame;
    private LinearLayout btnFriendList;
    private LinearLayout btnChatList;
    private LinearLayout btnLetterList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomActionBar();
        setContentView(R.layout.main);
        bindView();
        setValues();
        setUpEvents();

        // 채팅 기능 구현 테스트용 인텐트
        // 끝나면 바로 지우자
//        Intent intent = new Intent(getApplicationContext(), GameRoomView.class);
//        startActivity(intent);
//        finish();

    }

    @Override
    public void setUpEvents() {

        movePageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                vpMain.setCurrentItem(tag);
                Log.d("Click", tag + "");
                System.out.println("Click ............ " + tag);

//                Intent intent = new Intent(getApplicationContext(), LetterDialog.class);
//                intent.putExtra("letterFlag",String.valueOf(tag));
//                startActivity(intent);

            }
        };

        btnStartGame.setTag(0);
        btnStartGame.setOnClickListener(movePageListener);


        btnFriendList.setTag(1);
        btnFriendList.setOnClickListener(movePageListener);


        btnChatList.setTag(2);
        btnChatList.setOnClickListener(movePageListener);


        btnLetterList.setTag(3);
        btnLetterList.setOnClickListener(movePageListener);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            vpMain.setId(vpMain.hashCode());
        } else {
            vpMain.setId(View.generateViewId());
        }

        vpMain.setAdapter(new MainViewAdapter(getSupportFragmentManager()));
        vpMain.setCurrentItem(0);

//        startGameListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gameListView.updateAdapter();
//                int tag = (int) view.getTag();
//                vpMain.setCurrentItem(tag);
//            }
//        };
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        DataSync.getInstance().Timer(new DataSync.AsyncResponse() {
            @Override
            public void onFinished(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        });

    }

    public void initUserData() {


        network = new NetworkSI();


        String response = network.request(DataSync.Command.GETINIT, "", new NetworkSI.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                ParseData parse = new ParseData();
                try {
                    JSONArray jsonArray;
                    DBSI db = new DBSI();
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
            public void onFailure(String response) {

            }
        });



    }


    public void testFunction(){
        Log.d("TEST","FUNCTION");
    }

    @Override
    public void setValues() {
        //        /// 임시로 친구 데이터 삽입

        DBSI dbsi = new DBSI();
//        // 나중엔 지우자
        if ((dbsi.selectQuery("SELECT * FROM User WHERE PKey = 1") == null) ? true : false) {
            dbsi.query("INSERT INTO User('PKey', 'ID', 'LoginType' ,'NickName','Gender','BirthDay','MySelf', 'CreatedDate', 'UpdatedDate') " +
                    "VALUES('1','admin','0','toby','1','1993-12-06 12:00:00','1','2017-08-03 12:27:47','17-08-11 11:54:09')");
        }else if((dbsi.selectQuery("SELECT * FROM User WHERE PKey = 2") == null) ? true : false){
            dbsi.query("INSERT INTO User('PKey', 'ID', 'LoginType' ,'NickName','Gender','BirthDay','MySelf', 'CreatedDate', 'UpdatedDate') " +
                    "VALUES('2','qwerty','0','test','0','2001-01-01 12:00:00','1','2017-09-04 17:02:54','2017-09-04 17:02:54')");
        }
        dbsi.selectQuery("SELECT * FROM User");

        // FCM Access Token 받아오기
        FirebaseInstanceId.getInstance().getToken();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM_TOKEN",token);

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

    @Override
    public void bindView() {
        this.vpMain = (ViewPager) findViewById(R.id.vpMain);
        this.btnLetterList = (LinearLayout) findViewById(R.id.btnLetterList);
        this.btnChatList = (LinearLayout) findViewById(R.id.btnChatList);
        this.btnFriendList = (LinearLayout) findViewById(R.id.btnFriendList);
        this.btnStartGame = (LinearLayout) findViewById(R.id.btnStartGame);
    }


}
