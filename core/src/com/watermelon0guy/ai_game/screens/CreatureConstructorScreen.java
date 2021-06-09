package com.watermelon0guy.ai_game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.watermelon0guy.ai_game.BackgroundGrid;
import com.watermelon0guy.ai_game.Creature;
import com.watermelon0guy.ai_game.GeneticAlgorithm;
import com.watermelon0guy.ai_game.inputs.ConstructorInputManager;
import com.watermelon0guy.ai_game.physics.MyContactListener;
import com.watermelon0guy.ai_game.utils;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class CreatureConstructorScreen implements Screen {

    Game mainClass;
    Stage stage;
    Camera camera;
    Viewport viewport;
    SpriteBatch batch;
    ShapeDrawer shapeDrawer;
    BackgroundGrid bgg = new BackgroundGrid();

    Box2DDebugRenderer b2dr;

    Creature creature;
    InputMultiplexer inputSum;

    public  CreatureConstructorScreen(final Game mainClass)
    {
        this.mainClass = mainClass;
        TextureRegion region = utils.createWhitePixel();//пиксель которым рисует shape drawer

        world = new World(new Vector2(0,0),false);
        //region Инициализация графона
        b2dr = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        shapeDrawer = new ShapeDrawer(batch,region);
        viewport = new ExtendViewport(10,10, camera);
        stage = new Stage(viewport,batch);
        camera.translate(-3,-1,0);
        //endregion
        inputSum = new InputMultiplexer(stage,new ConstructorInputManager());
        Gdx.input.setInputProcessor(inputSum);

        creature = new Creature();
    }

    World world;
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,250,240,1);
        Gdx.graphics.getGL20().glClearColor(255,250,240,1 );
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );



        b2dr.render(world,camera.combined);
        batch.begin();
        bgg.render(shapeDrawer);
        batch.end();
        stage.act();
        stage.draw();

        update(delta);

    }

    private void update(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
