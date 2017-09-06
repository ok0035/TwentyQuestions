package graduateproject.com.twentyquestions.item;

import android.widget.Button;

import graduateproject.com.twentyquestions.view.MainView;

/**
 * Created by mapl0 on 2017-08-19.
 */

public class GameListViewItem {

    private String roomNumber;
    private String roomName;
    private String description;
    private String chatRoomPKey;
    private Button btnGameState;

    public GameListViewItem() {
        btnGameState = new Button(MainView.mContext);
    }

    public void setRoomNumber(String num) {

        roomNumber = num;

    }

    public void setRoomName(String roomName) {

        this.roomName = roomName;
    }

    public void setDescription(String desc) {

        this.description = desc;

    }

    public String getRoomNumber() {

        return roomNumber;
    }

    public String getRoomName() {

        return roomName;

    }

    public String getDescription() {

        return description;
    }

    public String getChatRoomPKey() {
        return chatRoomPKey;
    }

    public void setChatRoomPKey(String chatRoomPKey) {
        this.chatRoomPKey = chatRoomPKey;
    }
}
