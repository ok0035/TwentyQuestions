package graduateproject.com.twentyquestions.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.activity.BaseActivity;
import graduateproject.com.twentyquestions.util.ParseData;

/**
 * Created by mapl0 on 2017-08-03.
 */

public class DataSyncController {

    private int localChatPkey = 0;
    private int localChatRoomPkey = 0;
    private int localChatMemberPkey = 0;

    private int serverChatPkey = 0;
    private int serverChatRoomPkey = 0;
    private int serverChatMemberPkey = 0;

    private JSONArray chatRoomArray;
    private JSONObject chatRoomData;
    private String parsedResult = "";

    public void updateData(String response) {

        ParseData parse = new ParseData();
        DBSI db = new DBSI();

        try {

            parsedResult = parse.parseJsonObject(response, "Result");
            Log.d("parsedResult", parsedResult);

            chatRoomArray = parse.jsonArrayInObject(response, "ChatRoom");

            for (int i = 0; i < chatRoomArray.length(); i++) {

                Log.d("JsonArray", chatRoomArray.get(i).toString());
                chatRoomData = parse.doubleJsonObject(chatRoomArray.get(i).toString(), "chatroom");

                Log.d("chatRoomPkey", chatRoomData.getString("PKey"));
                Log.d("chatRoomName", chatRoomData.getString("Name"));
                Log.d("chatRoomLongitude", chatRoomData.getString("Longitude"));
                Log.d("chatRoomLatitude", chatRoomData.getString("Latitude"));
                Log.d("chatRoomCreatedDate", chatRoomData.getString("CreatedDate"));
                Log.d("chatRoomUpdatedDate", chatRoomData.getString("UpdatedDate"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BaseActivity.ShowDoubleArray(db.selectQuery("select * from Chat;"));

        if (serverChatPkey <= localChatPkey) {
            //update
//            db.query("update Chat set ");

        } else {
            //insert
//            db.query("insert into Chat values ()");

        }

        if (serverChatRoomPkey <= localChatRoomPkey) {
            //update


        } else {
            //insert

        }

        if (serverChatMemberPkey <= localChatMemberPkey) {
            //update


        } else {
            //insert
//            db.query("insert into ");
        }


    }

}
