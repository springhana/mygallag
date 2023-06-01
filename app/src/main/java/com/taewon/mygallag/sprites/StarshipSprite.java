package com.taewon.mygallag.sprites;


import android.content.Context;
import android.graphics.RectF;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpecialItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;

public class StarshipSprite extends Sprite {

    Context context;        // 메인 액티비티
    SpaceInvadersView game; // 게임 진행에 필요한 함수 사용
    public float speed;     // 플레이어 캐릭터 속도
    private int bullets, life = 3, powerLevel;    // 총알, 라이프, 파워 레벨
    private int specialShotCount;   // 특수 공격 갯수
    private boolean isSpecialShooting;  // 특수 공격을 사용중인가
    private static ArrayList<Integer> bulletSprites = new ArrayList<Integer>(); // 총알 이미지들 모음
    private final static float MAX_SPEED = 3.5f;    // 플레이어 최대 스피드
    private final static int MAX_HEART = 3;     // 플레이어 최대 목숨
    private RectF rectF;        // 플레이어 캐릭터의 HittingBox 좌표
    private boolean isReloading = false;

    public StarshipSprite(Context context, SpaceInvadersView game, int resId, int x, int y, float speed) {
        // 생성시 들어온 매개변수를 저장 및 초기화
        super(context, resId, x, y);
        this.context = context;
        this.game = game;
        this.speed = speed;
        init();
    }

    public void init() {
        dx = dy = 0;    // 움직임 초기화
        bullets = 30; // 총알 초기화
        life = 3;     // 라이프 초기화
        specialShotCount = 3; // 특수공격 수 초기화
        powerLevel = 0;   // 파워레벨 초기화
        Integer[] shots = {R.drawable.shot_001, R.drawable.shot_002, R.drawable.shot_003, R.drawable.shot_004,
                R.drawable.shot_005, R.drawable.shot_006, R.drawable.shot_007};    // 파워레벨에 따른 총알 모양 변경
        for (int i = 0; i < shots.length; i++) {
            bulletSprites.add(shots[i]);
        }

    }

    @Override
    public void move() { // 플레이어 움직임 구현
        //벽에 부딪히면 못 가게 하기
        if ((dx < 0) && (x < 120)) return;
        if ((dx > 0) && (x > game.screenW - 120)) return;
        if ((dy < 0) && (y < 120)) return;
        if ((dy > 0) && (y > game.screenH - 120)) return;
        super.move(); //슈퍼클래스 가서 x,y 위치 다시지정
    }

    //총알 개수 리턴
    public int getBulletsCount() {
        return bullets;
    }

    //번개 개수 리턴
    public int getspecialShotCountCount() {
        return specialShotCount;
    }


    //위, 아래, 오른쩍, 왼쪽 이동하기
    public void moveRight(double force) {
        setDx((float) (1 * force * speed));
    }

    public void moveLeft(double force) {
        setDx((float) (-1 * force * speed));
    }

    public void moveDown(double force) {
        setDy((float) (1 * force * speed));
    }

    public void moveUp(double force) {
        setDy((float) (-1 * force * speed));
    }

    public void resetDx() {
        setDx(0);
    }

    public void resetDy() {
        setDy(0);
    }

    // 스피드 제어
    public void plusSpeed(float speed) {
        this.speed += speed;
    }

    // 총알 발사
    public void fire() {
        if (isReloading | isSpecialShooting) { // 총알을 리로딩중이거나 특수공격 사용중엔 총알 발사 못함
            return;
        }
        MainActivity.effectSound(MainActivity.PLAYER_SHOT); // 총알 발사 할 때마다 총쏘는 효과음 발생
        // 총알 발사시 ShotSprite 생성
        ShotSprite shot = new ShotSprite(context, game, bulletSprites.get(powerLevel), getX() + 10, getY() - 30, -16);

        // 화면에 나타나는 객체들의 ArrayList에 총알 추가
        game.getSprites().add(shot);
        bullets--;// 소지중인 총알 갯수 1개 소진

        MainActivity.bulletCount.setText(bullets + "/30"); // 화면에 소지 총알 텍스트 변경
        Log.d("bullets", bullets + "/30");
        if (bullets == 0) {  // 총알 소진시 리로딩
            reloadBullets();
            return;
        }
    }

