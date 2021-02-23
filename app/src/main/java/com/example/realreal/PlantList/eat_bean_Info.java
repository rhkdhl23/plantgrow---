package com.example.realreal.PlantList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.realreal.R;

public class eat_bean_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_bean__info);

        findViewById(R.id.button_back_bean).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_back_bean:
                    myStartActivity(Eat_plantActivity.class);
                    break;
            }
        }
    };
            private void myStartActivity ( Class c){
                Intent intent = new Intent(this, c);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
