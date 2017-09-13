package graduateproject.com.twentyquestions.item;

/**
 * Created by yrs00 on 2017-08-29.
 */

public class ChatDataItem {

    private String userName;
    private String chattingText;
    private String userPKey;
    private String userMySelf;
    private String thumbnailPath;
    private String chatPKey;

    public String getChatPKey() {
        return chatPKey;
    }

    public void setChatPKey(String chatPKey) {
        this.chatPKey = chatPKey;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChattingText() {
        return chattingText;
    }

    public void setChattingText(String chattingText) {
        this.chattingText = chattingText;
    }

    public String getUserPKey() {
        return userPKey;
    }

    public void setUserPKey(String userPKey) {
        this.userPKey = userPKey;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getUserMySelf() {
        return userMySelf;
    }

    public void setUserMySelf(String userMySelf) {
        this.userMySelf = userMySelf;
    }


}
