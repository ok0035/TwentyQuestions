package graduateproject.com.twentyquestions.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.adapter.GameListViewAdapter;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.HttpNetwork;
import graduateproject.com.twentyquestions.util.BasicMethod;
import graduateproject.com.twentyquestions.util.ParseData;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class GameListView extends Fragment implements BasicMethod{

    private LinearLayout parentLayout, tabLayout, divisionLine;
    private ListView lvGame;
    private GameListViewAdapter adapter;

    private CreateRoom dialog;

    private TextView btnCreateGame, btnFastGame, btnOnlyEmptyRoom, btnLatestOrder, btnRefresh;
    private View.OnClickListener clickRefresh, clickCreateRoom;

    private Context context;
    private View view;

    public GameListView() {
        super();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getContext();
        DataSync.getInstance().setDSContext(context);


    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.game_list, container, false);

        bindView();
        setValues();
        setUpEvents();



        return view;
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

                    lvGame.setAdapter(adapter);

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
        }).execute(BaseActivity.serverAddress + "TQTest/twentyQuestions/include/GetGameList.php");

    }

    @Override
    public void setValues() {

    }

    public void setUpEvents() {

        adapter = new GameListViewAdapter();
        lvGame.setAdapter(adapter);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAdapter();
            }
        });

        btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBSI dbsi = new DBSI();
                String[][] findLocalGameList = dbsi.selectQuery("SELECT * FROM GameList");
                if (findLocalGameList == null) {
                    Intent intent = new Intent(MainView.mContext, CreateRoom.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainView.mContext, GameRoomView.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void bindView() {
        this.lvGame = (ListView) view.findViewById(R.id.lvGame);
        this.btnRefresh = (TextView) view.findViewById(R.id.btnRefresh);
        this.btnOnlyEmptyRoom = (TextView) view.findViewById(R.id.btnOnlyEmptyRoom);
        this.btnLatestOrder = (TextView) view.findViewById(R.id.btnLatestOrder);
        this.btnFastGame = (TextView) view.findViewById(R.id.btnFastGame);
        this.btnCreateGame = (TextView) view.findViewById(R.id.btnCreateGame);
    }

}
