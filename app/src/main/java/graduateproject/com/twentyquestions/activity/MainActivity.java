package graduateproject.com.twentyquestions.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import graduateproject.com.twentyquestions.R;
import graduateproject.com.twentyquestions.network.DBSI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DBSI db = new DBSI(getApplicationContext(), "TwentyQuestions.db", null, 1);
        db.getResult();
//        db.insert();

        Intent intent = new Intent(getApplicationContext(), SubActivity.class);
        startActivity(intent);

    }
}
