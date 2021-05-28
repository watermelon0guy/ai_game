package com.watermelon0guy.ai_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

class GameScreen implements Screen
{
    //region Графон
    Stage stage;
    Camera camera;
    Viewport viewport;
    SpriteBatch batch;
    ShapeDrawer shapeDrawer;
    BackgroundGrid bgg = new BackgroundGrid();
    Texture texture;//для отрисовки фигур

    Box2DDebugRenderer b2dr;
    //endregion

    World world;
    ArrayList<Body> pBodies;
    Vector2 gravity = new Vector2(0,-9.8f);
    GeneticAlgorithm geneticAlgorithm;

    Floor floor;
    GameScreen()
    {
        //region Белый пиксель
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        texture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
        //endregion

        world = new World(gravity,false);
        world.setContactListener(new MyContactListener());
        //region Инициализация графона
        b2dr = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        shapeDrawer = new ShapeDrawer(batch,region);
        viewport = new ExtendViewport(10,10, camera);
        stage = new Stage(viewport,batch);
        Gdx.input.setInputProcessor(stage);
        camera.translate(-3,-1,0);
        //endregion

        floor = new Floor(0,0-2.5f,100,5,world);//бегать то по чему то надо

        geneticAlgorithm = new GeneticAlgorithm(world);
        geneticAlgorithm.spawnPopulation();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,250,240,1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);



        //stage.draw();
        b2dr.render(world,camera.combined);
        batch.begin();
        bgg.render(shapeDrawer);
        floor.render(shapeDrawer);
        //geneticAlgorithm.render(shapeDrawer);
        batch.end();

        update();
    }


    public void update()
    {
        geneticAlgorithm.act();
        world.step(1/60f,3,3);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
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
    public void show() {

    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
