package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class MuscleClass {
    Creature parent;
    public ArrayList<BoneClass> connectedBones = new ArrayList<>();
    float length;
    DistanceJoint spring;

    public int id;

    enum MuscleStatus
    {
        EXPAND, CONTRACT
    }
    MuscleStatus status = MuscleStatus.EXPAND;

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
        id = creature.getUniqueId();
    }

    public void setLength(float output)
    {
//        if (output < 0)
//        {
//            status = MuscleStatus.CONTRACT;
//        }
//        else {
//            status = MuscleStatus.EXPAND;
//        }
//        output = Math.abs(output);
//        float maxForce = StaticVars.maxForce;
//        float currentForce = output * maxForce;
//
//        if (status == MuscleStatus.CONTRACT)
//        {
//            contract(currentForce);
//        }
//        else {
//            expand(currentForce);
//        }

        float len = length + length * output;

        if (len > 1.2f)
        {
            len = 1.2f;
        }
        else if (len < 0.3f)
        {
            len = 0.3f;
        }
        spring.setLength(len);
    }

    public void expand(float force) {
        Vector2 startingPoint = new Vector2(connectedBones.get(0).pos());
        Vector2 endingPoint = new Vector2(connectedBones.get(1).pos());

        Vector2 midPoint = new Vector2(startingPoint.add(endingPoint).scl(0.5f));

        Vector2 endingForce = endingPoint.sub(midPoint).nor();
        Vector2 startingForce = startingPoint.sub(midPoint).nor();

        applyForces(force, startingForce, endingForce);
    }

    public void contract(float force) {

        Vector2 startingPoint = new Vector2(connectedBones.get(0).pos());
        Vector2 endingPoint = new Vector2(connectedBones.get(1).pos());

        Vector2 midPoint = startingPoint.add(endingPoint).scl(0.5f);

        Vector2 endingForce = midPoint.sub(endingPoint).nor();
        Vector2 startingForce = midPoint.sub(startingPoint).nor();

        applyForces(force, startingForce, endingForce);
    }

    void applyForces(float force, Vector2 startingForce, Vector2 endingForce) {

        Vector2 scaleVector = new Vector2(force, force);
        endingForce.scl(scaleVector);
        startingForce.scl(scaleVector);

        connectedBones.get(0).pBody.applyForce(startingForce, connectedBones.get(0).pos(),true);
        connectedBones.get(1).pBody.applyForce(endingForce, connectedBones.get(1).pos(),true);
    }

    public void rebuild(World world, DistanceJointDef springDef)
    {
        init(world,springDef);
    }

    public void init(World world, DistanceJointDef springDef)
    {
        if (spring != null) world.destroyJoint(spring);

        length = connectedBones.get(0).pBody.getPosition().dst(connectedBones.get(1).pBody.getPosition());
        springDef.length = length;
        spring = (DistanceJoint)world.createJoint(springDef);
    }

    public void delete(World world)
    {
        parent.muscles.remove(this);
        connectedBones.clear();
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

    public void createUniqueId()
    {
        id = parent.getUniqueId();
    }

    public void setId(MuscleClass muscleClass)
    {
        id = muscleClass.id;
    }
}
