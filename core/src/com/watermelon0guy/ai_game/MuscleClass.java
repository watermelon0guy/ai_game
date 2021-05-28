package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class MuscleClass {
    Creature parent;
    BoneClass boneFirst;
    BoneClass boneSecond;
    float lenght;
    float dampning = 0.7f;
    float frequency = 15;
    DistanceJointDef springDef;
    DistanceJoint spring;

    MuscleClass(BoneClass bone1,BoneClass bone2)
    {
        boneFirst = bone1;
        boneSecond = bone2;
        springDef = new DistanceJointDef();
        springDef.bodyA = bone1.pBody;
        springDef.bodyB = bone2.pBody;
        springDef.length = bone1.pBody.getPosition().dst(bone2.pBody.getPosition());
        lenght = bone1.pBody.getPosition().dst(bone2.pBody.getPosition());
        springDef.dampingRatio = dampning;
        springDef.frequencyHz = frequency;
    }

    public void setLength(float length)
    {
        spring.setLength(length * 1f);
    }

    public void init(World world)
    {
        spring = (DistanceJoint)world.createJoint(springDef);
    }

    public void render(ShapeDrawer shapeDrawer)
    {
        shapeDrawer.line(boneFirst.pBody.getPosition().x,boneFirst.pBody.getPosition().y,boneSecond.pBody.getPosition().x,boneSecond.pBody.getPosition().y,0.07f,Color.RED,Color.RED);
    }
}
