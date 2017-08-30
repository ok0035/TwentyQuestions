package graduateproject.com.twentyquestions.controller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.util.ParseData;
import graduateproject.com.twentyquestions.view.BaseActivity;

/**
 * Created by yrs00 on 2017-08-29.
 */

public class ChatContoller {

    private String stringResult;

    private JSONArray chatArray;

    public JSONObject getChatData() {
        return chatData;
    }

    private JSONObject chatData;
    private DBSI db;
    private String finalQuery;

    public String getFinalQuery(){
        return finalQuery;
    }



    public boolean parseChatData(String responseData) {

        ParseData parseData = new ParseData();

        db = new DBSI();

        try {
            stringResult = parseData.parseJsonObject(responseData, "Result");

            chatArray = parseData.jsonArrayInObject(responseData, "Chat");

            if (stringResult.equals("SENDCHATDATA")) { // TryRegist의 SENDCHATDATA값인지 확인

                Log.d("stringResult", stringResult);

                ArrayList<String> chatDataKeyList = new ArrayList<>();

                // Insert쿼리 문 만들자. => TRYREGIST SYNC
                String defaultQuery_Insert1 = "INSERT INTO Chat (";
                String addedNameQuery_insert = "";
                String defalutQuery_Insert2 = ") VALUES (";
                String addedValueQuery_insert = "";


                for (int a = 0; a < chatArray.length(); a++) {
                    chatData = parseData.doubleJsonObject(chatArray.get(a).toString(), "chat");
                    Iterator iterator = chatData.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        chatDataKeyList.add(key);
                    }
                    for (int b = 0; b < chatDataKeyList.size(); b++) {
                        if (b == chatDataKeyList.size() - 1) {
                            addedNameQuery_insert += "'" + chatDataKeyList.get(b) + "'";
                            addedValueQuery_insert += "'" + chatData.getString(chatDataKeyList.get(b)) + "')";

                        } else {
                            addedNameQuery_insert += "'" + chatDataKeyList.get(b) + "',";
                            addedValueQuery_insert += "'" + chatData.getString(chatDataKeyList.get(b)) + "',";

                        }

                    }
                }

                finalQuery = defaultQuery_Insert1 + addedNameQuery_insert + defalutQuery_Insert2 + addedValueQuery_insert;

            } else {
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }


}
