package com.watermelon0guy.ai_game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.watermelon0guy.ai_game.screens.*;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class aiGame extends Game {
	GameScreen gameScreen;
	public CreatureConstructorScreen constructorScreen;
	public Box2DDebugRenderer b2dr;
	public SpriteBatch batch;
	public ShapeDrawer shapeDrawer;


	@Override
	public void create() {
		TextureRegion region = utils.createWhitePixel();//пиксель которым рисует shape drawer
		b2dr = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		shapeDrawer = new ShapeDrawer(batch,region);
		constructorScreen = new CreatureConstructorScreen(this);
		setScreen(constructorScreen);
	}

	@Override
	public void dispose() {
		batch.dispose();
		b2dr.dispose();
		constructorScreen.dispose();
		//gameScreen.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
