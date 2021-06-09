package com.watermelon0guy.ai_game;

import com.badlogic.gdx.graphics.Color;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class BackgroundGrid {
    int width = 150;
    int height = 50;
    float lineWidth = 0.03f;
    Color color = Color.LIGHT_GRAY;

    public BackgroundGrid(int width,int height, float lineWidth,Color color)
    {
        this.width = width;
        this.height = height;
        this.lineWidth = lineWidth;
        this.color = color;
    }
    public BackgroundGrid() {}

    public void render(ShapeDrawer shapeDrawer)
    {
        for(int x = 0-width/2; x <= width/2; x++) {
            shapeDrawer.line(x,0-height/2,x,height/2, color,lineWidth);
        }
        for(int y = 0-height/2; y <= height/2; y++) {
            shapeDrawer.line(0-width/2,y,width/2,y, color,lineWidth);
        }
    }
}
