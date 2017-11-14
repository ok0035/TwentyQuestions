package graduateproject.com.twentyquestions.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.adapter.FriendAdapter;
import graduateproject.com.twentyquestions.item.FriendData;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.network.NetworkSI;
import graduateproject.com.twentyquestions.util.BasicMethod;
import graduateproject.com.twentyquestions.util.ParseData;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class FriendListView extends Fragment implements BasicMethod{

    private View view;
    private android.widget.ListView lvFriend;
    private ArrayList<FriendData> friendList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.friend_list, container, false);

        bindView();
        setValues();
        setUpEvents();

        return view;
    }

    @Override
    public void setUpEvents() {

        JSONObject data = new JSONObject();

        new NetworkSI().request(DataSync.Command.GETFRIENDLIST, data.toString(), new NetworkSI.AsyncResponse() {
            @Override
            public void onSuccess(String response) {
                Log.d("What?", response);

                if(!response.equals("[]")) {
                    ParseData parse = new ParseData();
                    try {
                        Log.d("ArrayInObject", parse.parseJsonArray(response) + "");
                        JSONArray friendListArray = parse.parseJsonArray(response);
                        for (int i = 0; i < friendListArray.length(); i++) {
                            JSONObject json = parse.doubleJsonObject(friendListArray.get(i).toString(), "friend");
                            FriendData friendData = new FriendData();
                            friendData.setFriendName(json.getString("NickName"));
                            friendList.add(friendData);
                        }

                        FriendAdapter adapter =  new FriendAdapter(getContext(), friendList);

                        lvFriend.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else Log.d("FriendDataResponse", response);

            }

            @Override
            public void onFailure(String response) {

            }
        });

    }

    @Override
    public void setValues() {
        friendList = new ArrayList<FriendData>();
    }

    @Override
    public void bindView() {
        this.lvFriend = (ListView) view.findViewById(R.id.lvFriend);
    }


}
