package graduateproject.com.twentyquestions.controller;

/**
 * Created by Heronation on 2017-08-07.
 */


import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.util.ParseData;

public class LoginController {

    private ArrayList<ArrayList<BasicNameValuePair>> parseList;


    public ArrayList<ArrayList<BasicNameValuePair>> getParseList() {
        return parseList;
    }

    public boolean parseLoginData(String responseData) {
//        String Result = "";
//        String State = "";
//        parseList = new ArrayList<>();
//
//        try {
//            jsonResponse = new JSONObject(responseData);
//
//
//            Result = jsonResponse.getString("Result");
//            State = jsonResponse.getString("State");
//
//
//            if (Result.equals("TRYREGIST") || Result.equals("TRYLOGIN")) { // TryRegist의 result 값인지 확인
//                if (State.equals("REGIST_SUCCESS") || State.equals("LOGIN_SUCCESS")) { // 회원가입, 로그인 성공인지 확인
//
//                    JSONArray tableArray = jsonResponse.getJSONArray("User");
//                    Iterator iterator = tableArray.getJSONObject(0).keys();
//                    ArrayList<String> keyList = new ArrayList<>();
//                    while (iterator.hasNext()) {
//                        String key = iterator.next().toString();
//                        keyList.add(key);
//                    }
//                    for (int i = 0; i < tableArray.length(); i++) {
//                        for (int j = 0; j < keyList.size(); j++) {
//                            String value = tableArray.getJSONObject(i).getString(keyList.get(j));
//                            parseList.add(new BasicNameValuePair(keyList.get(j), value));
//                        }
//                    }
//
//
//
//
////                    // 파싱 테스트용 //
////                    for(BasicNameValuePair basicNameValuePair : parseList){
////                        Log.d("Key : ", basicNameValuePair.getName());
////                        Log.d("value : ", basicNameValuePair.getValue());
////                    }
//
//
//                } else {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
        ParseData Result = new ParseData();


        parseList = new ArrayList<>();

        String stringResult = Result.parseDataToPair(responseData, "Result").getValue();
        String stringState = Result.parseDataToPair(responseData, "State").getValue();

        if (stringResult.equals("TRYREGIST") || stringResult.equals("TRYLOGIN")) { // TryRegist의 result 값인지 확인
            if (stringState.equals("REGIST_SUCCESS") || stringState.equals("REGIST_SUCCESS")) {
                Log.d("stringResult", stringResult);
                Log.d("stringState", stringState);

                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray= jsonObject.getJSONArray("User"); // 테이블 명 데이터 가져오기
                    for(int i = 0 ; i < jsonArray.length() ; i++){
                        ArrayList<BasicNameValuePair> pair = Result.parseDataToList(jsonArray.getJSONObject(i),"user"); //레코드 명 데이터 가져오기
                        parseList.add(pair);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                return false;
            }
        } else {
            return false;
        }


        return true;
    }

}