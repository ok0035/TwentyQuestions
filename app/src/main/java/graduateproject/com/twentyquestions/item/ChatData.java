package graduateproject.com.twentyquestions.item;

/**
 * Created by yrs00 on 2017-08-29.
 */

public class ChatData {

    private String userName;
    private String chattingText;
    private String userPKey;
    private String userMySelf;
    private String thumbnailPath;
    private String chatFlag;
    private String objPKey;

    public String getObjPKey() {
        return objPKey;
    }

    public void setObjPKey(String objPKey) {
        this.objPKey = objPKey;
    }


    public String getChatFlag() {
        return chatFlag;
    }

    public void setChatFlag(String chatFlag) {
        this.chatFlag = chatFlag;
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
