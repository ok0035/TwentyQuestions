package graduateproject.com.twentyquestions.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.item.LetterData;
import graduateproject.com.twentyquestions.view.LetterDialog;

/**
 * Created by hero on 2017-10-23.
 */

public class LetterAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<LetterData> mList;
    private LayoutInflater inf;

    public LetterAdapter(Context context, ArrayList<LetterData> list) {
        super(context, R.layout.letter_list_item, list);
        mContext = context;
        mList = list;
        inf = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            row = inf.inflate(R.layout.letter_list_item, null);
        }

        ImageView profilImage = (ImageView) row.findViewById(R.id.ivLetterProfil);
        TextView tvLetterItemType = (TextView) row.findViewById(R.id.tvLetterItemType);
        TextView tvLetterTitle = (TextView) row.findViewById(R.id.tvLetterTitle);

        final LetterData letterDataItem = mList.get(position);

        switch (letterDataItem.getLetterType()) {
            case "Friend": {
                tvLetterItemType.setText("친구신청" + " / " + letterDataItem.getLetterCreatedDate());
                tvLetterTitle.setText("친구신청을 받아주세요");
            }
            break;
            default: {
                tvLetterItemType.setText(mList.get(position).getLetterType() + " / " + mList.get(position).getLetterCreatedDate());
                tvLetterTitle.setText(mList.get(position).getLetterTitle());
            }
        }


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext,mList.get(position).getLetterPKey(),Toast.LENGTH_SHORT ).show();

                Intent intent = new Intent(mContext, LetterDialog.class);
                ArrayList<LetterData> list = new ArrayList<LetterData>();
                list.add(letterDataItem);
                intent.putExtra("LetterData", list);
                getContext().startActivity(intent);

            }
        });

        return row;
    }
}
