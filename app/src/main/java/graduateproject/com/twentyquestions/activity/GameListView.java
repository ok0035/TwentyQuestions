package graduateproject.com.twentyquestions.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.adapter.GameListViewAdapter;
import graduateproject.com.twentyquestions.network.HttpNetwork;
import graduateproject.com.twentyquestions.util.ParseData;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class GameListView extends Fragment {

    private LinearLayout parentLayout, divisionLine;
    private ListView list;
    private GameListViewAdapter adapter;

    public GameListView() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<NameValuePair> params = new ArrayList<>();

        new HttpNetwork(params, new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("What?", response);

                ParseData parse = new ParseData();
                try {
                    Log.d("ArrayInObject", parse.parseJsonArray(response) + "");
                    JSONArray gameListArray = parse.parseJsonArray(response);
                    for(int i=0; i<gameListArray.length(); i++) {

                        JSONObject json = parse.doubleJsonObject(gameListArray.get(i).toString(), "gameroom");
                        adapter.addItem(json.getString("GamePKey"), json.getString("GameName"), json.getString("GameDescription"));
                        adapter.notifyDataSetChanged();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String response) {

            }
        }).execute(BaseActivity.serverAddress + "android/twentyQuestions/include/GetGameList.php");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setView();

        return parentLayout;
    }

    public void setView() {

        adapter = new GameListViewAdapter();

        list = new ListView(MainView.mContext);
        list.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        list.setAdapter(adapter);

        parentLayout = new LinearLayout(MainView.mContext);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        parentLayout.addView(list);

//        parentLayout.setBackgroundColor(Color.WHITE);

    }


}
