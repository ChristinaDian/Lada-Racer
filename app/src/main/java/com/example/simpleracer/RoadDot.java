package com.example.simpleracer;

import java.util.Random;

public class RoadDot {
    int x;
    int y;
    int maxX;
    int maxY;


    Random random;

    public RoadDot(int screenSizeX, int screenSizeY)
    {
        maxX=screenSizeX;
        maxY=screenSizeY;
        random=new Random();
        x=random.nextInt(maxX);
        y=random.nextInt(maxY);

    }

    public void update(int playerSpeed){
        y+= playerSpeed;
        if (y>maxY){
            y= random.nextInt(10)* -1;
            x= random.nextInt(maxX);
        }
    }
}
