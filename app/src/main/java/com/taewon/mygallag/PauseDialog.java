package com.taewon.mygallag;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

public class PauseDialog extends Dialog {
    // 라디오 그룹 2개 정의
    RadioGroup bgMusicOnOff;
    RadioGroup effectSoundOnOff;

    public PauseDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.pause_dialog); //pause_dialog.xml 구현
        // 라디어 그룹 각각 id 받아옴
        bgMusicOnOff = findViewById(R.id.bgMusicOnOff);
        effectSoundOnOff = findViewById(R.id.effectSoundOnOff);
        init(); // init 메서드 실행
    }

    public void init() {
        // 버튼 라이도 그룹 중 bgMusicOnOff 중에 하나의 라디오 버튼을 누르면
        bgMusicOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bgMusicOn: // bgMusicOn일 경우
                        // MainActivity에 있는 bgMusic(배경음악)의 볼륨(왼쪽 볼륨 1, 오른쪽 볼륨 1) | setVolume() 메서드는 미디어 재생을 제어하는 클래스에서 재공하는 메서드 | 1.0은 최대 볼륨을 의미
                        MainActivity.bgMusic.setVolume(1, 1);
                        break; // 탈출
                    case R.id.bgMusicOff: // bgMusicOff일 경우
                        MainActivity.bgMusic.setVolume(0, 0); // MainActivity에 있는 bgMusic(배경음악)의 볼륨(왼쪽 볼륨 0, 오른쪽 볼륨 0)
                        break; // 탈출
                }
            }
        });
        // 버튼 라이도 그룹 중 effectSoundOnOff 중에 하나의 라디오 버튼을 누르면
        effectSoundOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.effectSoundOn: // effectSoundOn일 경우
                        MainActivity.effectVolumn = 1.0f; // MainActivity.effectvolumn을 1.0f으로 설정
                        break;
                    case R.id.effectSoundOff: // effectSoundOff일 경우
                        MainActivity.effectVolumn = 0; // MainActivity.effectvolumn을 0으로 설정
                        break;
                }
            }
        });
        findViewById(R.id.dialogCancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); //cancle보다 안전하게 화면 종료, cancle과의 차이 이해하기
            }
        });

        findViewById(R.id.dialogOkBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
