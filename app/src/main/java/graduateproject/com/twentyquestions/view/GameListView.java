package graduateproject.com.twentyquestions.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.adapter.GameListViewAdapter;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.HttpNetwork;
import graduateproject.com.twentyquestions.util.ParseData;

import static graduateproject.com.twentyquestions.util.CalculatePixel.calculatePixelY;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class GameListView extends Fragment {

    private LinearLayout parentLayout, tabLayout, divisionLine;
    private ListView list;
    private GameListViewAdapter adapter;

    private CreateRoomDialog dialog;

    private TextView btnCreateGame, btnFastGame, btnOnlyEmptyRoom, btnLatestOrder, btnRefresh;
    private View.OnClickListener clickRefresh, clickCreateRoom;

    private Context context;

    public GameListView() {
        super();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getContext();
        DataSync.getInstance().setDSContext(context);
        setUpEvents();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setView();

        return parentLayout;
    }

    public void updateAdapter() {

        final GameListViewAdapter adapter = new GameListViewAdapter();

        ArrayList<NameValuePair> params = new ArrayList<>();

        new HttpNetwork(params, new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("What?", response);

                ParseData parse = new ParseData();
                try {
                    Log.d("ArrayInObject", parse.parseJsonArray(response) + "");
                    JSONArray gameListArray = parse.parseJsonArray(response);
                    for (int i = 0; i < gameListArray.length(); i++) {

                        JSONObject json = parse.doubleJsonObject(gameListArray.get(i).toString(), "gameroom");
                        adapter.addItem(json.getString("GamePKey"), json.getString("GameName"), json.getString("GameDescription"), json.getString("ChatRoomPKey"));
//                        adapter.notifyDataSetChanged();

                    }

                    list.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        }).execute(BaseActivity.serverAddress + "android/twentyQuestions/include/GetGameList.php");

    }

    public void setUpEvents() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        ArrayList<NameValuePair> params = new ArrayList<>();

        new HttpNetwork(params, new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("What?", response);

                ParseData parse = new ParseData();
                try {
                    Log.d("ArrayInObject", parse.parseJsonArray(response) + "");
                    JSONArray gameListArray = parse.parseJsonArray(response);
                    for (int i = 0; i < gameListArray.length(); i++) {

                        JSONObject json = parse.doubleJsonObject(gameListArray.get(i).toString(), "gameroom");
                        adapter.addItem(json.getString("GamePKey"), json.getString("GameName"), json.getString("GameDescription"), json.getString("ChatRoomPKey"));
                        adapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String response) {

            }

            @Override
            public void onPreExcute() {

            }
        }).execute(BaseActivity.serverAddress + "android/twentyQuestions/include/GetGameList.php");

        clickRefresh = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAdapter();
            }
        };

        clickCreateRoom = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBSI dbsi = new DBSI();
                String[][] findLocalGameList = dbsi.selectQuery("SELECT * FROM GameList");
                if (findLocalGameList == null) {
                    dialog = new CreateRoomDialog(getContext());
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {

                        }
                    });
                    dialog.show();
                }else{
                    Intent intent = new Intent(MainView.mContext, GameRoomView.class);
                    startActivity(intent);
                }

            }

        };

    }

    public void setView() {

        adapter = new GameListViewAdapter();

        btnCreateGame = new TextView(MainView.mContext);
        btnCreateGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnCreateGame.setGravity(Gravity.CENTER);
        btnCreateGame.setText("게임 만들기");
        btnCreateGame.setOnClickListener(clickCreateRoom);

        btnFastGame = new TextView(MainView.mContext);
        btnFastGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnFastGame.setGravity(Gravity.CENTER);
        btnFastGame.setText("빠른시작");

        btnLatestOrder = new TextView(MainView.mContext);
        btnLatestOrder.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnLatestOrder.setGravity(Gravity.CENTER);
        btnLatestOrder.setText("최근순");

        btnOnlyEmptyRoom = new TextView(MainView.mContext);
        btnOnlyEmptyRoom.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnOnlyEmptyRoom.setGravity(Gravity.CENTER);
        btnOnlyEmptyRoom.setText("빈방만");

        btnRefresh = new TextView(MainView.mContext);
        btnRefresh.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT, 1f));
        btnRefresh.setGravity(Gravity.CENTER);
        btnRefresh.setText("새로고침");
        btnRefresh.setOnClickListener(clickRefresh);

        tabLayout = new LinearLayout(MainView.mContext);
        tabLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) calculatePixelY(30)));
        tabLayout.setOrientation(LinearLayout.HORIZONTAL);

        tabLayout.addView(btnCreateGame);
        tabLayout.addView(btnFastGame);
        tabLayout.addView(btnLatestOrder);
        tabLayout.addView(btnOnlyEmptyRoom);
        tabLayout.addView(btnRefresh);

        divisionLine = new LinearLayout(MainView.mContext);
        divisionLine.setBackgroundColor(Color.BLACK);
        divisionLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));

        list = new ListView(MainView.mContext);
        list.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        list.setAdapter(adapter);

        parentLayout = new LinearLayout(MainView.mContext);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        parentLayout.addView(tabLayout);
        parentLayout.addView(divisionLine);
        parentLayout.addView(list);

//        parentLayout.setBackgroundColor(Color.WHITE);

    }


}
