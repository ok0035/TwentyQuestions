package graduateproject.com.twentyquestions.util;

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
    public  BasicNameValuePair parseDataToPair(String response, String Key){
        try {
            this.KEY = Key;
            this.jsonResponse = new JSONObject(response);
            convertJSonObject = jsonResponse.getString(KEY);
            parseObjectPair= new BasicNameValuePair(KEY,convertJSonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parseObjectPair;

    }
    // table의 값들을 뽑아내서 List<BasicNameValue>형태로 저장하는 메소드
    public  ArrayList parseDataToList(JSONObject response, String Key){
        try {
            this.KEY = Key;
            parsePairList = new ArrayList<>();
            tableObject= response.getJSONObject(KEY);
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


}
