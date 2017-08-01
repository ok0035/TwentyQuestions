package graduateproject.com.twentyquestions.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DataSync;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class SubActivity extends BaseActivity {

    private android.widget.Button test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        this.test = (Button) findViewById(R.id.test);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSync.getInstance().doSync();
            }
        });
    }
}
