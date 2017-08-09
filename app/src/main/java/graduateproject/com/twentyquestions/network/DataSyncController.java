package graduateproject.com.twentyquestions.network;

import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private ArrayList<BasicNameValuePair> parsedDataList;

    public void updateData(String response) {

        ParseData parse = new ParseData();
        parsedDataList = new ArrayList();
        DBSI db = new DBSI();

//        response = "{\"Result\":\"GETFULLDATA\",\"ChatRoom\":[],\"Chat\":[],\"ChatMember\":[]}";

        BasicNameValuePair result = parse.parseDataToPair(response, "Result");
        BasicNameValuePair chat = parse.parseDataToPair(response, "Chat");
        BasicNameValuePair chatRoom = parse.parseDataToPair(response, "ChatRoom");
        BasicNameValuePair chatMember = parse.parseDataToPair(response, "ChatMember");
//        Log.d("ParsingTest", parse.parseJsonObject(response, "ChatRoom"));

        String parsedResult = "";

        parsedResult = parse.parseJsonObject(response, "Result").toString();
        Log.d("parsedResult", parsedResult);
        String parsedChatRoom1 = parse.parseJsonObject(response, "ChatRoom");
        JSONArray parsedChatRoom2 = parse.parseJsonArray(parsedChatRoom1);
        String parsedChatRoom3;
        String parsedChatRoom4;

        for(int i =0; i< parsedChatRoom2.length(); i++) {

            try {
                Log.d("JsonArray", parsedChatRoom2.get(i).toString());
                parsedChatRoom3 = parse.parseJsonObject(parsedChatRoom2.get(i).toString(), "chatroom");
                Log.d("Test chatroom", parsedChatRoom3);
                parsedChatRoom4 = parse.parseJsonObject(parsedChatRoom3.toString(), "PKey");
                Log.d("Test PKey", parsedChatRoom4);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

//        for(int i=0; i<par)

//        Log.d("parseChatRoom Test", parsedChatRoom);


        chatRoom.getValue();

        try {
            JSONArray jsonArray = new JSONArray(chatRoom.getValue());

            for(int i=0; i<jsonArray.length(); i++) {

                JSONObject chatRoonJson = new JSONObject(jsonArray.get(i).toString());
                parsedDataList = parse.parseDataToList(chatRoonJson.toString(), "chatroom");

            }


            Log.d("NameValuePair Result", result.getValue());
            Log.d("NameValuePair Chat", chat.getValue());
            Log.d("NameValuePair ChatRoom", chatRoom.getValue());
//            Log.d("Parsed ChatRoom", );
            Log.d("NameValuePair ChatMem", chatMember.getValue());

            for(int i = 0; i< parsedDataList.size(); i++) {

                Log.d("ArrayList", parsedDataList.get(i).getValue());
//                ArrayList<BasicNameValuePair> pair = (ArrayList<BasicNameValuePair>) parsedDataList;
//                Log.d("test", pair.get(i).getValue());


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(result != null)


        BaseActivity.ShowDoubleArray(db.selectQuery("select * from Chat;"));

        if(serverChatPkey <= localChatPkey) {
            //update
//            db.query("update Chat set ");

        } else {
            //insert
//            db.query("insert into Chat values ()");

        }

        if(serverChatRoomPkey <= localChatRoomPkey) {
            //update


        } else {
            //insert

        }

        if(serverChatMemberPkey <= localChatMemberPkey) {
            //update


        } else {
            //insert
//            db.query("insert into ");
        }



    }

}
