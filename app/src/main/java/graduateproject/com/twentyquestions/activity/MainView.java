package graduateproject.com.twentyquestions.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
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
    private LinearLayout pagerLayout, divisionLine;
    private TextView btnStartGame, btnFriendList, btnLetterList, btnChatList, btnCreateGame, btnFastGame, btnOnlyEmptyRoom, btnLatestOrder;
    private ViewPager pager;
    private View.OnClickListener movePageListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCustomActionBar();

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
                Log.d("Click", tag + "");
                System.out.println("Click ............ " + tag);
            }
        };
    }

    @Override
    public void setView() {

        btnStartGame = new TextView(this);
        btnStartGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnStartGame.setText("게임하기");
        btnStartGame.setGravity(Gravity.CENTER);
        btnStartGame.setTag(0);
        btnStartGame.setOnClickListener(movePageListener);

        btnFriendList = new TextView(this);
        btnFriendList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnFriendList.setText("친구");
        btnFriendList.setGravity(Gravity.CENTER);
        btnFriendList.setTag(1);
        btnFriendList.setOnClickListener(movePageListener);

        btnChatList = new TextView(this);
        btnChatList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnChatList.setText("대화");
        btnChatList.setGravity(Gravity.CENTER);
        btnChatList.setTag(2);
        btnChatList.setOnClickListener(movePageListener);

        btnLetterList = new TextView(this);
        btnLetterList.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnLetterList.setText("쪽지함");
        btnLetterList.setGravity(Gravity.CENTER);
        btnLetterList.setTag(3);
        btnLetterList.setOnClickListener(movePageListener);


        tabRowLayout1 = new LinearLayout(this);
        tabRowLayout1.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabRowLayout1.setOrientation(LinearLayout.HORIZONTAL);

        tabRowLayout1.addView(btnStartGame);
        tabRowLayout1.addView(btnFriendList);
        tabRowLayout1.addView(btnChatList);
        tabRowLayout1.addView(btnLetterList);


        btnCreateGame = new TextView(this);
        btnCreateGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnCreateGame.setGravity(Gravity.CENTER);
        btnCreateGame.setText("게임 만들기");

        btnFastGame = new TextView(this);
        btnFastGame.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnFastGame.setGravity(Gravity.CENTER);
        btnFastGame.setText("빠른시작");

        btnLatestOrder = new TextView(this);
        btnLatestOrder.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnLatestOrder.setGravity(Gravity.CENTER);
        btnLatestOrder.setText("최근순");

        btnOnlyEmptyRoom = new TextView(this);
        btnOnlyEmptyRoom.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, 1f));
        btnOnlyEmptyRoom.setGravity(Gravity.CENTER);
        btnOnlyEmptyRoom.setText("빈방만");


        tabRowLayout2 = new LinearLayout(this);
        tabRowLayout2.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tabRowLayout2.setOrientation(LinearLayout.HORIZONTAL);

        tabRowLayout2.addView(btnCreateGame);
        tabRowLayout2.addView(btnFastGame);
        tabRowLayout2.addView(btnLatestOrder);
        tabRowLayout2.addView(btnOnlyEmptyRoom);

        tabLayout = new LinearLayout(this);
        LinearLayout.LayoutParams tabParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabParams.setMargins(0,100,0,0);
        tabLayout.setLayoutParams(tabParams);
        tabLayout.setOrientation(LinearLayout.VERTICAL);

        tabLayout.addView(tabRowLayout1);
        tabLayout.addView(tabRowLayout2);

        divisionLine = new LinearLayout(MainView.mContext);
        divisionLine.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        layoutParams.setMargins(0, 100, 0, 0);
        divisionLine.setLayoutParams(layoutParams);

        tabLayout.addView(divisionLine);


        pager = new ViewPager(this);
        pager.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            pager.setId(pager.hashCode());
        } else {
            pager.setId(View.generateViewId());
        }

        pager.setAdapter(new MainViewAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        pagerLayout = new LinearLayout(this);
        pagerLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        pagerLayout.addView(pager);

        tabLayout.addView(pagerLayout);

        parentLayout = new RelativeLayout(this);
        parentLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        parentLayout.addView(tabLayout);


    }

    @Override
    public void setValues() {



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
