package graduateproject.com.twentyquestions.item;

import java.io.Serializable;

/**
 * Created by hero on 2017-10-23.
 */

public class LetterData implements Serializable {


    private String letterPKey;
    private String letterType;
    private String letterCreatedDate;
    private String profilImagePath;
    private String letterTitle;
    private String letterTablePKey;
    private String letterContent;

    public String getLetterContent() {
        return letterContent;
    }

    public void setLetterContent(String letterContent) {
        this.letterContent = letterContent;
    }

    public String getLetterType() {
        return letterType;
    }

    public void setLetterType(String letterType) {
        this.letterType = letterType;
    }

    public String getLetterCreatedDate() {
        return letterCreatedDate;
    }

    public void setLetterCreatedDate(String letterCreatedDate) {
        this.letterCreatedDate = letterCreatedDate;
    }

    public String getProfilImagePath() {
        return profilImagePath;
    }

    public void setProfilImagePath(String profilImagePath) {
        this.profilImagePath = profilImagePath;
    }

    public String getLetterTitle() {
        return letterTitle;
    }

    public void setLetterTitle(String letterTitle) {
        this.letterTitle = letterTitle;
    }

    public String getLetterPKey() {
        return letterPKey;
    }

    public void setLetterPKey(String letterPKey) {
        this.letterPKey = letterPKey;
    }


    public String getLetterTablePKey() {
        return letterTablePKey;
    }

    public void setLetterTablePKey(String letterTablePKey) {
        this.letterTablePKey = letterTablePKey;
    }
}
