package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Floor {
    Body pBody;
    float x;
    float y;
    float width;
    float height;
    FixtureDef fixture = new FixtureDef();
    public Floor(float xCord, float yCord, float width, float height, World world)
    {
        x = xCord;
        y = yCord;
        this.width = width;
        this.height = height;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(xCord,yCord);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2,height/2);
        fixture.shape = shape;
        fixture.filter.categoryBits = StaticVars.STATIC_FLOOR;
        fixture.filter.maskBits = (short)(StaticVars.BONES | StaticVars.JOINTS);
        pBody = world.createBody(bodyDef);
        pBody.createFixture(fixture).setUserData(this);
        shape.dispose();
    }

    public void render(ShapeDrawer shapeDrawer)
    {
        shapeDrawer.filledRectangle(x-width/2,y-height/2,width,height, Color.DARK_GRAY);
    }
}
