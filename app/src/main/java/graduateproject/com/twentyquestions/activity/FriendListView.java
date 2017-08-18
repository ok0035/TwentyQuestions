package graduateproject.com.twentyquestions.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class FriendListView extends Fragment {

    private RelativeLayout parentLayout;

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

        parentLayout = new RelativeLayout(MainView.mContext);

    }

}
