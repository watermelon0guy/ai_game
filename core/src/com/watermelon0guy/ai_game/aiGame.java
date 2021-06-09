package com.watermelon0guy.ai_game;

import com.badlogic.gdx.Game;
import com.watermelon0guy.ai_game.screens.*;

public class aiGame extends Game {
	GameScreen gameScreen;
	CreatureConstructorScreen constructorScreen;


	@Override
	public void create() {
		constructorScreen = new CreatureConstructorScreen(this);
		setScreen(constructorScreen);
	}

	@Override
	public void dispose() {

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
