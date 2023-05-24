package com.taewon.mygallag;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer; // 오디오 또는 비디오를 재생하기 위해 사용되는 클래스
    int characterId, effectId; // 선택한 비행기 담을 id와 soundPool 담을 id
    // ===== xml =====
    ImageButton startBtn; // 시작 버튼 가져오기
    TextView guideTv; // TextView 가져오기
    ImageView imgView[] = new ImageView[8]; // ImageView 배열 8사이즈
    // ==============
    Integer img_id[] = {R.id.ship_001, R.id.ship_002, R.id.ship_003, R.id.ship_004, R.id.ship_005, R.id.ship_006, R.id.ship_007, R.id.ship_008}; // 비행기 종류 8종의 ID 배열에 담음
    Integer img[] = {R.drawable.ship_0000, R.drawable.ship_0001, R.drawable.ship_0002, R.drawable.ship_0003, R.drawable.ship_0004, R.drawable.ship_0005, R.drawable.ship_0006, R.drawable.ship_0007}; // 비행기 종류 8종류 배열에 담음
    SoundPool soundPool; // 짧은 오디오 효과를 재생하기 위해 사용

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mediaPlayer = MediaPlayer.create(this, R.raw.robby_bgm); // 미디어 플레이어 민들기(robby_bgm 들어가 있음)
        mediaPlayer.setLooping(true); // 반복 재생
        mediaPlayer.start();// 미디어 플레이어 시작
        soundPool = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE, 0); // soundPool(동시에 재생할 수 있는 최대 스트림 수, 오디오 스트림 유형, 현재 매개 변수(사운드의 품질))
        effectId = soundPool.load(this, R.raw.reload_sound, 1); // effectId 에 soundPool(컨텍스트 객체, reload_sound, 우선순위)를 load() 메서드 호출 결과로 정수 값으로 식별
        startBtn = findViewById(R.id.startBtn); // 시작 버튼 아이디 값 받아오기
        guideTv = findViewById(R.id.guideTv); // guideTv 아이디 값 받아오기

        startBtn.setVisibility(View.INVISIBLE); // 시작 버튼 안보이게 처리 (INVISIBLE) : 처음에는 안보이게 처리

        for (int i = 0; i < imgView.length; i++) { 
            imgView[i] = findViewById(img_id[i]); // 반복문을 이용해서 img_id[]에 들어있는  아이디들 imgView[]에 담기
            int index = i; //선택한 이미지 번호 알기 
            // imgView(비행기) 선택 리스너
            imgView[i].setOnClickListener(new View.OnClickListener() { 
                @Override
                public void onClick(View v) {
                    characterId = img[index]; // characterId에 선택된 img(비행기 img)
                    startBtn.setVisibility(View.VISIBLE); // 시작 버튼 보이기 처리 (VISIBLE)
                    startBtn.setEnabled(true); // 시작 버튼 활성화
                    startBtn.setImageResource(characterId); //버튼에 선택한 이미지 넣기
                    guideTv.setVisibility(View.INVISIBLE); //마지막 TextView 숨기기
                    // soundPool 재생 (위에서 만든 effectId, 왼쪽 스피커 볼륨, 오른쪽 스피커 볼륨, 우선순위, 반복횟수, 재생속도 )
                    soundPool.play(effectId, 1, 1, 0, 0, 1.0f);
                }
            });
        }
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        init(); // 초기화
    }

    private void init() {
        findViewById(R.id.startBtn).setVisibility(View.GONE); //버튼 위치는 남겨두고 숨기기
        findViewById(R.id.startBtn).setEnabled(false); //선택 비활성화 하기
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() { //시작 버튼 리스너
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class); // intent 이용해서 StartActivity에서 MainActivity 액티비티로 이동
                intent.putExtra("character", characterId); //선택한 이미지 넘기기 
                startActivity(intent); // 실행
                finish(); //액티비티 종료
            }
        });
    }

    @Override
    protected void onDestroy() { //액티비티 소멸직전 호출 mediaPlayer가 살아있으면 리소스 소멸시킨다.
        super.onDestroy();
        if (mediaPlayer != null) { // 미디어 플레이어가 null이 아니면
            mediaPlayer.release(); // 미디어 플레이어 해체
            mediaPlayer = null; // 미디어 플레이어에 null
        }
    }
}
