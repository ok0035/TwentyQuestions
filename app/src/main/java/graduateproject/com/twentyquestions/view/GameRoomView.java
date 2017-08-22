package graduateproject.com.twentyquestions.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

/**
 * Created by mapl0 on 2017-08-22.
 */

public class GameRoomView extends BaseActivity {
    LinearLayout parentView;

    public GameRoomView() {
        super();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setValues();
        setUpEvents();
        setView();
        setCustomActionBar();
        setContentView(parentView);

    }

    @Override
    public void setUpEvents() {
        super.setUpEvents();
    }

    @Override
    public void setView() {
        super.setView();
    }

    @Override
    public void setValues() {
        parentView = new LinearLayout(MainView.mContext);
    }

}
