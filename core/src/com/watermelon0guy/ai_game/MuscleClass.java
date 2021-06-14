package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class MuscleClass {
    Creature parent;
    public ArrayList<BoneClass> connectedBones = new ArrayList<>();
    float length;
    float damping = 0.7f;
    float frequency = 15;
    DistanceJoint spring;

    public MuscleClass(BoneClass bone1,BoneClass bone2,World world, Creature creature, DistanceJointDef springDef)
    {
        connectedBones.add(bone1);
        connectedBones.add(bone2);
        springDef.bodyA = bone1.pBody;
        springDef.bodyB = bone2.pBody;
        parent = creature;
        creature.muscles.add(this);
        bone1.connectedMuscles.add(this);
        bone2.connectedMuscles.add(this);
        init(world,springDef);
    }

    public void setLength(float length)
    {
        spring.setLength(this.length * length * 2);
    }

    public void rebuild(World world, DistanceJointDef springDef)
    {
        init(world,springDef);
    }

    public void init(World world, DistanceJointDef springDef)
    {
        if (spring != null) world.destroyJoint(spring);
        spring = (DistanceJoint)world.createJoint(springDef);
        length = connectedBones.get(0).pBody.getPosition().dst(connectedBones.get(1).pBody.getPosition());
        springDef.length = length;
    }

    public void delete(World world)
    {
        world.destroyJoint(spring);
        parent.muscles.remove(this);
        connectedBones.get(0).connectedJoints.remove(this);
        connectedBones.get(1).connectedJoints.remove(this);

    }

    public void render(ShapeDrawer shapeDrawer)
    {
        if (connectedBones.size()<2) return;
        shapeDrawer.line(connectedBones.get(0).pBody.getPosition().x,
                connectedBones.get(0).pBody.getPosition().y,
                connectedBones.get(1).pBody.getPosition().x,
                connectedBones.get(1).pBody.getPosition().y,
                0.07f,Color.RED,Color.RED);
    }
}
