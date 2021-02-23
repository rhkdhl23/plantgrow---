package com.example.realreal.Chatbot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realreal.R;
import com.example.realreal.activity.MainActivity;

public class Chatbot_01_Activity extends AppCompatActivity {

    private Button chatbot1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_01);

        chatbot1 = findViewById(R.id.chatbot1);
        chatbot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Chatbot_01_Activity.this);
                dlg.setTitle("궁금한 내용의 키워드를 입력해주세요!"); //제목
                dlg.setMessage(
                        "식물에 문제가 생기셨나요?\n제가 도와드릴게요!" +
                                "\n'분갈이,물주기,햇빛,꿀팁' 등에 대해 알려드릴게요!" +
                                "\n 저에게'안녕'이라고 말해주세요 :) "); // 메시지
                dlg.setIcon(R.drawable.chatbot); // 아이콘 설정
//                버튼 클릭시 동작
                dlg.setPositiveButton("환영합니다.",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Chatbot_01_Activity.this, Chatbot_02_Activity.class);
                        startActivity(intent);
                    }
                });
                dlg.show();

            }
        });

        Button button = (Button)findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chatbot_01_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
