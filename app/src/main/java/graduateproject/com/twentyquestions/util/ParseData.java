package graduateproject.com.twentyquestions.util;

import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Brant on 2017-04-10.
 * PHP에서 받아온 데이터를 파싱해주는 클래스
 * [[a,b,c,[d,g,t]], [a,b,c,[d,g,t]]], [a,b,c] 와 같은 형식을 파싱해줌
 */

public class ParseData {

    private String mToParsingData;
    private String[][] mParsedDoubleArrayData;
    private String[] mParsedArrayData;
    private ArrayList<String[]> mParsedMergeDataList;

    public ParseData() {

    }

    //    인자 : 2차 배열 JSON Array (String)
    public String[][] getDoubleArrayData(String toParsingData) {

        mToParsingData = toParsingData;

        try {

            JSONArray jsonArray;
            jsonArray = new JSONArray(mToParsingData);

            mParsedDoubleArrayData = new String[jsonArray.length()][jsonArray.getJSONArray(0).length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                for (int j = 0; j < jsonArray.getJSONArray(i).length(); j++) {

                    mParsedDoubleArrayData[i][j] = jsonArray.getJSONArray(i).getString(j);
                    System.out.println("DoubleArrayData ==>>>>>>>>> " + mParsedDoubleArrayData[i][j]);
                }
            }

        } catch (JSONException e) {

            mParsedDoubleArrayData = new String[1][1];
            mParsedDoubleArrayData[0][0] = "";
            System.out.println("DoubleArrayData ==>>>>>>>>> " + mParsedDoubleArrayData[0][0]);
//            e.printStackTrace();

        }

        return mParsedDoubleArrayData;
    }

//    [d,g,t] 형식의 JSON String을 d,g,t 가 들어가 있는 배열형태로 반환해줌

    public String[] getArrayData(String parseData) {

        try {

            JSONArray json = new JSONArray(parseData);
            mParsedArrayData = new String[json.length()];
            System.out.println(json.length());
            for (int i = 0; i < json.length(); i++) {

                mParsedArrayData[i] = json.getString(i);
                System.out.println("ArrayData ==>>>>>>>>> " + mParsedArrayData[i]);
            }

        } catch (JSONException e) {
            mParsedArrayData = new String[1];
            mParsedArrayData[0] = "";
//            e.printStackTrace();
        }

        return mParsedArrayData;
    }


//  2중배열, 배열이 있는 요소 index
//  mProductListItem[i][6]에 스타일 배열이 있음 ["모던", "트렌디" ...]
//  다른 위치에 있는 배열들을 모아서 합친 후 리스트에 뿌려줌

    public ArrayList<String[]> getMergeArrayList(String[][] parsingData, int position) {

        System.out.println("parsingData ==>>> " + parsingData.length);

        mParsedMergeDataList = new ArrayList<>();

        for (int i = 0; i < parsingData.length; i++)
            mParsedMergeDataList.add(i, getArrayData(parsingData[i][position]));

        return mParsedMergeDataList;
    }

    private String KEY = "KEY";
    private JSONObject jsonResponse;

    private BasicNameValuePair parseObjectPair;
    private ArrayList<BasicNameValuePair> parsePairList;

    private String convertJSonObject = null;
    private JSONObject tableObject = null;


    //Result : GETFULLDATA등 Result의 값을 뽑아내는 메소드
    public BasicNameValuePair parseDataToPair(String response, String Key) {
        try {
            this.KEY = Key;
            this.jsonResponse = new JSONObject(response);
            convertJSonObject = jsonResponse.getString(KEY);
            parseObjectPair = new BasicNameValuePair(KEY, convertJSonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parseObjectPair;

    }

    // table의 값들을 뽑아내서 List<BasicNameValue>형태로 저장하는 메소드
    public ArrayList parseDataToList(String response, String Key) {
        try {
            this.KEY = Key;
            parsePairList = new ArrayList<>();
            response = response.replace("[", "");
            response = response.replace("]", "");
            JSONObject json = new JSONObject(response);
            tableObject = json.getJSONObject(KEY);
            Iterator iterator = tableObject.keys();
            ArrayList<String> keyList = new ArrayList<>();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                keyList.add(key);
            }
            for (int j = 0; j < keyList.size(); j++) {
                String value = tableObject.getString(keyList.get(j));
                parsePairList.add(new BasicNameValuePair(keyList.get(j), value));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsePairList;
    }

    // JSonObject 안에 원하는 값이 있을 경우 키값을 이용해 원하는 값만 꺼내올 수 있다.
    public String parseJsonObject(String response, String Key) throws JSONException {
        String result = null;
        try {
            Log.d("Response", response);
            JSONObject json = new JSONObject(response);
            result = json.getString(Key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    // JsonArray 형식으로 반환
    public JSONArray parseJsonArray(String response) throws JSONException {

        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(response);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArr;
    }

    //JsonObject 안에 JsonArray가 있는경우
    /*
    table : "ChatRoom"
    {
            "chatroom"
            {
                    PKey : PKey
                    Name : Name
                    Longitude : Longitude

            }

            "chatroom"
            {
                    PKey : PKey
                    Name : Name
                    Longitude : Longitude

            }
    }

    예 / jsonArrayInObject(data, "ChatRoom");
        과 같이 사용하면 JsonArray를 얻을 수 있다.

    활용 / doubleJsonObject(jsonArrayInObject(data, "ChatRoom").get(i).toString(), "chatroom").getString("PKey");
        i번째 chatroom의 PKey를 알 수 있다.

     */
    public JSONArray jsonArrayInObject(String data, String Key) throws JSONException {

        JSONArray result = null;
        try {
            JSONObject json = new JSONObject(data);
            result = json.getJSONArray(Key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }

    /*
    JSonObject 안에 JSonObject가 있는 경우 오브젝트 안에 있는 오브젝트를 반환한다.

    Key : data {
        {Key2 : data2, Key3 : data3, Key4 : data4...}
    } 이런경우 doubleJsonObject(data, key1).getString(key2);와 같이 사용하면 data2을 얻을 수 있다.

     */
    public JSONObject doubleJsonObject(String data, String Key) throws JSONException {

        JSONObject json = null;
        JSONObject result = null;
        try {
            json = new JSONObject(data);
            result = new JSONObject(json.getString(Key));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }

}
