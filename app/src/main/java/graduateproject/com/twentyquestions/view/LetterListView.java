package graduateproject.com.twentyquestions.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.adapter.LetterAdapter;
import graduateproject.com.twentyquestions.item.LetterDataItem;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class LetterListView extends Fragment {

    LinearLayout layout;
    ListView listview;
    LetterDataItem letterDataItem;
    ArrayList<LetterDataItem > letterDataItemList;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.letter_litst_view, container, false);
        listview = (ListView) layout.findViewById(R.id.letterListView);


        letterDataItemList = new ArrayList<>();
        for(int i = 0 ; i < 100 ; i++){
            letterDataItem = new LetterDataItem();
            letterDataItem.setLetterCreatedDate("2003-12-19");
            letterDataItem.setLetterPKey(i+"");
            letterDataItem.setLetterTitle("title"+i);
            letterDataItem.setLetterType("test");
            letterDataItemList.add(letterDataItem);
        }

        listview.setAdapter(new LetterAdapter(getContext(), letterDataItemList));

        return layout;
    }
}
