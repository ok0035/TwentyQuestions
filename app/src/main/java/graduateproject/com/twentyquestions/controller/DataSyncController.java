package graduateproject.com.twentyquestions.controller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.network.DataSync;
import graduateproject.com.twentyquestions.util.ParseData;

/**
 * Created by mapl0 on 2017-08-03.
 */

public class DataSyncController {

    private String[][] localChatRoomArray;
    private String[][] localChatArray;
    private String[][] localChatMemberArray;
    private String[][] localGameListArray;
    private String[][] localGameMemberArray;

    private int localChatPKey = 0;
    private int localChatRoomPKey = 0;
    private int localChatMemberPKey = 0;
    private int localGameListPKey = 0;
    private int localGameMemberPKey = 0;

    private int serverChatPkey = 0;
    private int serverChatRoomPkey = 0;
    private int serverChatMemberPkey = 0;
    private int serverGameListPKey = 0;
    private int serverGameMemberPKey = 0;

    private JSONArray chatRoomArray;
    private JSONObject chatRoomData;

    private JSONArray chatArray;
    private JSONObject chatData;

    private JSONArray chatMemberArray;
    private JSONObject chatMemberData;

    private JSONArray gameListArray;
    private JSONObject gameListData;

    private JSONArray gameMemberArray;
    private JSONObject gameMemberData;

    private String result = "";

