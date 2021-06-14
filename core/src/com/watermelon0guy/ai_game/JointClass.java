package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class JointClass {
    public Creature parent;
    Color jointColor = new Color(1f/255f * 66f,1f/255f * 170f,1f/255f * 255f,1);
    float radius = 0.1f;
    //физика
    public Body pBody;
    public Vector2 pos(){return pBody.getPosition();}
    float angle(){return pBody.getAngle();}
    float density = 1250;
    float friction = 1;
    public ArrayList<BoneClass> connectedBones = new ArrayList<BoneClass>();

    //конструктор
    public JointClass(Vector2 pos,World world, Creature creature, BodyDef bodyDef, CircleShape shape, FixtureDef fixture)
    {
        this.parent = creature;
        creature.joints.add(this);
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        //настройка коллайдера

        //физ свойства

        fixture.shape = shape;
        fixture.friction = friction;
        fixture.density = density;
        fixture.filter.categoryBits = StaticVars.JOINTS;
        fixture.filter.maskBits = StaticVars.STATIC_FLOOR;

        pBody = world.createBody(bodyDef);
        pBody.createFixture(fixture).setUserData(this);
        pBody.setTransform(pos,0);
    }

    public void delete(World world) {
        world.destroyBody(pBody);
        parent.joints.remove(this);
        for ( BoneClass bone: connectedBones)
        {
            bone.connectedJoints.remove(this);
            bone.delete(world);
        }
    }

    public void render(ShapeDrawer shapeDrawer)
    {
        if(pBody == null) return;
        shapeDrawer.filledCircle(pBody.getPosition().x, pBody.getPosition().y,0.1f, jointColor);
    }
}
