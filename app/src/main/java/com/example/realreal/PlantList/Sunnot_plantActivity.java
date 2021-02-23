package com.example.realreal.PlantList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.realreal.R;

public class Sunnot_plantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunnot_plant);

        findViewById(R.id.button_back_sunnot).setOnClickListener(onClickListener);
        findViewById(R.id.button_sunnot_Stuki).setOnClickListener(onClickListener);
        findViewById(R.id.button_sunnot_Peperomia).setOnClickListener(onClickListener);
        findViewById(R.id.button_sunnot_moonshine).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_back_sunnot:
                    myStartActivity(Plant_list_Activity.class);
                    break;
                case R.id.button_sunnot_Stuki:
                    myStartActivity(sunnot_stuki_Info.class);
                    break;
                case R.id.button_sunnot_Peperomia:
                    myStartActivity(sunnot_peperomia_Info.class);
                    break;
                case R.id.button_sunnot_moonshine:
                    myStartActivity(sunnot_moonshine_Info.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