    public void updateData(String response) {

        ParseData parse = new ParseData();
        DBSI db = new DBSI();

        try {

            if (!response.equals(DataSync.Command.LOGINFAILED.toString())) {

                result = parse.parseJsonObject(response, "Result");
                Log.d("result", result);

                chatRoomArray = parse.jsonArrayInObject(response, "ChatRoom");
                chatArray = parse.jsonArrayInObject(response, "Chat");
                chatMemberArray = parse.jsonArrayInObject(response, "ChatMember");
                gameListArray = parse.jsonArrayInObject(response, "GameList");
                gameMemberArray = parse.jsonArrayInObject(response, "GameMember");

                System.out.println("========================================================================================================================================");

                Log.d("chatRoomArrayLength", chatRoomArray.length() + "");
                for (int i = 0; i < chatRoomArray.length(); i++) {

                    Log.d("JsonArray", chatRoomArray.get(i).toString());
                    chatRoomData = parse.doubleJsonObject(chatRoomArray.get(i).toString(), "chatroom");

                    Log.d("chatRoomPkey", chatRoomData.getString("PKey"));
//                    Log.d("chatRoomName", chatRoomData.getString("Name"));
//                    Log.d("chatRoomLongitude", chatRoomData.getString("Longitude"));
//                    Log.d("chatRoomLatitude", chatRoomData.getString("Latitude"));
//                    Log.d("chatRoomCreatedDate", chatRoomData.getString("CreatedDate"));
//                    Log.d("chatRoomUpdatedDate", chatRoomData.getString("UpdatedDate"));

                    localChatRoomArray = db.selectQuery("select * from ChatRoom");

//                    BaseActivity.ShowDoubleArray("selectLocal.....",localChatRoomArray);

                    if (localChatRoomArray != null && localChatRoomArray.length > 0) {

                        localChatRoomPKey = Integer.parseInt(localChatRoomArray[localChatRoomArray.length - 1][0]);

                        Log.d("serverRoomPKey", chatRoomData.getInt("PKey") + "");
                        Log.d("localChatRoomPKey", localChatRoomPKey + "");
                        if (chatRoomData.getInt("PKey") <= localChatRoomPKey) {
                            Log.d("Update", "............................Update");

                            String query = "update ChatRoom set PKey = \'" + chatRoomData.getString("PKey") + "\', Name = \'" + chatRoomData.getString("Name") + "\', " +
                                    "Longitude = \'" + chatRoomData.getString("Longitude") + "\', Latitude = \'" + chatRoomData.getString("Latitude") + "\', " +
                                    "CreatedDate = \'" + chatRoomData.getString("CreatedDate") + "\', UpdatedDate = \'" + chatRoomData.getString("UpdatedDate") + "\', " +
                                    "Description = \'" + chatRoomData.getString("Description") + "\' where PKey = " + chatRoomData.getString("PKey");

                            Log.d("RoomUpdateQuery", query);

                            db.query(query);

                        } else {

                            Log.d("Insert", "............................Insert");

                            String query = "insert into ChatRoom(PKey, Name, Longitude, Latitude, CreatedDate, UpdatedDate, Description) values (\'" +
                                    chatRoomData.getString("PKey") + "\', \'" + chatRoomData.getString("Name") + "\', \'" + chatRoomData.getString("Longitude") + "\', \'" + chatRoomData.getString("Latitude") +
                                    "\', \'" + chatRoomData.getString("CreatedDate") + "\', \'" + chatRoomData.getString("UpdatedDate") + "\',\'" + chatRoomData.getString("Description") + "\')";

                            Log.d("RoomInsertQuery", query);

                            db.query(query);

                        }

                    } else {

                        String query = "insert into ChatRoom(PKey, Name, Longitude, Latitude, CreatedDate, UpdatedDate, Description) values (\'" +
                                chatRoomData.getString("PKey") + "\', \'" + chatRoomData.getString("Name") + "\', \'" + chatRoomData.getString("Longitude") + "\', \'" + chatRoomData.getString("Latitude") +
                                "\', \'" + chatRoomData.getString("CreatedDate") + "\', \'" + chatRoomData.getString("UpdatedDate") + "\',\'" + chatRoomData.getString("Description") + "\')";

                        Log.d("RoomNullInsertQuery", query);

                        db.query(query);

                    }

//                    BaseActivity.ShowDoubleArray("RoomNull........", localChatRoomArray);

                    System.out.println("========================================================================================================================================");

                }

                System.out.println("========================================================================================================================================");

                for (int i = 0; i < chatArray.length(); i++) {
                    chatData = parse.doubleJsonObject(chatArray.get(i).toString(), "chat");

                    Log.d("chatPkey", chatData.getString("PKey"));
//                    Log.d("chatRoomPKey", chatData.getString("ChatRoomPKey"));
//                    Log.d("chatUserPKey", chatData.getString("UserPKey"));
//                    Log.d("chatLatitude", chatData.getString("ChatText"));
//                    Log.d("chatCount", chatData.getString("Count"));
//                    Log.d("chatType", chatData.getString("Type"));
//                    Log.d("chatCreatedDate", chatData.getString("CreatedDate"));

                    localChatArray = db.selectQuery("select * from Chat");

                    if (localChatArray != null) {

                        localChatPKey = Integer.parseInt(localChatArray[localChatArray.length - 1][0]);

                        Log.d("serverChatPKey", chatData.getInt("PKey") + "");
                        Log.d("localChatPKey", localChatPKey + "");

                        if (chatData.getInt("PKey") > localChatPKey) {
                            Log.d("Insert", "............................Insert");
                            String query = "insert into Chat(PKey, ChatRoomPKey, UserPKey, ChatText, Count, Type, CreatedDate) values (\'" +
                                    chatData.getString("PKey") + "\', \'" + chatData.getString("ChatRoomPKey") + "\', \'" + chatData.getString("UserPKey") + "\', \'" + chatData.getString("ChatText") +
                                    "\', \'" + chatData.getString("Count") + "\', \'" + chatData.getString("Type") + "\', \'" + chatData.getString("CreatedDate") + "\')";

                            Log.d("ChatInsertQuery", query);
                            db.query(query);
                        }

                    } else {
                        String query = "insert into Chat(PKey, ChatRoomPKey, UserPKey, ChatText, Count, Type, CreatedDate) values (\'" +
                                chatData.getString("PKey") + "\', \'" + chatData.getString("ChatRoomPKey") + "\', \'" + chatData.getString("UserPKey") + "\', \'" + chatData.getString("ChatText") +
                                "\', \'" + chatData.getString("Count") + "\', \'" + chatData.getString("Type") + "\', \'" + chatData.getString("CreatedDate") + "\')";

                        Log.d("ChatNullInsertQuery", query);
                        db.query(query);

                    }

//                    BaseActivity.ShowDoubleArray("localChatArray.........", localChatArray);

                    System.out.println("========================================================================================================================================");

                }

                System.out.println("========================================================================================================================================");

                Log.d("ChatMemberArrayLength", chatMemberArray.length() + "");
                for (int i = 0; i < chatMemberArray.length(); i++) {

                    chatMemberData = parse.doubleJsonObject(chatMemberArray.get(i).toString(), "chatmember");

                    Log.d("chatMemberPkey", chatMemberData.getString("PKey"));
//                    Log.d("chatMemberName", chatMemberData.getString("RoomPKey"));
//                    Log.d("chatMemberStatus", chatMemberData.getString("Status"));
//                    Log.d("chatMemberNotify", chatMemberData.getString("Notify"));
//                    Log.d("chatMemberCreatedDate", chatMemberData.getString("CreatedDate"));
//                    Log.d("chatMemberUpdatedDate", chatMemberData.getString("UpdatedDate"));

                    localChatMemberArray = db.selectQuery("select * from ChatMember");

                    if (localChatMemberArray != null) {

                        localChatMemberPKey = Integer.parseInt(localChatMemberArray[localChatMemberArray.length - 1][0]);

                        Log.d("serverMemberPKey", chatMemberData.getInt("PKey") + "");
                        Log.d("localMemberPKey", localChatMemberPKey + "");
                        if (chatMemberData.getInt("PKey") <= localChatMemberPKey) {
                            Log.d("Update", "............................Update");

                            String query = "update ChatMember set PKey = \'" + chatMemberData.getString("PKey") + "\', RoomPKey = \'" + chatMemberData.getString("RoomPKey") + "\', " +
                                    "Status = \'" + chatMemberData.getString("Status") + "\', Notify= \'" + chatMemberData.getString("Notify") + "\', " +
                                    "CreatedDate = \'" + chatMemberData.getString("CreatedDate") + "\', UpdatedDate = \'" + chatMemberData.getString("UpdatedDate") + "\' " +
                                    "where PKey = " + chatMemberData.getString("PKey");

                            Log.d("MemberUpdateQuery", query);
                            db.query(query);

                        } else {
                            Log.d("Insert", "............................Insert");

                            String query = "insert into ChatMember(PKey, UserPKey, RoomPKey, Status, Notify, CreatedDate, UpdatedDate) values (\'" +
                                    chatMemberData.getString("PKey") + "\', \'" + chatMemberData.getString("UserPKey") + "\', \'" + chatMemberData.getString("RoomPKey") + "\', \'" + chatMemberData.getString("Status") + "\', \'" + chatMemberData.getString("Notify") +
                                    "\', \'" + chatMemberData.getString("CreatedDate") + "\', \'" + chatMemberData.getString("UpdatedDate") + "\')";

                            db.query(query);
                            Log.d("MemberInsertQuery", query);

                        }

                    } else {

                        String query = "insert into ChatMember(PKey, UserPKey, RoomPKey, Status, Notify, CreatedDate, UpdatedDate) values (\'" +
                                chatMemberData.getString("PKey") + "\', \'" + chatMemberData.getString("UserPKey") + "\', \'" + chatMemberData.getString("RoomPKey") + "\', \'" + chatMemberData.getString("Status") + "\', \'" + chatMemberData.getString("Notify") +
                                "\', \'" + chatMemberData.getString("CreatedDate") + "\', \'" + chatMemberData.getString("UpdatedDate") + "\')";

                        db.query(query);
                        Log.d("MemberNullInsertQuery", query);

                    }

//                    BaseActivity.ShowDoubleArray("localChatMemberArray...............", localChatMemberArray);

                    System.out.println("========================================================================================================================================");

                }

                System.out.println("========================================================================================================================================");

                Log.d("gameRoomArrayLength", gameListArray.length() + "");
                for (int i = 0; i < gameListArray.length(); i++) {

                    Log.d("JsonArray", gameListArray.get(i).toString());
                    gameListData = parse.doubleJsonObject(gameListArray.get(i).toString(), "gamelist");

                    Log.d("gameListPKey", gameListData.getString("PKey"));

                    localGameListArray = db.selectQuery("select * from GameList");

                    if (localGameListArray != null && localGameListArray.length > 0) {

                        localGameListPKey = Integer.parseInt(localGameListArray[localGameListArray.length - 1][0]);

                        Log.d("serverGameListPKey", gameListData.getInt("PKey") + "");
                        Log.d("localGameListPKey", localGameListPKey + "");
                        if (gameListData.getInt("PKey") <= localGameListPKey) {
                            Log.d("Update", "............................Update");

                            String query = "update GameList set PKey = \'" + gameListData.getString("PKey") + "\', ChatRoomPKey = \'" + gameListData.getString("ChatRoomPKey") +
                                    "\', GameTypePKey = \'" + gameListData.getString("GameTypePKey") + "\', Name = \'" + gameListData.getString("Name") +
                                    "\', Password = \'" + gameListData.getString("Password") + "\', Description = \'" + gameListData.getString("Description") +
                                    "\', Status = \'" + gameListData.getString("Status") +"\', GameStatus = \'" + gameListData.getString("GameStatus") +
                                    "\', MinMember = \'" + gameListData.getString("MinMember") + "\', MaxMember = \'" + gameListData.getString("MaxMember") +"\', " +
                                    "Longitude = \'" + gameListData.getString("Longitude") + "\', Latitude = \'" + gameListData.getString("Latitude") + "\', " +
                                    "CreatedDate = \'" + gameListData.getString("CreatedDate") + "\', UpdatedDate = \'" + gameListData.getString("UpdatedDate") + "\' " +
                                    "where PKey = " + gameListData.getString("PKey");

                            Log.d("GameListUpdateQuery", query);

                            db.query(query);

                        } else {

                            Log.d("Insert", "............................Insert");

                            String query = "insert into GameList values (\'" +
                                    gameListData.getString("PKey") + "\', \'" + gameListData.getString("ChatRoomPKey") + "\', \'" +
                                    gameListData.getString("GameTypePKey") + "\', \'" + gameListData.getString("Name") + "\', \'" + gameListData.getString("Password") + "\', \'" +
                                    gameListData.getString("Description") + "\', \'" + gameListData.getString("Status") + "\', \'" + gameListData.getString("GameStatus") + "\', \'" +
                                    gameListData.getString("MinMember") + "\', \'" + gameListData.getString("MaxMember") + "\', \'" + gameListData.getString("Longitude") + "\', \'" +
                                    gameListData.getString("Latitude") + "\', \'" + gameListData.getString("CreatedDate") + "\', \'" + gameListData.getString("UpdatedDate") + "\')";

                            Log.d("GameListInsertQuery", query);

                            db.query(query);

                        }

                    } else {

                        String query = "insert into GameList values (\'" +
                                gameListData.getString("PKey") + "\', \'" + gameListData.getString("ChatRoomPKey") + "\', \'" +
                                gameListData.getString("GameTypePKey") + "\', \'" + gameListData.getString("Name") + "\', \'" + gameListData.getString("Password") + "\', \'" +
                                gameListData.getString("Description") + "\', \'" + gameListData.getString("Status") + "\', \'" + gameListData.getString("GameStatus") + "\', \'" +
                                gameListData.getString("MinMember") + "\', \'" + gameListData.getString("MaxMember") + "\', \'" + gameListData.getString("Longitude") + "\', \'" +
                                gameListData.getString("Latitude") + "\', \'" + gameListData.getString("CreatedDate") + "\', \'" + gameListData.getString("UpdatedDate") + "\')";

                        Log.d("GameListNullInsertQuery", query);

                        db.query(query);

                    }

//                    BaseActivity.ShowDoubleArray("RoomNull........", localChatRoomArray);

                    System.out.println("========================================================================================================================================");

                }

                Log.d("gameMemberArrayLength", gameMemberArray.length() + "");
                for (int i = 0; i < gameMemberArray.length(); i++) {

                    Log.d("JsonArray", gameMemberArray.get(i).toString());
                    gameMemberData = parse.doubleJsonObject(gameMemberArray.get(i).toString(), "gamemember");

                    Log.d("gameMemberPKey", gameMemberData.getString("PKey"));

                    localGameMemberArray = db.selectQuery("select * from GameMember");

                    if (localGameMemberArray != null && localGameMemberArray.length > 0) {

                        localGameMemberPKey = Integer.parseInt(localGameMemberArray[localGameMemberArray.length - 1][0]);

                        Log.d("serverGameMemberPKey", gameMemberData.getInt("PKey") + "");
                        Log.d("localGameMemberPKey", localGameMemberPKey + "");
                        if (gameMemberData.getInt("PKey") <= localGameMemberPKey) {
                            Log.d("Update", "............................Update");

                            String query = "update GameMember set PKey = \'" + gameMemberData.getString("PKey") + "\', GameListPKey = \'" +
                                    gameMemberData.getString("GameListPKey") + "\', UserPKey = \'" + gameMemberData.getString("UserPKey") + "\', MemberPriority = \'" +
                                    gameMemberData.getString("MemberPriority") + "\', Status = \'" + gameMemberData.getString("Status") + "\', IsWinner = \'" +
                                    gameMemberData.getString("IsWinner") + "\', CreatedDate = \'" + gameMemberData.getString("CreatedDate") + "\', UpdatedDate = \'" +
                                    gameMemberData.getString("UpdatedDate") + "\' " + "where PKey = " + gameMemberData.getString("PKey");

                            Log.d("GameMemberUpdateQuery", query);

                            db.query(query);

                        } else {

                            Log.d("Insert", "............................Insert");

                            String query = "insert into GameMember values (\'" +
                                    gameMemberData.getString("PKey") + "\', \'" + gameMemberData.getString("GameListPKey") + "\', \'" +
                                    gameMemberData.getString("UserPKey") + "\', \'" + gameMemberData.getString("MemberPriority") + "\', \'" + gameMemberData.getString("Status") + "\', \'" +
                                    gameMemberData.getString("IsWinner") + "\', \'" + gameMemberData.getString("CreatedDate") + "\', \'" + gameMemberData.getString("UpdatedDate") + "\')";

                            Log.d("GameMemberInsertQuery", query);

                            db.query(query);

                        }

                    } else {

                        String query = "insert into GameMember values (\'" +
                                gameMemberData.getString("PKey") + "\', \'" + gameMemberData.getString("GameListPKey") + "\', \'" +
                                gameMemberData.getString("UserPKey") + "\', \'" + gameMemberData.getString("MemberPriority") + "\', \'" + gameMemberData.getString("Status") + "\', \'" +
                                gameMemberData.getString("IsWinner") + "\', \'" + gameMemberData.getString("CreatedDate") + "\', \'" + gameMemberData.getString("UpdatedDate") + "\')";

                        Log.d("GameMemNullInsertQuery", query);

                        db.query(query);

                    }

//                    BaseActivity.ShowDoubleArray("RoomNull........", localChatRoomArray);

                    System.out.println("========================================================================================================================================");

                }

            } else Log.d("LOGIN_ERR", "LOGIN_FAILED");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
