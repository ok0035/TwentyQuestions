package graduateproject.com.twentyquestions.controller;

/**
 * Created by Heronation on 2017-08-07.
 */


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import graduateproject.com.twentyquestions.activity.BaseActivity;
import graduateproject.com.twentyquestions.network.DBSI;
import graduateproject.com.twentyquestions.util.ParseData;

public class LoginController {

    private JSONArray userArray;
    private JSONObject userData;
    private String stringResult = "";
    private String stringState = "";
    private String[] userInfo;

    public boolean parseLoginData(String responseData) {

        ParseData parseData = new ParseData();
        final DBSI db = new DBSI(BaseActivity.mContext, "TwentyQuestions.db", null, 1);
        userInfo = db.getUserInfo().split("/");

        try {
            stringResult = parseData.parseJsonObject(responseData, "Result");
            stringState = parseData.parseJsonObject(responseData, "State");
            userArray = parseData.jsonArrayInObject(responseData, "User");

            if (stringResult.equals("TRYREGIST") || stringResult.equals("TRYLOGIN")) { // TryRegist의 result 값인지 확인
                if (stringState.equals("REGIST_SUCCESS") || stringState.equals("LOGIN_SUCCESS")) {
                    Log.d("stringResult", stringResult);
                    Log.d("stringState", stringState);
                    ArrayList<String> userDataKeyList = new ArrayList<>();

                    // Insert쿼리 문 만들자. => TRYREGIST SYNC
                    String defaultQuery_Insert1 = "INSERT INTO User (";
                    String addedNameQuery_insert = "";
                    String defalutQuery_Insert2 = ") VALUES (";
                    String addedValueQuery_insert = "";

                    // Update쿼리 문 만들자. => TRYLOGIN SYNC
                    String defaultQuery_Update1 = "UPDATE User SET ";
                    String addedNameValueQuery_update = "";
                    String defaultQuery_Update2 = " WHERE PKey = " + userInfo[0];

                    for (int a = 0; a < userArray.length(); a++) {
                        userData = parseData.doubleJsonObject(userArray.get(a).toString(), "user");
                        Iterator iterator = userData.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next().toString();
                            userDataKeyList.add(key);
                        }
                        for (int b = 0; b < userDataKeyList.size(); b++) {
                            if (b == userDataKeyList.size() - 1) {
                                addedNameQuery_insert += "'" + userDataKeyList.get(b) + "'";
                                addedValueQuery_insert += "'" + userData.getString(userDataKeyList.get(b)) + "')";
                                addedNameValueQuery_update +=
                                        "'" + userDataKeyList.get(b) + "' = "
                                                + "'" + userData.getString(userDataKeyList.get(b)) + "'";
                            } else {
                                addedNameQuery_insert += "'" + userDataKeyList.get(b) + "',";
                                addedValueQuery_insert += "'" + userData.getString(userDataKeyList.get(b)) + "',";
                                addedNameValueQuery_update +=
                                        "'" + userDataKeyList.get(b) + "' = "
                                        + "'" + userData.getString(userDataKeyList.get(b)) + "',";

                            }

                        }
                    }

                    if(stringState.equals("REGIST_SUCCESS")){
                        String finalQuery = defaultQuery_Insert1 + addedNameQuery_insert + defalutQuery_Insert2 + addedValueQuery_insert;
                        Log.d("Query,,,,", finalQuery);

                        db.query(finalQuery);
                        String[][] localUserArray = db.selectQuery("select * from User");
                        BaseActivity.ShowDoubleArray("localUserArray..... ",localUserArray);

                    }else if(stringState.equals("LOGIN_SUCCESS")){
                        String finalQuery = defaultQuery_Update1 + addedNameValueQuery_update + defaultQuery_Update2;
                        Log.d("Query,,,,", finalQuery);

                        db.query(finalQuery);
                        String[][] localUserArray = db.selectQuery("select * from User");
                        BaseActivity.ShowDoubleArray("localUserArray.....", localUserArray);

                    }else{
                        return false;
                    }
//                    Log.d("Query,,,,Update",defaultQuery_Update1 + addedNameValueQuery_update + defaultQuery_Update2);

                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

}