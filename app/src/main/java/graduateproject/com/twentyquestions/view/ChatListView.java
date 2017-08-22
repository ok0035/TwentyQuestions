package graduateproject.com.twentyquestions.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class ChatListView extends Fragment {

    private LinearLayout parentLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setView();

        return parentLayout;
    }

    public void setView() {

        parentLayout = new LinearLayout(MainView.mContext);
        parentLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parentLayout.setBackgroundColor(Color.GRAY);

    }

}
