package com.imaginegames.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imaginegames.game.MyGameMain;

public class Explosion {

	private MyGameMain game;
	private static final int PWIDTH = 256;
	private static final int PHEIGHT = 256;
	public static float WIDTH;
	public static float HEIGHT;
	private static final float ANIMATION_SPEED = 0.04f;
	private static final float SCALE = 1f;
	
	private static Animation<?> ANIMATION;
	private float stateTime;
	private float x, y;
	
	public boolean remove = false;
	
	public Explosion (MyGameMain game, float firstRectX, float secondRectX, float firstRectWIDTH, float secondRectWIDTH, float firstRectY, float secondRectY, float firstRectHEIGHT, float secondRectHEIGHT, float EXPLOSION_SIZE) {
		this.game = game;
		this.x = Math.min(firstRectX, secondRectX) + (firstRectWIDTH + secondRectWIDTH - WIDTH) / 2;
		this.y = Math.min(firstRectY, secondRectY) + (firstRectHEIGHT + secondRectHEIGHT - HEIGHT) / 2;
		WIDTH = PWIDTH * EXPLOSION_SIZE * SCALE;
		HEIGHT = PHEIGHT * EXPLOSION_SIZE * SCALE;
		stateTime = 0;
		TextureRegion[][] animation_sheet = TextureRegion.split(game.assets.get("explosion_sheet.png", Texture.class), PWIDTH, PHEIGHT);
		
		if (ANIMATION == null) {
			ANIMATION = new Animation<>(ANIMATION_SPEED, animation_sheet[0]);
		}
	}
	
	public void update(float delta) {
		stateTime += delta;
		if (ANIMATION.isAnimationFinished(stateTime)) {
			remove = true;
		}
	}
	
	public void render(SpriteBatch batch) {
		batch.draw((TextureRegion) ANIMATION.getKeyFrame(stateTime), x, y, WIDTH, HEIGHT);
	}
	
}
