package com.example.realreal.PlantList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.realreal.R;

public class Eat_plantActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_plant);

        findViewById(R.id.button_back_eat).setOnClickListener(onClickListener);
        findViewById(R.id.button_eat_leaf).setOnClickListener(onClickListener);
        findViewById(R.id.button_bean).setOnClickListener(onClickListener);
        findViewById(R.id.button_eat_rosemary).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_back_eat:
                    myStartActivity(Plant_list_Activity.class);
                    break;
                case R.id.button_eat_leaf:
                    myStartActivity(eat_leaf_Info.class);
                    break;
                case R.id.button_bean:
                    myStartActivity(eat_bean_Info.class);
                    break;
                case R.id.button_eat_rosemary:
                    myStartActivity(eat_rosemary_Info.class);
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
