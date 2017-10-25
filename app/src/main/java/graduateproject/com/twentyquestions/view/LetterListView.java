package graduateproject.com.twentyquestions.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.adapter.LetterAdapter;
import graduateproject.com.twentyquestions.item.LetterDataItem;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.HttpNetwork;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.ParseData;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class LetterListView extends Fragment {

    LinearLayout layout;
    ListView listview;
    LetterDataItem letterDataItem;
    ArrayList<LetterDataItem > letterDataItemList;

    DBSI dbsi;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbsi = new DBSI();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.letter_litst_view, container, false);
        listview = (ListView) layout.findViewById(R.id.letterListView);

        updateLetterList();

        return layout;
    }

    private void updateLetterList(){
        letterDataItemList = new ArrayList<>();
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("UserPKey", dbsi.selectQuery("SELECT PKey FROM User WHERE MySelf = 0")[0][0]));
        new HttpNetwork(params, new HttpNetwork.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("What?", response);

                ParseData parse = new ParseData();
                try {
                    Log.d("ArrayInObject", parse.parseJsonArray(response) + "");
                    JSONArray letterListArray = parse.parseJsonArray(response);
                    for (int i = 0; i < letterListArray.length(); i++) {
                        JSONObject json = parse.doubleJsonObject(letterListArray.get(i).toString(), "letter");
                        LetterDataItem letterDataItem = new LetterDataItem();
                        letterDataItem.setLetterPKey(json.getString("PKey"));
                        letterDataItem.setLetterType(json.getString("TableName"));
                        letterDataItem.setLetterTitle(json.getString("Title"));
                        letterDataItem.setLetterCreatedDate(json.getString("CreatedDate"));
                        letterDataItem.setLetterContent(json.getString("Content"));
                        letterDataItemList.add(letterDataItem);
                    }

                    listview.setAdapter(new LetterAdapter(getContext(), letterDataItemList));

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
        }).execute(BaseActivity.serverAddress + "TQTest/twentyQuestions/include/GetLetterList.php");

    }
}
