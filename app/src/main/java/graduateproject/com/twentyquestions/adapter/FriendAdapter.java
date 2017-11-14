package graduateproject.com.twentyquestions.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.item.FriendData;

/**
 * Created by mapl0 on 2017-11-13.
 */

public class FriendAdapter extends ArrayAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<FriendData> friendList;

    private TextView tvFriendName;

    public FriendAdapter(@NonNull Context context, ArrayList list) {
        super(context, R.layout.friend_list, list);

        friendList = list;
        mContext = context;
        inflater = LayoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View convert = convertView;

        if(convert == null) {
                convert = inflater.inflate(R.layout.friend_item, parent, false);
        }

        String name = friendList.get(position).getFriendName();
        Log.d("friendName", name);

        tvFriendName = convert.findViewById(R.id.tvFriendName);
        tvFriendName.setText(name);

        return convert;

    }

    public ArrayAdapter getAdapter() {
        return this;
    }
}
