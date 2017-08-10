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

    private int localChatPKey = 0;
    private int localChatRoomPkey = 0;
    private int localChatMemberPkey = 0;

    private int serverChatPkey = 0;
    private int serverChatRoomPkey = 0;
    private int serverChatMemberPkey = 0;

    private JSONArray chatRoomArray;
    private JSONObject chatRoomData;

    private JSONArray chatArray;
    private JSONObject chatData;

    private JSONArray chatMemberArray;
    private JSONObject chatMemberData;

    private String reesult = "";

    public void updateData(String response) {

        ParseData parse = new ParseData();
        DBSI db = new DBSI();

        try {

            reesult = parse.parseJsonObject(response, "Result");
            Log.d("reesult", reesult);

            chatRoomArray = parse.jsonArrayInObject(response, "ChatRoom");
            chatArray = parse.jsonArrayInObject(response, "Chat");
            chatMemberArray = parse.jsonArrayInObject(response, "ChatMember");

            String[] userInfo = db.getUserInfo().split("/");
//            System.out.println(Integer.parseInt(userInfo[0]));

            String[][] localChatRoomArray = db.selectQuery("select * from ChatRoom");
            String[][] localChatArray = db.selectQuery("select * from Chat");
            String[][] localChatMemberArray = db.selectQuery("select * from ChatMember");

            BaseActivity.ShowDoubleArray(localChatRoomArray);
            BaseActivity.ShowDoubleArray(localChatArray);
            BaseActivity.ShowDoubleArray(localChatMemberArray);

            System.out.println("========================================================================================================================================");

            for (int i = 0; i < chatRoomArray.length(); i++) {

                Log.d("JsonArray", chatRoomArray.get(i).toString());
                chatRoomData = parse.doubleJsonObject(chatRoomArray.get(i).toString(), "chatroom");

                Log.d("chatRoomPkey", chatRoomData.getString("PKey"));
                Log.d("chatRoomName", chatRoomData.getString("Name"));
                Log.d("chatRoomLongitude", chatRoomData.getString("Longitude"));
                Log.d("chatRoomLatitude", chatRoomData.getString("Latitude"));
                Log.d("chatRoomCreatedDate", chatRoomData.getString("CreatedDate"));
                Log.d("chatRoomUpdatedDate", chatRoomData.getString("UpdatedDate"));

//                if(chatRoomData.getInt("PKey") <= localChatRoomPKey) {
//                    db.query("");
//
//                } else {
//                    db.query("");
//
//                }

                System.out.println("========================================================================================================================================");

            }

            System.out.println("========================================================================================================================================");

            for(int i=0; i<chatArray.length(); i++) {
                chatData = parse.doubleJsonObject(chatArray.get(i).toString(), "chat");

                Log.d("chatPkey", chatData.getString("PKey"));
                Log.d("chatRoomPKey", chatData.getString("ChatRoomPKey"));
                Log.d("chatUserPKey", chatData.getString("UserPKey"));
                Log.d("chatLatitude", chatData.getString("ChatText"));
                Log.d("chatCount", chatData.getString("Count"));
                Log.d("chatType", chatData.getString("Type"));
                Log.d("chatCreatedDate", chatData.getString("CreatedDate"));

//                if(chatData.getInt("PKey") <= localChatPKey) {
//                    db.query("");
//
//                } else {
//                    db.query("");
//
//                }

                System.out.println("========================================================================================================================================");

            }

            System.out.println("========================================================================================================================================");

            for(int i=0; i<chatMemberArray.length(); i++) {

                chatMemberData = parse.doubleJsonObject(chatMemberArray.get(i).toString(), "chatmember");

                Log.d("chatMemberPkey", chatMemberData.getString("PKey"));
                Log.d("chatMemberName", chatMemberData.getString("RoomPKey"));
                Log.d("chatMemberStatus", chatMemberData.getString("Status"));
                Log.d("chatMemberNotify", chatMemberData.getString("Notify"));
                Log.d("chatMemberCreatedDate", chatMemberData.getString("CreatedDate"));
                Log.d("chatMemberUpdatedDate", chatMemberData.getString("UpdatedDate"));

//                if(chatMemberData.getInt("PKey") <= localChatMemberPkey) {
//                    db.query("");
//
//                } else {
//                    db.query("");
//
//                }

                System.out.println("========================================================================================================================================");

            }

            System.out.println("========================================================================================================================================");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
