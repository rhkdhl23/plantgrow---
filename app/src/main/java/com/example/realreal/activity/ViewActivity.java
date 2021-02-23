package com.example.realreal.activity;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.realreal.Calender.CalendarActivity;
import com.example.realreal.R;

public class ViewActivity extends AppCompatActivity {

  Button btn_water;
  Button btn_calendar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    btn_water = findViewById(R.id.btn_water);
    btn_calendar = findViewById(R.id.btn_calendar);

    btn_water.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.falling_water);
        animationView.setAnimation("water.json");
        animationView.playAnimation();

        Toast.makeText(getApplicationContext(), "물을 주었습니다!", Toast.LENGTH_LONG).show();
      }

    });

    btn_calendar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        myStartActivity(CalendarActivity.class);
        finish();
      }
    });

    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowCustomEnabled(true);    // 커스터마이징 하기 위해 필요
      actionBar.setDisplayShowTitleEnabled(false);
      actionBar.setDisplayHomeAsUpEnabled(true);      // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
      actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_arrow_left_black_24);
    }

    Intent intent = getIntent();
    setValues(intent);
  }

  private void setValues(Intent intent) {
    TextView mName = findViewById(R.id.name);
    String name = intent.getStringExtra("name");
    mName.setText(name);

    TextView mKind = findViewById(R.id.kind);
    String kind = intent.getStringExtra("kind");
    mKind.setText(kind);

    TextView mIntro = findViewById(R.id.intro);
    String intro = intent.getStringExtra("intro");
    mIntro.setText(intro);

    ImageView imageView = findViewById(R.id.imageView);
    String imageUrl = intent.getStringExtra("imageUrl");
    Glide.with(imageView).load(imageUrl).into(imageView);

    imageView.setBackground(new ShapeDrawable(new OvalShape()));
    imageView.setClipToOutline(true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_view, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home: {
        Intent intent = new Intent(ViewActivity.this, MainActivity.class);
        startActivity(intent);
        return true;
      }
    }

    return super.onOptionsItemSelected(item);
  }

  private void myStartActivity(Class c) {
    Intent intent = new Intent(this, c);
    startActivityForResult(intent, 0);
  }
}
