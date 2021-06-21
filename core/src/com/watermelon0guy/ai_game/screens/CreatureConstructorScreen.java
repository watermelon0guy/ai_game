package com.watermelon0guy.ai_game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.watermelon0guy.ai_game.BackgroundGrid;
import com.watermelon0guy.ai_game.Creature;
import com.watermelon0guy.ai_game.aiGame;
import com.watermelon0guy.ai_game.inputs.ConstructorInputManager;
import com.watermelon0guy.ai_game.utils;

import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.watermelon0guy.ai_game.StaticVars.*;

public class CreatureConstructorScreen implements Screen {

    aiGame mainClass;
    GameScreen gameScreen;
    Stage stage;
    Skin skin;
    public Camera camera;
    Viewport viewport;
    Viewport uiViewport;
    SpriteBatch batch;
    ShapeDrawer shapeDrawer;
    BackgroundGrid bgg = new BackgroundGrid();

    Box2DDebugRenderer b2dr;

    public Creature creature;
    InputMultiplexer inputSum;
    ConstructorInputManager cIM = new ConstructorInputManager(this);

    public World world;//здесь он чисто для галочки. мы даже его не счетаем
    public BodyDef bodyDefBone;
    public BodyDef bodyDefJoint;
    public DistanceJointDef springDef;
    public PolygonShape shape;

    public CircleShape circleShape;
    public FixtureDef fixture;


    TextButton buttonJoint, buttonBone, buttonMuscle, buttonMove, buttonDelete, buttonEvo;

    public CreatureConstructorScreen(final aiGame mainClass)
    {
        this.mainClass = mainClass;

        world = new World(new Vector2(0,0),false);
        bodyDefBone = utils.createBodyDef(BodyDef.BodyType.DynamicBody,false);
        bodyDefJoint = utils.createBodyDef(BodyDef.BodyType.DynamicBody,true);
        springDef = new DistanceJointDef();
        springDef.dampingRatio = damping;
        springDef.frequencyHz = frequency;
        shape = new PolygonShape();
        circleShape = new CircleShape();
        circleShape.setRadius(radius);
        fixture = new FixtureDef();

        //region Инициализация графона
        b2dr = mainClass.b2dr;
        camera = new OrthographicCamera();
        batch = mainClass.batch;
        shapeDrawer = mainClass.shapeDrawer;
        viewport = new ExtendViewport(9.5f,4.5f, camera);
        uiViewport = new ScreenViewport();
        stage = new Stage(uiViewport,batch);
        //camera.translate(-3,-1,0);
        //endregion
        inputSum = new InputMultiplexer(stage,cIM);
        Gdx.input.setInputProcessor(inputSum);

        creature = new Creature();
    }

    @Override
    public void show() {
        stage.clear();
        //region инициализация скина
        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        skin.load(Gdx.files.internal("ui/uiskin.json"));
        //endregion
        initButtons();
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255,250,240,1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        //b2dr.render(world,camera.combined);
        batch.begin();
        bgg.render(shapeDrawer);
        creature.render(shapeDrawer);
        batch.end();
        stage.act();
        uiViewport.apply();
        stage.draw();

        update(delta);
        //b2dr.render(world,camera.combined);
    }

    private void initButtons() {
        //region Инициализация кнопок конструктора
        Table table = new Table();
        table.align(Align.bottomLeft);
        table.pad(10);
        //table.setDebug(true);
        buttonJoint = new TextButton("Joint",skin,"white");
        buttonJoint.getLabel().setFontScale(2);
        buttonBone = new TextButton("Bone",skin,"white");
        buttonBone.getLabel().setFontScale(2);
        buttonMuscle = new TextButton("Muscle",skin,"white");
        buttonMuscle.getLabel().setFontScale(2);
        buttonMove = new TextButton("Move",skin,"white");
        buttonMove.getLabel().setFontScale(2);
        buttonDelete = new TextButton("Delete",skin,"white");
        buttonDelete.getLabel().setFontScale(2);

        buttonEvo = new TextButton("EVO",skin,"white");
        buttonEvo.getLabel().setFontScale(2);

        table.add(buttonJoint).align(Align.left).padBottom(15).padTop(15).width(uiViewport.getScreenWidth()/9).height(uiViewport.getScreenHeight()/10);
        table.row();
        table.add(buttonBone).align(Align.left).padBottom(15).padTop(15).width(uiViewport.getScreenWidth()/9).height(uiViewport.getScreenHeight()/10);
        table.row();
        table.add(buttonMuscle).align(Align.left).padBottom(15).padTop(15).width(uiViewport.getScreenWidth()/9).height(uiViewport.getScreenHeight()/10);
        table.row();
        table.add(buttonMove).align(Align.left).padBottom(15).padTop(15).width(uiViewport.getScreenWidth()/9).height(uiViewport.getScreenHeight()/10);
        table.row();
        table.add(buttonDelete).align(Align.left).padBottom(15).padTop(15).width(uiViewport.getScreenWidth()/9).height(uiViewport.getScreenHeight()/10);
        table.row();
        table.add(buttonEvo).align(Align.left).padBottom(15).padTop(15).width(uiViewport.getScreenWidth()/9).height(uiViewport.getScreenHeight()/10);
        table.setTransform(true);
        //table.setFillParent(true);
        stage.addActor(table);
        //endregion

        buttonJoint.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cIM.status = ConstructorInputManager.constructorStatus.Joint;
            }
        });

        buttonBone.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cIM.status = ConstructorInputManager.constructorStatus.Bone;
            }
        });

        buttonMuscle.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cIM.status = ConstructorInputManager.constructorStatus.Muscle;
            }
        });

        buttonMove.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cIM.status = ConstructorInputManager.constructorStatus.Move;
            }
        });

        buttonDelete.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cIM.status = ConstructorInputManager.constructorStatus.Delete;
            }
        });

        buttonEvo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen = new GameScreen(mainClass.constructorScreen);
                mainClass.setScreen(gameScreen);
            }
        });
    }

    private void update(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        uiViewport.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
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
        gameScreen.dispose();
    }
}
