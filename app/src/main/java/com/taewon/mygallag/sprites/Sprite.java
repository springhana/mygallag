package com.taewon.mygallag.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Sprite {
    protected float x, y;
    protected int width, height;
    protected float dx, dy;
    private Bitmap bitmap;
    protected int id;
    private RectF rect; // 사각형의 좌표를 표현하기 위해 사용

    public Sprite(Context context, int resourceId, float x, float y) {
        this.id = resourceId;
        this.x = x;
        this.y = y; 
        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId); // 안드로이드에서 비트맵을 생성하고 디코딩 ( 앱의 리소스를 비트맵으로 변환하여 사용 가능 )
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        rect = new RectF();
    }

//  width,height getter
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, x, y, paint); // canvas에 비트맵을 그린다.(그려질 비트맵, x좌표, y좌표, paint)
    }

    public void move() {
        x = x + dx;
        y = y + dy;
        rect.left = x; // rect 왼쪽 좌표
        rect.right = x + width; // 오른쪽 좌표
        rect.top = y; // 위 좌표
        rect.bottom = y + height; // 아래 좌표
    }

//  setter, getter
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public RectF getRect() {
        return rect;
    }

    // 두개의 RectF 객체를 받아 두 사각형이 교차하는지 여부를 판단
    public boolean checkCollision(Sprite other) {
        return RectF.intersects(this.getRect(), other.getRect());
    }

    // 충돌처리위한
    public void handleCollision(Sprite other) {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
