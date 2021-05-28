package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class BoneClass {
    Creature parent;
    Color boneColor = Color.DARK_GRAY;
    float length;
    float thickness=0.08f;
    //физика
    Body pBody;
    Vector2 pos(){return pBody.getPosition();}
    float angle(){return pBody.getAngle();}
    BodyDef bodyDef;
    float density = 500;
    float friction = 1;
    FixtureDef fixture = new FixtureDef();
    ArrayList<MuscleClass> connectedMuscles = new ArrayList<MuscleClass>();
    ArrayList<JointClass> joints = new ArrayList<JointClass>();

    //конструктор
    BoneClass(float length)
    {
        this.length = length;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //настройка коллайдера
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(length/2,thickness/2);
        //физические свойства
        fixture.shape = shape;
        fixture.friction = friction;
        fixture.density = density;
        fixture.filter.categoryBits = StaticVars.BONES;
        fixture.filter.maskBits = StaticVars.STATIC_FLOOR;
    }

    void init(Vector2 coord, float angle, World world)
    {
        pBody = world.createBody(bodyDef);
        pBody.createFixture(fixture).setUserData(this);;
        pBody.setTransform(coord, angle);
    }

    public void render(ShapeDrawer shapeDrawer)
    {
        //shapeDrawer.filledRectangle(pos().x-0.67f, pos().y, length, thickness, angle(), boneColor, boneColor);
        shapeDrawer.line(joints.get(0).pos().x,joints.get(0).pos().y,joints.get(1).pos().x,joints.get(1).pos().y,thickness,boneColor,boneColor);
    }
}