    public void powerUp() {  // 파워업 아이템과 충돌시 파워레벨이 올라감. 최대 파워레벨일시 스코어 증가
        if (powerLevel >= bulletSprites.size() - 1) {
            game.setScore(game.getScore() + 1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
            return;
        }
        powerLevel++;
        MainActivity.fireBtn.setImageResource(bulletSprites.get(powerLevel)); // 파워레벨이 올라가면서 총알 모양도 바뀜
        MainActivity.fireBtn.setBackgroundResource(R.drawable.round_button_shape);
    }

    public void reloadBullets() {   // 총알 리로딩 함수
        isReloading = true;     // 리로딩 bool 을 true로 변경
        MainActivity.effectSound(MainActivity.PLAYER_RELOAD);   // 리로딩 효과음발생
        MainActivity.fireBtn.setEnabled(false); // 리로딩 하는 동안 총알 발사 버튼 사용 못하게 막기
        MainActivity.reloadBtn.setEnabled(false);   // 리로딩 하는 동안 리로딩 버튼 사용 못하게 막기

        new Handler().postDelayed(new Runnable(){   // 리로딩은 2초간 진행. 리로딩이 끝나면 총알 갯수는 다시 30개로 채워지며 버튼들 사용가능
            @Override
            public void run() {
                bullets = 30;
                MainActivity.fireBtn.setEnabled(true);
                MainActivity.reloadBtn.setEnabled(true);
                MainActivity.bulletCount.setText(bullets+"/30");
                MainActivity.bulletCount.invalidate();
                isReloading=false;
            }
        },2000);
    }

    // 필사기
    public void specialShot() {
        specialShotCount--;
        //SpecialshotSprite 구현
        SpecialshotSprite shot = new SpecialshotSprite(context, game, R.drawable.laser, getRect().right - getRect().left, 0);
        // game -> SpaceInvadersView의 getSprites() : sprite에 shot 추가하기
        game.getSprites().add(shot);

        // 따로 추가 했음
        MainActivity.specialShotBtn.setEnabled(false);
        MainActivity.specialShotCount.setText(specialShotCount + "/3"); // 메인으로 넘겨주기
        MainActivity.specialShotCount.invalidate(); //화면 새로고침
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.specialShotBtn.setEnabled(true);
            }
        }, 5000);

    }

    public int getSpecialShotCount() {
        return specialShotCount;
    }

    public boolean isSpecialShooting() {
        return isSpecialShooting;
    }

    public void setSpecialShooting(boolean specialShooting) {
        isSpecialShooting = specialShooting;
    }

    //생명 잃었을때
    public int getLife() {
        return life;
    }

    public void hurt() {
        life--;
        if (life <= 0) {
            ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource((R.drawable.ic_baseline_favorite_24));
            //SpaceInvadersView의 endGame() 에서 game 종료시키기
            game.endGame();
            return;
        }
        Log.d("hurt", Integer.toString(life)); //생명확인하기
        ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }

    //생명 얻었을 때
    public void heal() {
        Log.d("heal", Integer.toString(life));
        if (life + 1 > MAX_HEART) {
            game.setScore(game.getScore() + 1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
            return;
        }
        ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource(R.drawable.ic_baseline_favorite_24);
        life++;
//        specialShotCount++;
    }

    //속도 올리기
    private void speedUp() {
        if (MAX_SPEED >= speed + 0.2f) plusSpeed(0.2f);
        else {
            game.setScore(game.getScore() + 1);
            MainActivity.scoreTv.setText(Integer.toString((game.getScore())));
        }
    }

    private void specialUp() {
        if (specialShotCount >= 3) {
            specialShotCount = 3;
            MainActivity.specialShotCount.invalidate(); //화면 새로고침
        } else {
            specialShotCount++;
            MainActivity.specialShotCount.invalidate(); //화면 새로고침
            MainActivity.specialShotCount.setText(specialShotCount + "/3"); // 메인으로 넘겨주기
        }
    }

    //Sprite의 handleCollision() -> 충돌처리
    @Override
    public void handleCollision(Sprite other) {  // 플레이어와 다른 객체가 충돌 했을 때의 기능 구현
        if (other instanceof AlienSprite) {  // 적과 충돌했을시 라이프 감소
            //외계인 맞으면
            game.removeSprite(other);
            MainActivity.effectSound(MainActivity.PLAYER_HURT);
            hurt();
        }

        if (other instanceof AlienShotSprite) { // 스피드업 아이템과 충돌했을시 스피드증가 또는 스코어증가
            //총알 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_HURT);
            game.removeSprite(other);
            hurt();
        }
        if (other instanceof PowerItemSprite) {  // 적의 총알과 충돌했을시 라이프 감소
            //파워아이템을 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            powerUp();
            game.removeSprite(other);
        }
        if (other instanceof HealitemSprite) { // 파워 아이템과 충돌했을시 파워레벨 증가 또는 스코어증가
            //생명아이템 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            game.removeSprite(other);
            heal();
        }
        if (other instanceof SpeedItemSprite) { // 힐링 아이템과 충돌했을시 라이프 증가
            //스피드아이템 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            game.removeSprite(other);
            speedUp();
        }
        if (other instanceof SpecialItemSprite) {
            //빌사기 아이템 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            game.removeSprite(other);
            specialUp();
        }
    }

    public int getPowerLevel() {
        return powerLevel;
    }
}
