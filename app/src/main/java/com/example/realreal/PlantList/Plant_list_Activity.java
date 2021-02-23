package com.example.realreal.PlantList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realreal.PlantList.Eat_plantActivity;
import com.example.realreal.PlantList.Inside_plantActivity;
import com.example.realreal.PlantList.Pet_plantActivity;
import com.example.realreal.PlantList.Sunnot_plantActivity;
import com.example.realreal.R;
import com.example.realreal.activity.MainActivity;

public class Plant_list_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        findViewById(R.id.button_list_back).setOnClickListener(onClickListener);
        findViewById(R.id.button_eat).setOnClickListener(onClickListener);
        findViewById(R.id.button_inside).setOnClickListener(onClickListener);
        findViewById(R.id.button_sunnot).setOnClickListener(onClickListener);
        findViewById(R.id.button_pet).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_list_back:
                    myStartActivity(MainActivity.class);
                    break;
                case R.id.button_eat:
                    myStartActivity(Eat_plantActivity.class);
                    break;
                case R.id.button_inside:
                    myStartActivity(Inside_plantActivity.class);
                    break;
                case R.id.button_sunnot:
                    myStartActivity(Sunnot_plantActivity.class);
                    break;
                case R.id.button_pet:
                    myStartActivity(Pet_plantActivity.class);
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
