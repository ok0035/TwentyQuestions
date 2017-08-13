package graduateproject.com.twentyquestions.network;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import graduateproject.com.twentyquestions.activity.BaseActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class DBSI extends SQLiteOpenHelper{

//    private static DBSI db;

    SQLiteDatabase db;
    private String[][] result;

    public DBSI(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        result = null;

    }

    public DBSI() {
        super(BaseActivity.mContext, "twentyQuestions.db" , null, 1);
        result = null;
    }

//    public static DBSI getInstance() {
//
//        if(db == null) {
//            db = new DBSI(BaseActivity.mContext, "TwentyQuestions.db", null, 1);
//        }
//
//        return db;
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */

        setDB(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void query(String query) {

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(query);

        db.close();

    }

    public String[][] selectQuery(String query) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor;

        try{
            cursor = db.rawQuery(query, null);

            if(cursor.getCount() != 0) {

                this.result = new String[cursor.getCount()][cursor.getColumnCount()];

//            Log.d("getCount", cursor.getCount() + "");
//            Log.d("getColumnCount", cursor.getColumnCount() + "");

                cursor.moveToFirst();

                int i = 0;

                do {

                    for(int j = 0; j< cursor.getColumnCount(); j++) {

                        this.result[i][j] = cursor.getString(j);
                        System.out.println("Select Result........" + cursor.getColumnName(j) + "... : " + cursor.getString(j));

                    }

                    System.out.println("========================================================================================================");

                    i++;
//                System.out.println("moveToNext...." + cursor.moveToNext());
                } while (cursor.moveToNext());

            } else {
                Log.d("SelectNULL", "NULL");
                result = null;
            }

        } catch(SQLiteException e) {
            this.result = null;
        }



        db.close();
        return result;

    }

    public void update(String item, String price) {
        SQLiteDatabase db = getWritableDatabase();
    }

    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
    }

    public String getUserInfo() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT PKey, ID, Password FROM User", null);
        while (cursor.moveToNext()) {

            result += cursor.getString(0)
                    + "/"
                    + cursor.getString(1)
                    + "/"
                    + cursor.getString(2);
        }

//        cursor.moveToFirst();
//        this.result = new String[cursor.getCount()][cursor.getColumnCount()];
//        for(int i = 0; i<cursor.getCount(); i++) {
//            for(int j =0; j<cursor.getColumnCount(); j++) {
//                this.result[i][j] = cursor.getString(j);
//            }
//        }

        db.close();

