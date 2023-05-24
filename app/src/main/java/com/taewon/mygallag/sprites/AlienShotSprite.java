package com.taewon.mygallag.sprites;

import android.content.Context;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;

public class AlienShotSprite extends Sprite {
    private Context context; // context = 어플리케이션 환경에 대한 글로버 정보를 갖는 인터페이스
    private SpaceInvadersView game;

    public AlienShotSprite(Context context, SpaceInvadersView game, float x, float y, int dy) {
        super(context, R.drawable.shot_001, x, y);
        this.game = game;
        this.context = context;
        setDy(dy);
    }
}
