package com.taewon.mygallag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_dialog); // game_over_dialog.xml
        init(); // init 메서드 호출
    }

    private void init() {
        // goMainBtn (처음으로) 버튼 리스너
        findViewById(R.id.goMainBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, StartActivity.class); // startActivity로 이동 설정
                startActivity(intent); // intent 사용해서 액티비티 시작
                finish(); // 스택 제거 (StartActivity가 시작된 후에는 ResultActivity가 종료
            }
        });
        ((TextView) findViewById(R.id.userFinalScoreText)).setText(getIntent().getIntExtra("score", 0) + ""); //spaceInvadersView에서 받아온 score점수를 userFinalScoreText에 넣어준다
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}