//        result = cursor.getString(0);

            Log.d("UserInfo", result);

        return result;
    }

    private void setDB(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS User (PKey integer(8) NOT NULL, ID varchar(50), SNSAccessToken varchar(1500), Password varchar(255), LoginType integer(1) DEFAULT 0 NOT NULL, NickName varchar(20) NOT NULL, Phone varchar(15), Gender integer(1) NOT NULL, Birthday date NOT NULL, Longitude double(10), Latitude double(10), ConditionMessage varchar(200), Introduction varchar(200), IsVerification boolean DEFAULT '0', Status tinyint(3) DEFAULT 1, LatestLogin timestamp, UDID varchar(50), DeviceType integer(1), DeviceName varchar(100), OS varchar(200), CreatedDate timestamp NOT NULL, UpdatedDate timestamp NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Letter (PKey integer(8) NOT NULL, Sender integer(8) NOT NULL, Receiver integer(8) NOT NULL, TableName varchar(50), TablePKey integer(8), IsLock boolean DEFAULT '0' NOT NULL, IsRead boolean DEFAULT '0', Type integer(1) DEFAULT 0 NOT NULL, Title varchar(100) NOT NULL, Content text, CreatedDate timestamp NOT NULL, UpdatedDate timestamp NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS AskAnswerList (PKey integer(8) NOT NULL, TwentyQuestionsPKey integer(8) NOT NULL, Guesser integer(8), Answerer integer(8), Guess varchar(255) NOT NULL, Answer varchar(255) NOT NULL, Status integer(1) DEFAULT 0 \t\t\t, CreatedDate timestamp, UpdatedDate timestamp);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Chat (PKey integer(8) NOT NULL, ChatRoomPKey integer(8) NOT NULL, UserPKey integer(8) NOT NULL, ChatText text NOT NULL, Count integer(1), Type integer(1), CreatedDate timestamp NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS ChatMember (PKey integer(8) NOT NULL, UserPKey integer(8) NOT NULL, RoomPKey integer(8) NOT NULL, Status integer(1) DEFAULT 0 NOT NULL, Notify boolean DEFAULT '1' NOT NULL, CreatedDate timestamp, UpdatedDate timestamp);");
        db.execSQL("CREATE TABLE IF NOT EXISTS ChatRoom (PKey integer(8) NOT NULL, Name varchar(50) NOT NULL, Longitude double(10), Latitude double(10), CreatedDate timestamp NOT NULL, UpdatedDate timestamp NOT NULL, Description varchar(200));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Friend (PKey integer(8) NOT NULL, UserPKey integer(8) NOT NULL, FriendPKey integer(8) NOT NULL, Status integer(1) DEFAULT 0 NOT NULL, CreatedDate timestamp NOT NULL, UpdatedDate timestamp NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS GameList (PKey integer(8) NOT NULL, ChatRoomPKey integer(8), GameTypePKey integer(4) NOT NULL, Name varchar(50), Password varchar(20), Description varchar(255), Status boolean DEFAULT '0' NOT NULL, GameStatus integer(1), MinMember integer(4) DEFAULT 2 NOT NULL, MaxMember integer(4) DEFAULT 2 NOT NULL, Longitude double(10), Latitude double(10), CreatedDate timestamp, UpdatedDate timestamp);");
        db.execSQL("CREATE TABLE IF NOT EXISTS GameMember (PKey integer(8) NOT NULL, GameListPKey integer(8) NOT NULL, UserPKey integer(8) NOT NULL, MemberPriority integer(1) DEFAULT 0, Status integer(1) DEFAULT 0, IsWinner boolean DEFAULT '0', CreatedDate timestamp, UpdatedDate timestamp);");
        db.execSQL("CREATE TABLE IF NOT EXISTS GameType (PKey integer(4) NOT NULL, Type integer(1), TableName varchar(50) NOT NULL, GameName varchar(50) NOT NULL, Description varchar(255));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Hint (PKey integer(8) NOT NULL, GameListPKey integer(8) NOT NULL, UserPKey integer(8) NOT NULL, HintType integer(1), CreatedDate timestamp NOT NULL, UpdatedDate timestamp NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Notice (PKey integer(4) NOT NULL, Title varchar(100), Content text, NewDisplayDay integer(1), IsRead boolean DEFAULT '0', CreatedDate timestamp, UpdatedDate timestamp);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Picture (PKey integer(8) NOT NULL, UserPKey integer(8) NOT NULL, Priority integer(1) DEFAULT 0 NOT NULL, PicturePath varchar(255) NOT NULL, IsDeleted boolean DEFAULT '0' NOT NULL, CreatedDate timestamp NOT NULL, UpdatedDate timestamp NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS RandomName (PKey integer(4) NOT NULL, Type integer(1), Name varchar(50) NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS RightAnswerList (PKey integer(8) NOT NULL, TwentyQuestionPKey integer(8) NOT NULL, Guesser integer(8), Answerer integer(8), Guess varchar(255) NOT NULL, Answer text NOT NULL, IsRight integer(1) DEFAULT 0 NOT NULL, Status integer(1) DEFAULT 0 NOT NULL, CreatdDate timestamp NOT NULL, UpdatedDate timestamp NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS TwentyQuestions (PKey integer(8) NOT NULL, GameListPKey integer(8) NOT NULL, Object varchar(50) NOT NULL, MaxAskable integer(1) DEFAULT 20, MaxGuessable integer(1) DEFAULT 3, CreatedDate timestamp, UpdatedDate timestamp);");

        db.execSQL("" +
                "CREATE UNIQUE INDEX AskAnswerList_PKey ON AskAnswerList (PKey);\n" +
                "CREATE UNIQUE INDEX Chat_PKey ON Chat (PKey);\n" +
                "CREATE UNIQUE INDEX ChatMember_PKey ON ChatMember (PKey);\n" +
                "CREATE UNIQUE INDEX ChatRoom_PKey ON ChatRoom (PKey);\n" +
                "CREATE UNIQUE INDEX Friend_PKey ON Friend (PKey);\n" +
                "CREATE UNIQUE INDEX GameList_PKey ON GameList (PKey);\n" +
                "CREATE UNIQUE INDEX GameMember_PKey ON GameMember (PKey);\n" +
                "CREATE UNIQUE INDEX GameType_PKey ON GameType (PKey);\n" +
                "CREATE UNIQUE INDEX Hint_PKey ON Hint (PKey);\n" +
                "CREATE UNIQUE INDEX Letter_PKey ON Letter (PKey);\n" +
                "CREATE INDEX Notice_PKey ON Notice (PKey);\n" +
                "CREATE UNIQUE INDEX Picture_PKey ON Picture (PKey);\n" +
                "CREATE UNIQUE INDEX RandomName_PKey ON RandomName (PKey);\n" +
                "CREATE UNIQUE INDEX RightAnswerList_PKey ON RightAnswerList (PKey);\n" +
                "CREATE UNIQUE INDEX TwentyQuestions_PKey ON TwentyQuestions (PKey);\n" +
                "CREATE UNIQUE INDEX User_PKey ON User (PKey);\n" +
                "CREATE UNIQUE INDEX User_ID ON User (ID);\n");

    }

    public void dropTable() {

        query("DROP TABLE IF EXISTS AskAnswerList");
        query("DROP TABLE IF EXISTS Chat");
        query("DROP TABLE IF EXISTS Chat");
        query("DROP TABLE IF EXISTS ChatMember");
        query("DROP TABLE IF EXISTS ChatRoom");
        query("DROP TABLE IF EXISTS Friend");
        query("DROP TABLE IF EXISTS GameList");
        query("DROP TABLE IF EXISTS GameMember");
        query("DROP TABLE IF EXISTS GameType");
        query("DROP TABLE IF EXISTS Hint");
        query("DROP TABLE IF EXISTS Letter");
        query("DROP TABLE IF EXISTS Notice");
        query("DROP TABLE IF EXISTS Picture");
        query("DROP TABLE IF EXISTS RandomName");
        query("DROP TABLE IF EXISTS RightAnswerList");
        query("DROP TABLE IF EXISTS TwentyQuestions");
        query("DROP TABLE IF EXISTS User");

    }

    public void checkTable() {

        SQLiteDatabase db = getWritableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if(c.moveToFirst()) {

                for(;;) {

                    Log.e(TAG, "table name : " + c.getString(0));

                    if(!c.moveToNext())

                        break;

                }

            }
        } catch (SQLiteException e) {

            Log.d("SearchTable", "table is nothing");
        }




    }
}
