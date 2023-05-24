package com.taewon.mygallag;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRouter;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    //  ===== xml =====
    public static ImageButton fireBtn, reloadBtn; // 발싸 버튼, 장전 버튼 
    ImageButton specialShotBtn; // 필사기 번튼
    ImageView pauseBtn; // 정지 버튼
    LinearLayout gameFrame; // 게임 화면을 가리키는 LinearLayout
    public static LinearLayout lifeFrame; // 생명(하트 3개)를 가리키는 LinearLayout
    public static TextView scoreTv; // 점수 텍스트
    public static TextView bulletCount; // 총알 텍스트
    JoystickView joyStick; // 조이스틱
    //  ===== xml =====

    private Intent userIntent; // intent 선언 ( 액티비티간 통신이나 액티비티의 동작을 저장하기 위해 사용 )
    SpaceInvadersView spaceInvadersView;
    public static SoundPool effectSound; // 여러 개의 오디오 스트림을 동시에 재생할 수 있는 기능
    public static float effectVolumn; // 효과음 정적 변수로 선언
    public static MediaPlayer bgMusic; // 배경음악을 위한 미디어플레이어
    int bgMusicIndex; // 배경음악을 선택할 인덱스
    ArrayList<Integer> bgMusicList; // 배경음악을 담을 리스트
    private static ArrayList<Integer> effectSoundList; // 효과음을 담을 리스트

    // 사운드 효과를 추가할 인덱스(위치)
    public static final int PLAYER_SHOT = 0;
    public static final int PLAYER_HURT = 1;
    public static final int PLAYER_RELOAD = 2;
    public static final int PLAYER_GET_ITEM = 3;

    // =====================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userIntent = getIntent(); // 현재 액티비티에 전달된 intent를 가져온다.
        bgMusicIndex = 0; // 배경음악을 틀기 위한 인덱스
        bgMusicList = new ArrayList<Integer>(); //음악넣을 arrylist 생성
        bgMusicList.add(R.raw.main_game_bgm1); // main_game_bgm1 추가
        bgMusicList.add(R.raw.main_game_bgm2); // main_game_bgm2 추가
        bgMusicList.add(R.raw.main_game_bgm3); // main_game_bgm3 추가

        effectSound = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE, 0); // soundPool(동시에 재생할 수 있는 최대 스트림 수, 오디오 스트림 유형, 현재 매개 변수(사운드의 품질))
        effectVolumn = 1; // 효과음 = 1

        // id 가져오기
        specialShotBtn = findViewById(R.id.specialShotBtn); // 필사기
        joyStick = findViewById(R.id.joyStick); // 조이스틱
        scoreTv = findViewById(R.id.score); // 스코어 TextView
        fireBtn = findViewById(R.id.fireBtn); // 발사 ImageButton
        reloadBtn = findViewById(R.id.reloadBtn); // 장전 ImageButton
        gameFrame = findViewById(R.id.gameFrame); // 첫번째 LinerLayout
        pauseBtn = findViewById(R.id.pauseBtn); // 정지 ImageView
        lifeFrame = findViewById(R.id.lifeFrame); // 두번째 LinerLayout

        init(); // 게임 시작 시 초기화
        setBtnBehavior(); //조이스틱 작동함수 실행
    }

    // 엑티비티가 일시 중지된 후 다시 실행될 때
    @Override
    protected void onResume() {
        super.onResume();
        bgMusic.start(); // 배경음악 미디어플레이어 시작
        spaceInvadersView.resume(); //spaceInvadersView에 있는 resume 시작 ( 쓰레드 )
    }

    // 초기화를 위한 메서드
    private void init() {
        // WindowManager는 안드로이드에서 창과 뷰를 관리하는 시스템 서비스.Display객체를 반환 ( 이를 통해 액티비티가 실행되고 있는 디바이스의 디스플레이 정보를 얻을 수 있다 )　Display는 디스플레이 정보를 나타냄
        Display display = getWindowManager().getDefaultDisplay(); // view의 display를 얻어온다.
        Point size = new Point(); // Point 좌표를 나타냄 ( 화면 크기 )
        display.getSize(size); // display의 크기를 size에 저장

        //아이템 화면에 띄우기 ★
        spaceInvadersView = new SpaceInvadersView(this, userIntent.getIntExtra("character", R.drawable.ship_0000), size.x, size.y); // spaceInvadersView(컨스턴스 객체, (startActivity에서 받아온 character, 비행기),x,y)
        gameFrame.addView(spaceInvadersView); //프레임에 만든 아이템들 넣기

        //음악바꾸기 - 게임시작시 배경음악을 틀어야됨
        changeBgMusic();
        bgMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //음악이 끝나면
            @Override
            public void onCompletion(MediaPlayer mp) {
                changeBgMusic();
            }
        });

        bulletCount = findViewById(R.id.bulletCount); //총알 개수 id 가져오기

        //spaceInvadersView의 getPlayer() 구현
        bulletCount.setText(spaceInvadersView.getPlayer().getBulletsCount() + "/30"); // 30/30 표시
        scoreTv.setText(Integer.toString(spaceInvadersView.getScore())); // score: 0 표시

        effectSoundList = new ArrayList<>(); // 효과음을 담을 리스트 생성 ( 밑에 차례대로 추가 )
        // effectSoundList.add(리스트에 사운드 효과를 추가할 인덱스,effectSound.load(컨텍스트, 사운드 파일, 우선순위))
        effectSoundList.add(PLAYER_SHOT, effectSound.load(MainActivity.this, R.raw.player_shot_sound, 1));
        effectSoundList.add(PLAYER_HURT, effectSound.load(MainActivity.this, R.raw.player_hurt_sound, 1));
        effectSoundList.add(PLAYER_RELOAD, effectSound.load(MainActivity.this, R.raw.reload_sound, 1));
        effectSoundList.add(PLAYER_GET_ITEM, effectSound.load(MainActivity.this, R.raw.player_get_item_sound, 1));
        bgMusic.start(); // 배경음악 실행
    }

    //음악 체인지
    private void changeBgMusic() {
        bgMusic = MediaPlayer.create(this, bgMusicList.get(bgMusicIndex)); // 미디어 플레이어 생성 (컨스턴스 객체, 배경음악(초기값은 0) )
        bgMusic.start(); // 배경 음악 시작
        bgMusicIndex++; // 처음 게임 시작 시 처음 음악을 틀어야 하니 시작 후 인덱스 추가
        bgMusicIndex = bgMusicIndex % bgMusicList.size(); // 음악갯수 만큼 넣기
    }

    @Override
    protected void onPause() { //일시 정지시
        super.onPause();
        bgMusic.pause(); // 배경 음악 일시 정지
        spaceInvadersView.pause(); // spaceIvaderView View 일시 정지 
    }

    public static void effectSound(int flag) { // StartShipSprite에서 사용
        effectSound.play(effectSoundList.get(flag), effectVolumn, effectVolumn, 0, 0, 1.0f); // Soundpool 실행 (효과음.get(StartShipSprite 가져온 flag), 효과음 볼륨, 우선순위, 반복횟수, 재생 속도)
    }

    //조이스틱 버튼 작용
    private void setBtnBehavior() {
        joyStick.setAutoReCenterButton(true); // 조이스틱에 대해 자동 재센터 기능 활성화 ( 조이스틱 움직인 후에 중심으로 자동으로 복귀 )
        joyStick.setOnKeyListener(new View.OnKeyListener() { // 조이스틱 키 리스너
            // 로그를 찍기 위한 메서드
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("keycode", Integer.toString((i)));
                return false;
            }
        });

        // 조이스틱 이동방향으로 비행기 이동하게 한다.
        joyStick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) { // angle은 조이스틱의 이동 각도를 나타낸다 (0도는 오른쪽을 가리킴) // strength는 조이스틱 이동 강도를 나타냄
                Log.d("angle", Integer.toString(angle));
                Log.d("strength", Integer.toString(strength));
                if (angle > 67.5 && angle < 112.5) {
                    //위
                    spaceInvadersView.getPlayer().moveUp(strength / 10); // ↓ 강도에 따라서 속도 변화
                    spaceInvadersView.getPlayer().resetDx();
                } else if (angle > 247.5 && angle < 292.5) {
                    //아래
                    spaceInvadersView.getPlayer().moveDown(strength / 10);
                    spaceInvadersView.getPlayer().resetDx();
                } else if (angle > 112.5 && angle < 157.5) {
                    //왼족 대각선 위
                    spaceInvadersView.getPlayer().moveUp(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveLeft(strength / 10 * 0.5);
                } else if (angle > 157.5 && angle < 202.5) {
                    //왼쪽
                    spaceInvadersView.getPlayer().moveLeft(strength / 10);
                    spaceInvadersView.getPlayer().resetDy();
                } else if (angle > 202.5 && angle < 247.5) {
                    //왼쪽 대각선 아래
                    spaceInvadersView.getPlayer().moveLeft(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveDown(strength / 10 * 0.5);
                } else if (angle > 22.5 && angle < 67.5) {
                    //오른쪽 대각선 위
                    spaceInvadersView.getPlayer().moveUp(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveRight(strength / 10 * 0.5);
                } else if (angle > 337.5 || angle < 22.5) {
                    //오른쪽
                    spaceInvadersView.getPlayer().moveRight(strength / 10);
                    spaceInvadersView.getPlayer().resetDy();
                } else if (angle > 292.5 && angle < 337.5) {
                    //오른쪽 아래
                    spaceInvadersView.getPlayer().moveRight(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveDown(strength / 10 * 0.5);
                }
            }
        });

        //총알 쏘는 버튼 리스너
        fireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaceInvadersView.getPlayer().fire(); //SpaceInvadersView -> StarshipSprite -> fire()
            }
        });

        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spaceInvadersView.getPlayer().reloadBullets();
                //SpaceInvadersView -> StarshipSprite -> reloadButtlets()
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() { // 정지 버튼(ImageView) 리스너
            @Override
            public void onClick(View v) {
                spaceInvadersView.pause(); //spaceInvadersView 일시정지
                PauseDialog pauseDialog = new PauseDialog(MainActivity.this); // PauseDialog 객체 생성
                pauseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // setOnDismissListener 는 다이얼로그가 닫힐 떼 호출되는 이벤트 리스너
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        spaceInvadersView.resume(); //spaceInvadersView 종료
                    }
                });
                pauseDialog.show(); //pauseDialog 띄움
            }
        });

        // 필사기 버튼 리스너
        specialShotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spaceInvadersView.getPlayer().getSpecialShotCount() >= 0) // 필사기 카운터가 0보다 많거나 같으면 필사기 사용
                    spaceInvadersView.getPlayer().specialShot();
            }
        });

    }
}