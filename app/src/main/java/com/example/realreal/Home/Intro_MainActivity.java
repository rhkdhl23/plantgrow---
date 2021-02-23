package com.example.realreal.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.realreal.adapter.Intro_VPAdapter;
import com.example.realreal.R;

import me.relex.circleindicator.CircleIndicator;


public class Intro_MainActivity extends AppCompatActivity {

  public String fname = null;
  public String str = null;

  Button intro_start;
  TextView intro_start_login;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro_main);

    ViewPager intro_viewpager = findViewById(R.id.intro_viewpager);
    Intro_VPAdapter adapter = new Intro_VPAdapter(getSupportFragmentManager());
    intro_viewpager.setAdapter(adapter);

    CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
    indicator.setViewPager(intro_viewpager);


    intro_start = findViewById(R.id.intro_start);
    intro_start.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(Intro_MainActivity.this, SignUpActivity.class);
        startActivity(intent);
      }
    });

    intro_start_login = findViewById(R.id.intro_start_login);
    intro_start_login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(Intro_MainActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });
  }
}
