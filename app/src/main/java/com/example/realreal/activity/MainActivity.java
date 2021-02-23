package com.example.realreal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realreal.Chatbot.Chatbot_01_Activity;
import com.example.realreal.Plant;
import com.example.realreal.PlantList.Plant_list_Activity;
import com.example.realreal.R;
import com.example.realreal.Alarm.activities.AlarmMainActivity;
import com.example.realreal.adapter.MyRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private FirebaseUser firebaseUser;
  private List<Plant> plantList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.action_alarm).setOnClickListener(onClickListener);
    findViewById(R.id.action_catbot).setOnClickListener(onClickListener);
    findViewById(R.id.action_user).setOnClickListener(onClickListener);
    findViewById(R.id.action_info_plant).setOnClickListener(onClickListener);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowCustomEnabled(true);    // 커스터마이징 하기 위해 필요
      actionBar.setDisplayShowTitleEnabled(false);
      actionBar.setDisplayHomeAsUpEnabled(false);      // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }

    final RecyclerView recyclerView = findViewById(R.id.main_recyclerView);

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    firebaseDatabase.getReference().child("plant").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        plantList = new ArrayList<>();
        String fUid = firebaseUser.getUid();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          Plant value = snapshot.getValue(Plant.class);

          if (value != null) {
            String dUid = value.getUid();
            if (fUid.equals(dUid)) {
              value.setKey(snapshot.getKey());
              plantList.add(value);
            }
          }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(plantList, MainActivity.this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerViewAdapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        // Failed to read value
        Log.w("Hello", "Failed to read value.", databaseError.toException());
      }
    });
  }

  View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.action_alarm:
          myStartActivity(AlarmMainActivity.class);
          break;
        case R.id.action_catbot:
          myStartActivity(Chatbot_01_Activity.class);
          break;
        case R.id.action_user:
          myStartActivity(UserActivity.class);
          break;
        case R.id.action_info_plant:
          myStartActivity(Plant_list_Activity.class);
          break;
      }
    }
  };

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_list, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home: {
        Intent intent = new Intent(MainActivity.this, Plant_Add_Activity.class);
        startActivity(intent);
        break;
      }
      case R.id.action_insert: {
        if (chkPlantCnt()) {
          Intent intent = new Intent(MainActivity.this, AddActivity.class);
          intent.putExtra("FLAG", "I");
          startActivity(intent);
        } else {
          Toast.makeText(getApplicationContext(), "식물은 10개까지 등록 가능합니다.", Toast.LENGTH_SHORT).show();
        }

        break;
      }
    }

    return super.onOptionsItemSelected(item);
  }

  private boolean chkPlantCnt() {
    boolean flag = false;
    if (plantList.size() <= 10)
      flag = true;

    return flag;
  }

  private void myStartActivity(Class c) {
    Intent intent = new Intent(this, c);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }
}
