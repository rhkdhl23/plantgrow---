package com.example.realreal.PlantList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.realreal.R;

public class Inside_plantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_plant);

        findViewById(R.id.button_back_inside).setOnClickListener(onClickListener);
        findViewById(R.id.button_inside_tillandsia).setOnClickListener(onClickListener);
        findViewById(R.id.button_inside_dracenamaginata).setOnClickListener(onClickListener);
        findViewById(R.id.button_inside_sofora).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_back_inside:
                    myStartActivity(Plant_list_Activity.class);
                    break;
                case R.id.button_inside_tillandsia:
                    myStartActivity(inside_tillandsia_Info.class);
                    break;
               case R.id.button_inside_dracenamaginata:
                    myStartActivity(inside_dracenamaginata_Info.class);
                    break;
                case R.id.button_inside_sofora:
                   myStartActivity(inside_sofora_Info.class);
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
