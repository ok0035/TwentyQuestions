package graduateproject.com.twentyquestions.network;

import graduateproject.com.twentyquestions.util.ParseData;

/**
 * Created by mapl0 on 2017-08-03.
 */

public class DataSyncController {

    private int localChatPkey = 0;
    private int localChatRoomPkey = 0;
    private int localChatMemberPkey = 0;

    private int serverChatPkey = 0;
    private int serverChatRoomPkey = 0;
    private int serverChatMemberPkey = 0;

    public void updateData(String response) {

        ParseData parse = new ParseData();
        DBSI db = new DBSI();

        if(serverChatPkey <= localChatPkey) {
            //update
//            db.query("update Chat set ");

        } else {
            //insert
//            db.query("insert into Chat values ()");

        }

        if(serverChatRoomPkey <= localChatRoomPkey) {
            //update


        } else {
            //insert

        }

        if(serverChatMemberPkey <= localChatMemberPkey) {
            //update


        } else {
            //insert
//            db.query("insert into ");
        }



    }

}
