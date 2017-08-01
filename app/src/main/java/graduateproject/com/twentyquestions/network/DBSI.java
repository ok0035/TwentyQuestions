package graduateproject.com.twentyquestions.network;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class DBSI extends SQLiteOpenHelper{

    public DBSI(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */

//        db.execSQL("drop table User;");

        db.execSQL("CREATE TABLE IF NOT EXISTS User (" +
                        "PKey INTEGER NOT NULL PRIMARY KEY, " +
                        "ID varchar(50), " +
                        "SNSAccessToken varchar(1500), " +
                        "Password varchar(255), " +
                        "LoginType integer(1) DEFAULT 0 NOT NULL, " +
                        "NickName varchar(20) NOT NULL, " +
                        "Phone varchar(15), " +
                        "Gender integer(1) NOT NULL, " +
                        "Birthday date NOT NULL, " +
                        "Longitude double(10), " +
                        "Latitude double(10), " +
                        "ConditionMessage varchar(200), " +
                        "Introduction varchar(200), " +
                        "IsVerification boolean DEFAULT '0', " +
                        "Status tinyint(3) DEFAULT 1, " +
                        "LatestLogin timestamp, " +
                        "UDID varchar(50), " +
                        "DeviceType integer(1), " +
                        "DeviceName varchar(100), " +
                        "OS varchar(200), " +
                        "CreatedDate timestamp NOT NULL, " +
                        "UpdatedDate timestamp NOT NULL);");

//        db.execSQL("CREATE TABLE IF NOT EXISTS UserTest (ID TEXT PRIMARY KEY, Password TEXT);");

//        db.execSQL("CREATE TABLE IF NOT EXISTS MONEYBOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, RoomName TEXT, GameType TEXT, create_at TEXT);");
        Log.d("DBHelper", "자동으로 들어오나?");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert() {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO User(PKey, ID, Password, NickName, LoginType, Gender, Birthday, CreatedDate, UpdatedDate) " +
                        "VALUES('2', 'ok0035', '1111', 'asdf', '0', '0', '19930116', '10:10', '10:10');");
        db.close();
    }

    public void update(String item, String price) {
        SQLiteDatabase db = getWritableDatabase();
    }

    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT ID, Password FROM User", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + "\n"
                    + cursor.getString(1)
                    + "\n";
        }

//        result = cursor.getString(0);

            Log.d("asdf", result);


        return result;
    }
}
