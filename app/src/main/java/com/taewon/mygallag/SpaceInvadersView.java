package com.taewon.mygallag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.taewon.mygallag.sprites.AlienSprite;
import com.taewon.mygallag.sprites.Sprite;
import com.taewon.mygallag.sprites.StarshipSprite;

import java.util.ArrayList;
import java.util.Random;

public class SpaceInvadersView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    //SurfaceView 는 스레드를 이용해 강제로 화면에 그려주므로 View보다 바르다. 애니메이션, 영상 처리에 이용
    //SurfaceHolder.Callback Surface의 변화감지를 위해 필요. 지금처럼 SurfaceView와 거의 같이 사용한다.

    private static int MAX_ENEMY_COUNT = 10;
    private Context context;
    private int characterId;
    private SurfaceHolder ourHolder; //화면에 그리는데 View보다 빠리게 그려준다.
    private Paint paint;
    public int screenW, screenH;
    private Rect src, dst; // 사각형 그리는 클래스
    private ArrayList sprites = new ArrayList();
    private Sprite starship;
    private int score, currEnemyCount;
    private Thread gameThread = null;
    private volatile boolean running; // 휘발성부울 함수
    private Canvas canvas;
    int mapBitmapY = 0;

    public SpaceInvadersView(Context context, int characterId, int x, int y) {
        super(context);
        this.context = context;
        this.characterId = characterId;
        ourHolder = getHolder(); // 현재 SurfaceView를 리턴받는다.
        paint = new Paint();
        screenW = x; // 받아온 x,y
        screenH = y;
        src = new Rect(); //원본 사각형
        dst = new Rect(); //사본 사격형
        dst.set(0, 0, screenW, screenH); // 시작 x,y 와 끝 x,y
        startGame();
    }

    private void startGame() { // 게임 시작 메서드
        sprites.clear(); //Arraylist 지우기
        initSprites(); //ArrayList에 침략자 아이템들 추가하기
        score = 0; // 게임 시작 시 점수를 0으로 초기화
    }

    public void endGame() { // 게임 오버 메서드
        Log.e("Game Over", "Game Over");
        Intent intent = new Intent(context, ResultActivity.class); // ResultActivity 연결
        intent.putExtra("score", score); // ResultActivity 액티비티에 스코어 점수 보내줌
        context.startActivity(intent); // ResultActivity 실행
        gameThread.stop(); // 스레드 중지
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    } // sprite 삭제

    private void initSprites() {
        starship = new StarshipSprite(context, this, characterId, screenW / 2, screenH - 400, 1.5f); //StartShipSprite 생성 아이템들 생성
        sprites.add(starship); //ArrayList에 추가
        spawnEnemy(); // 외계인 생성을 위한 메서드 호출
        spawnEnemy();
    }

    public void spawnEnemy() {
        Random r = new Random();
        int x = r.nextInt(300) + 100;
        int y = r.nextInt(300) + 100;
        //외계인 아이템
        Sprite alien = new AlienSprite(context, this, R.drawable.ship_0002, 100 + x, 100 + y); // 외계인을 랜덤값을 이용해서 초기값 // ALienSprite가 Sprite를 상속받고 있기 때문 ( 부모를 쓰는게 좋음 )
        sprites.add(alien); // 외계인 생성
        currEnemyCount++; //외계인 아이템 개수 증가
    }

    public ArrayList getSprites() {
        return sprites;
    } // sprites를 가져온다

    public void resume() { //사용자가 만드는 resume()함수
        running = true;
        gameThread = new Thread(this); // 스레드 생성
        gameThread.start(); // 스레드 시작
    }

    // Sprite 를 StartshipSprite로 형병환하여 리턴하기
    public StarshipSprite getPlayer() {
        return (StarshipSprite) starship; // startship 변수를 StarshipSprite 타입으로 반환
    }

    public int getScore() {
        return score;
    } // 스코어 getter

    public void setScore(int score) {
        this.score = score;
    } // 스코어 setter

    public void setCurrEnemyCount(int currEnemyCount) { // setCurrEnemyCount setter
        this.currEnemyCount = currEnemyCount;
    }

    public int getCurrEnemyCount() {
        return currEnemyCount;
    } // setCurrEnemyCount getter

    public void pause() {
        running = false;
        try {
            gameThread.join(); //스레드 종료 대기하기
        } catch (InterruptedException e) {

        }
    }

    //   ==========================
    @Override
    public void run() {
        while (running) {
            Random r = new Random();
            boolean isEnemySpawn = r.nextInt(100) + 1 < (getPlayer().speed + (int) (getPlayer().getPowerLevel() / 2)); // 스피드와 파워 증가시 적 비행기 출현 확률 업
            if (isEnemySpawn && currEnemyCount < MAX_ENEMY_COUNT) spawnEnemy(); // ??? 추가

            for (int i = 0; i < sprites.size(); i++) {
                Sprite sprite = (Sprite) sprites.get(i); //ArrayList에꺼 하나씩 가져와서 움직이기 (sprites는 ArrayList)
                sprite.move(); // 화면에 추가된 모든 객체들의 이동을 구현
            }

            // sprites(배열) 크기 만큼 반복
            for (int p = 0; p < sprites.size(); p++) {
                for (int s = p + 1; s < sprites.size(); s++) {
                    try {
                        Sprite me = (Sprite) sprites.get(p);
                        Sprite other = (Sprite) sprites.get(s);
                        //충돌체크
                        if (me.checkCollision(other)) { // 두 객체 간의 충돌 여부를 판단
                            me.handleCollision(other);
                            other.handleCollision(me);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            draw();
            try {
                Thread.sleep(10); // 0.01
            } catch (Exception e) {

            }
        }
    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas(); // ourHolder를 잠금한다. CPU에서 랜더링하기 위한 버퍼를 잠그고 그리기에 사용할 수 있도록 캔버스 반환
            canvas.drawColor(Color.BLACK); // 검은색으로 채운다.
            mapBitmapY++; // 맵 비트맵을 하나씩 증가 시킨다.
            if (mapBitmapY < 0) mapBitmapY = 0;
            paint.setColor(Color.BLUE); // paint 색상을 파란색으로 교체
            for (int i = 0; i < sprites.size(); i++) { // sprites 사이즈 만큼 반복
                Sprite sprite = (Sprite) sprites.get(i); // sprites를 하나씩 가져온다
                sprite.draw(canvas, paint); // sprite를 그려준다
            }
            ourHolder.unlockCanvasAndPost(canvas); // 잠금된 canvas를 잠금 해체하고 변경
        }
    }

    // SurfaceHolder.Callback 인터페이스의 메서드 
    // SurfaceHolder가 생성될 때 호출
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        startGame();
    }

    // SurfaceHolder 의 크기 또는 형식이 변경될 때 호출
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    // SurfaceHolder가 파괴될 때 호출
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
    }


}
