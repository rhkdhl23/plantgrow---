package com.example.realreal.PlantList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.realreal.R;

public class Pet_plantActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_plant);

        findViewById(R.id.button_back_pet).setOnClickListener(onClickListener);
        findViewById(R.id.button_pet_marimo).setOnClickListener(onClickListener);
        findViewById(R.id.button_pet_catleaf).setOnClickListener(onClickListener);
        findViewById(R.id.button_eat_Olivetree).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_back_pet:
                    myStartActivity(Plant_list_Activity.class);
                    break;
                case R.id.button_pet_marimo:
                    myStartActivity(pet_marimo_Info.class);
                    break;
                case R.id.button_pet_catleaf:
                    myStartActivity(pet_catleaf_Info.class);
                    break;
                case R.id.button_eat_Olivetree:
                    myStartActivity(pet_olivetree_Info.class);

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
