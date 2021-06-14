package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class BoneClass {
    Creature parent;
    Color boneColor = Color.DARK_GRAY;
    float length;
    //region физика
    Body pBody;
    float thickness=0.08f;
    Vector2 pos(){return pBody.getPosition();}
    float angle(){return pBody.getAngle();}
    float density = 500;
    float friction = 1;
    //endregion
    public ArrayList<MuscleClass> connectedMuscles = new ArrayList<MuscleClass>();
    public ArrayList<JointClass> connectedJoints = new ArrayList<JointClass>();

    //конструктор
    public BoneClass(JointClass joint1, JointClass joint2,World world,Creature creature, BodyDef bodyDef, PolygonShape shape, FixtureDef fixture)
    {
        this.parent = creature;

        creature.bones.add(this);
        this.connectedJoints.add(joint1);
        this.connectedJoints.add(joint2);
        joint1.connectedBones.add(this);
        joint2.connectedBones.add(this);


        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //настройка коллайдера

        //физические свойства

        fixture.friction = friction;
        fixture.density = density;
        fixture.filter.categoryBits = StaticVars.BONES;
        fixture.filter.maskBits = StaticVars.STATIC_FLOOR;

        pBody = world.createBody(bodyDef);
        init(joint1,joint2,fixture,shape);
        connectBoneToJoints(world);
    }

    public void rebuild(FixtureDef fixture,PolygonShape shape)
    {
        init(connectedJoints.get(0), connectedJoints.get(1),fixture,shape);
    }

    void init(JointClass joint1, JointClass joint2, FixtureDef fixture,PolygonShape shape)
    {
        float length = joint1.pos().dst(joint2.pos());
        this.length = length;
        shape.setAsBox(length/2,thickness/2);
        fixture.shape = shape;
        Vector2 direction = joint2.pos().sub(joint1.pos());
        Vector2 position = joint1.pos().add(direction.scl(0.5f));

        for (Fixture fix: pBody.getFixtureList())
        {
            pBody.destroyFixture(fix);
        }
        pBody.createFixture(fixture).setUserData(this);
        pBody.setTransform(position, direction.angleRad());
    }

    public void connectBoneToJoints(World world)
    {
        RevoluteJointDef jointDef = new RevoluteJointDef();

        for (JointClass joint: connectedJoints) {
            jointDef.bodyA = joint.pBody;
            jointDef.bodyB = pBody;
            jointDef.localAnchorB.set(pBody.getLocalPoint(pos()));
            world.createJoint(jointDef);
        }
    }

    public void delete(World world)
    {
        world.destroyBody(pBody);
        parent.bones.remove(this);
        for(JointClass joint: connectedJoints)
        {
            joint.connectedBones.remove(this);
        }

        for(MuscleClass muscle: connectedMuscles)
        {
            muscle.connectedBones.remove(this);
        }
    }

    public void render(ShapeDrawer shapeDrawer)
    {
        if(connectedJoints.size() == 0) return;
        //shapeDrawer.filledRectangle(pos().x-0.67f, pos().y, length, thickness, angle(), boneColor, boneColor);
        shapeDrawer.line(connectedJoints.get(0).pos().x, connectedJoints.get(0).pos().y, connectedJoints.get(1).pos().x, connectedJoints.get(1).pos().y,thickness,boneColor,boneColor);
    }
}
