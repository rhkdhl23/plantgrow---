package com.example.realreal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realreal.IFirebaseLoadDone;
import com.example.realreal.R;
import com.example.realreal.Users;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class Plant_Add_Activity extends AppCompatActivity implements IFirebaseLoadDone {

  SearchableSpinner searchableSpinner;
  BottomSheetDialog bottomSheetDialog;
  IFirebaseLoadDone iFirebaseLoadDone;
  DatabaseReference userRef;
  private DatabaseReference mDatabase;
  TextView user_con;
  ImageView user_image;
  Button btn_fav;
  private int i;
  List<Users> users;
  boolean isFirstTimeClick = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_plant_add);

    View view = getLayoutInflater().inflate(R.layout.list_layout, null);
    user_con = (TextView) findViewById(R.id.usercon);
    user_image = (ImageView) findViewById(R.id.userimage);


    userRef = FirebaseDatabase.getInstance().getReference("Users");
    iFirebaseLoadDone = this;
    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        List<Users> users = new ArrayList<>();
        for (DataSnapshot userSnapShot : snapshot.getChildren()) {
          users.add(userSnapShot.getValue(Users.class));
        }
        iFirebaseLoadDone.onFirebaseLoadSuccess(users);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        iFirebaseLoadDone.onFirebaseLoadFailed(error.getMessage());
      }
    });

    FirebaseApp.initializeApp(this);

    mDatabase = FirebaseDatabase.getInstance().getReference();
    findViewById(R.id.btn_check).setOnClickListener(onClickListener);

    searchableSpinner = (SearchableSpinner) findViewById(R.id.searchable_spinner);
    searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!isFirstTimeClick) {
          Users user = users.get(i);
          user_con.setText(user.getStatus());
          Picasso.get().load(user.getImage()).into(user_image);

        } else isFirstTimeClick = false;
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

  }

  View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.btn_check:
          Toast.makeText(Plant_Add_Activity.this, "추가 완료", Toast.LENGTH_SHORT).show();
          finish();
      }
    }
  };

  @Override
  public void onFirebaseLoadSuccess(List<Users> usersList) {

    users = usersList;
    List<String> name_list = new ArrayList<>();
    for (Users users : usersList)
      name_list.add(users.getName());
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name_list);
    searchableSpinner.setAdapter(adapter);
  }


  @Override
  public void onFirebaseLoadFailed(String message) {

  }
}
