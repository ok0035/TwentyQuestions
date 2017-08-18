package graduateproject.com.twentyquestions.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by mapl0 on 2017-08-18.
 */

public class MainView extends BaseActivity {

    //View
    private RelativeLayout parentLayout;
    private LinearLayout tabRowLayout1, tabRowLayout2, tabLayout;
    private TextView btnStartGame, btnFriendList, btnLetterList, btnChatList, btnCreateGame, btnFastGame, btnOnlyEmptyRoom, btnLatestOrder;
    private ViewPager pager;
    private View.OnClickListener movePageListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setValues();
        setUpEvents();
        setView();
        setContentView(parentLayout);

    }

    @Override
    public void setUpEvents() {

        movePageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                pager.setCurrentItem(tag);
            }
        };
    }

    @Override
    public void setView() {

        btnStartGame = new TextView(this);
        btnStartGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnStartGame.setText("게임하기");
        btnStartGame.setOnClickListener(movePageListener);
        btnStartGame.setTag(0);

        btnFriendList = new TextView(this);
        btnFriendList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnFriendList.setText("친구");
        btnFriendList.setOnClickListener(movePageListener);
        btnFriendList.setTag(1);

        btnChatList = new TextView(this);
        btnChatList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnChatList.setText("대화");
        btnChatList.setOnClickListener(movePageListener);
        btnChatList.setTag(2);

        btnLetterList = new TextView(this);
        btnLetterList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnLetterList.setText("쪽지함");
        btnLetterList.setOnClickListener(movePageListener);
        btnLetterList.setTag(3);


        tabRowLayout1 = new LinearLayout(this);
        tabRowLayout1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabRowLayout1.setOrientation(LinearLayout.HORIZONTAL);

        tabRowLayout1.addView(btnStartGame);
        tabRowLayout1.addView(btnFriendList);
        tabRowLayout1.addView(btnChatList);
        tabRowLayout1.addView(btnLetterList);


        btnCreateGame = new TextView(this);
        btnCreateGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnCreateGame.setText("게임 만들기");

        btnFastGame = new TextView(this);
        btnFastGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnFastGame.setText("빠른시작");

        btnLatestOrder = new TextView(this);
        btnLatestOrder.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnLatestOrder.setText("최근순");

        btnOnlyEmptyRoom = new TextView(this);
        btnOnlyEmptyRoom.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnOnlyEmptyRoom.setText("빈방만");


        tabRowLayout2 = new LinearLayout(this);
        tabRowLayout2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabRowLayout2.setOrientation(LinearLayout.HORIZONTAL);

        tabRowLayout2.addView(btnCreateGame);
        tabRowLayout2.addView(btnFastGame);
        tabRowLayout2.addView(btnLatestOrder);
        tabRowLayout2.addView(btnOnlyEmptyRoom);

        tabLayout = new LinearLayout(this);
        tabLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabLayout.setOrientation(LinearLayout.VERTICAL);

        tabLayout.addView(tabRowLayout1);
        tabLayout.addView(tabRowLayout2);


        pager = new ViewPager(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            pager.setId(pager.hashCode());
        } else {
            pager.setId(View.generateViewId());
        }

        pager.setAdapter(new MainViewAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        parentLayout = new RelativeLayout(this);
        parentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        parentLayout.addView(tabLayout);
        parentLayout.addView(pager);

    }

    @Override
    public void setValues() {
        super.setValues();
    }

    private class MainViewAdapter extends FragmentStatePagerAdapter {

        public MainViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return new GameListView();
                case 1:
                    return new FriendListView();
                case 2:
                    return new ChatListView();
                case 3:
                    return new LetterListView();
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
