package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;

public final class utils {


    public static TextureRegion createWhitePixel()
    {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
        return region;
    }

    public static Vector2 screenToWorldPosV2(Camera camera, int screenX,int screenY)
    {
        Vector3 v3 = camera.unproject(new Vector3(screenX,screenY,0));
        return new Vector2(v3.x,v3.y);
    }

    public static BodyDef createBodyDef(BodyDef.BodyType bodyType,boolean fixedRotation)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        return bodyDef;
    }
}
