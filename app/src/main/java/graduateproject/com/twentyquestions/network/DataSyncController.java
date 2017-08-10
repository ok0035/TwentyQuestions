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

    private String[][] localChatRoomArray;
    private String[][] localChatArray;
    private String[][] localChatMemberArray;

    private int localChatPKey = 0;
    private int localChatRoomPKey = 0;
    private int localChatMemberPKey = 0;

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

            localChatRoomArray = db.selectQuery("select * from ChatRoom");
            localChatArray = db.selectQuery("select * from Chat");
            localChatMemberArray = db.selectQuery("select * from ChatMember");

            localChatRoomPKey = Integer.parseInt(localChatRoomArray[localChatRoomArray.length-1][0]);
            localChatPKey = Integer.parseInt(localChatArray[localChatArray.length -1][0]);
            localChatMemberPKey = Integer.parseInt(localChatMemberArray[localChatMemberArray.length-1][0]);

            Log.d("localChatRoomPKey", localChatRoomPKey + "");
            Log.d("localChatPKey", localChatPKey + "");
            Log.d("localChatMemberPKey", localChatMemberPKey + "");

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

                if (localChatRoomArray != null) {

                    if (chatRoomData.getInt("PKey") <= localChatRoomPKey) {

                        db.query("update ChatRoom set PKey = \'" + chatRoomData.getString("PKey") + "\', Name = \'" + chatRoomData.getString("Name") + "\', " +
                                "Longitude = \'"+ chatRoomData.getString("Longitude") +"\', Latitude = \'"+ chatRoomData.getString("Latitude") +"\', "+
                                "CreatedDate = \'"+ chatRoomData.getString("CreatedDate") +"\', UpdatedDate = \'"+ chatRoomData.getString("UpdatedDate") +"\', "+
                                "Description = \'"+ chatRoomData.getString("Description") +"\' where PKey = " + chatRoomData.getString("PKey"));
                    } else {

                        db.query("insert into ChatRoom(PKey, Name, Longitude, Latitude, CreatedDate, UpdatedDate, Description) values (\'" +
                                chatRoomData.getString("PKey") + "\', \'" + chatRoomData.getString("Name") + "\', \'" + chatRoomData.getString("Longitude") + "\', \'" + chatRoomData.getString("Latitude") +
                                "\', \'" +chatRoomData.getString("CreatedDate") + "\', \'" + chatRoomData.getString("UpdatedDate") + "\',\'" + chatRoomData.getString("Description") + "\')");
                    }

                } else {

                    db.query("insert into ChatRoom(PKey, Name, Longitude, Latitude, CreatedDate, UpdatedDate, Description) values (\'" +
                                chatRoomData.getString("PKey") + "\', \'" + chatRoomData.getString("Name") + "\', \'" + chatRoomData.getString("Longitude") + "\', \'" + chatRoomData.getString("Latitude") +
                                "\', \'" +chatRoomData.getString("CreatedDate") + "\', \'" + chatRoomData.getString("UpdatedDate") + "\',\'" + chatRoomData.getString("Description") + "\')");

                }

                System.out.println("========================================================================================================================================");

            }

            System.out.println("========================================================================================================================================");

            for (int i = 0; i < chatArray.length(); i++) {
                chatData = parse.doubleJsonObject(chatArray.get(i).toString(), "chat");

                Log.d("chatPkey", chatData.getString("PKey"));
                Log.d("chatRoomPKey", chatData.getString("ChatRoomPKey"));
                Log.d("chatUserPKey", chatData.getString("UserPKey"));
                Log.d("chatLatitude", chatData.getString("ChatText"));
                Log.d("chatCount", chatData.getString("Count"));
                Log.d("chatType", chatData.getString("Type"));
                Log.d("chatCreatedDate", chatData.getString("CreatedDate"));

                if (localChatRoomArray != null) {

                    if (chatData.getInt("PKey") <= localChatPKey) {

                        db.query("update Chat set PKey = \'" + chatData.getString("PKey") + "\', ChatRoomPKey = \'" + chatData.getString("ChatRoomPKey") + "\', " +
                                "UserPKey = \'"+ chatData.getString("UserPKey") +"\', ChatText = \'"+ chatData.getString("ChatText") +"\', "+
                                "Count = \'"+ chatData.getString("Count") +"\', Type = \'"+ chatData.getString("Type") +"\', "+
                                "CreatedDate = \'"+ chatData.getString("CreatedDate") +"\' where PKey = " + chatData.getString("PKey"));
                    } else {

                        db.query("insert into Chat(PKey, ChatRoomPKey, UserPKey, ChatText, Count, Type, CreatedDate) values (\'" +
                                chatData.getString("PKey") + "\', \'" + chatData.getString("ChatRoomPKey") + "\', \'" + chatData.getString("UserPKey") + "\', \'" + chatData.getString("ChatText") +
                                "\', \'" +chatData.getString("Count") + "\', \'" +chatData.getString("Type") + "\', \'" + chatData.getString("CreatedDate") + "\')");
                    }

                } else {

                    db.query("insert into Chat(PKey, ChatRoomPKey, UserPKey, ChatText, Count, Type, CreatedDate) values (\'" +
                            chatData.getString("PKey") + "\', \'" + chatData.getString("ChatRoomPKey") + "\', \'" + chatData.getString("UserPKey") + "\', \'" + chatData.getString("ChatText") +
                            "\', \'" +chatData.getString("Count") + "\', \'" +chatData.getString("Type") + "\', \'" + chatData.getString("CreatedDate") + "\')");

                }

                System.out.println("========================================================================================================================================");

            }

            System.out.println("========================================================================================================================================");

            for (int i = 0; i < chatMemberArray.length(); i++) {

                chatMemberData = parse.doubleJsonObject(chatMemberArray.get(i).toString(), "chatmember");

                Log.d("chatMemberPkey", chatMemberData.getString("PKey"));
                Log.d("chatMemberName", chatMemberData.getString("RoomPKey"));
                Log.d("chatMemberStatus", chatMemberData.getString("Status"));
                Log.d("chatMemberNotify", chatMemberData.getString("Notify"));
                Log.d("chatMemberCreatedDate", chatMemberData.getString("CreatedDate"));
                Log.d("chatMemberUpdatedDate", chatMemberData.getString("UpdatedDate"));

                if (localChatMemberArray != null) {

                    if (chatMemberData.getInt("PKey") <= localChatMemberPKey) {

                        db.query("update ChatMember set PKey = \'" + chatMemberData.getString("PKey") + "\', RoomPKey = \'" + chatMemberData.getString("RoomPKey") + "\', " +
                                "Status = \'"+ chatMemberData.getString("Status") +"\', Notify= \'"+ chatMemberData.getString("Notify") +"\', "+
                                "CreatedDate = \'"+ chatMemberData.getString("CreatedDate") +"\', UpdatedDate = \'"+ chatMemberData.getString("UpdatedDate") +"\' " +
                                "where PKey = " + chatMemberData.getString("PKey"));

                    } else {

                        db.query("insert into ChatMember(PKey, UserPKey, RoomPKey, Status, Notify, CreatedDate, UpdatedDate) values (\'" +
                                chatMemberData.getString("PKey") + "\', \'" + chatMemberData.getString("UserPKey") + "\', \'" + chatMemberData.getString("RoomPKey") + "\', \'" + chatMemberData.getString("Status") + "\', \'" + chatMemberData.getString("Notify") +
                                "\', \'" +chatMemberData.getString("CreatedDate") + "\', \'" + chatMemberData.getString("UpdatedDate") + "\')");
                    }

                } else {

                    db.query("insert into ChatMember(PKey, UserPKey, RoomPKey, Status, Notify, CreatedDate, UpdatedDate) values (\'" +
                            chatMemberData.getString("PKey") + "\', \'" + chatMemberData.getString("UserPKey") + "\', \'" + chatMemberData.getString("RoomPKey") + "\', \'" + chatMemberData.getString("Status") + "\', \'" + chatMemberData.getString("Notify") +
                            "\', \'" +chatMemberData.getString("CreatedDate") + "\', \'" + chatMemberData.getString("UpdatedDate") + "\')");

                }

                System.out.println("========================================================================================================================================");

            }

            System.out.println("========================================================================================================================================");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
