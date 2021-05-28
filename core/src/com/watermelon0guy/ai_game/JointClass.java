package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class JointClass {
    public Creature parent;
    Color jointColor = new Color(1f/255f * 66f,1f/255f * 170f,1f/255f * 255f,1);
    float radius = 0.1f;
    //физика
    Body pBody;
    Vector2 pos(){return pBody.getPosition();}
    float angle(){return pBody.getAngle();}
    BodyDef bodyDef;
    FixtureDef fixture = new FixtureDef();
    float density = 1250;
    float friction = 1;
    ArrayList<BoneClass> connectedBones;
    ArrayList<JointClass> neighbourJoints;
    ArrayList<JointClass> floatingJoints;

    //конструктор
    JointClass()
    {
        connectedBones = new ArrayList<BoneClass>();
        neighbourJoints = new ArrayList<JointClass>();

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        //настройка коллайдера
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        //физ свойства
        fixture.shape = shape;
        fixture.friction = friction;
        fixture.density = density;
        fixture.filter.categoryBits = StaticVars.JOINTS;
        fixture.filter.maskBits = StaticVars.STATIC_FLOOR;
    }

    void init(Vector2 coord, World world)
    {
        pBody = world.createBody(bodyDef);
        pBody.createFixture(fixture).setUserData(this);
        pBody.setTransform(coord,0);
    }

    public void connectJointToBones(World world)
    {
        RevoluteJointDef jointDef = new RevoluteJointDef();

        for (BoneClass bone: connectedBones) {
            jointDef.bodyA = pBody;
            jointDef.bodyB = bone.pBody;
            jointDef.localAnchorB.set(bone.pBody.getLocalPoint(pos()));
            world.createJoint(jointDef);
        }
    }

    public void addNeighbour(JointClass neigbour)
    {
        neighbourJoints.add(neigbour);
        floatingJoints = new ArrayList<JointClass>(neighbourJoints);
    }

    public void render(ShapeDrawer shapeDrawer)
    {
        shapeDrawer.filledCircle(pBody.getPosition().x, pBody.getPosition().y,0.1f, jointColor);
    }
}
