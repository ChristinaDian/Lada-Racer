package com.example.simpleracer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable{

    public static int MOVEMENT = 5;
    private final Bitmap bitmapExplosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
    int score=0;
    int lives=3;
    float ladaLife=1;
    Player player;
    ArrayList<RoadDot> dots = new ArrayList<>();
    ArrayList<RoadLine> lines = new ArrayList<>();
    ArrayList<Competitor> competitors = new ArrayList<>();
    PowerUp wrench;
    Random random;

    boolean isAlive=true;

    Paint paint;
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    Thread gameThread;

    int screenSizeX;
    int screenSizeY;

    public GameView(Context context, int screenSizeX, int screenSizeY) {
        super(context);
        this.screenSizeX=screenSizeX;
        this.screenSizeY=screenSizeY;

        player=new Player(context, screenSizeX,screenSizeY);
        player.speed=10;

        paint=new Paint();
        surfaceHolder=getHolder();

        for (int i=0; i<30000; i++)
        {
            dots.add(new RoadDot(screenSizeX,screenSizeY));
        }

            Bitmap lineBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.road_line);

            int lineAndSpacing = lineBitmap.getHeight()*2;
            int numberLines=screenSizeY/ lineAndSpacing;
            int laneWidth = screenSizeX / 3;

        for(int i=0; i<=numberLines; i++) {
            for(int j=1; j< 3; j++) {

                RoadLine line = new RoadLine(getContext(), screenSizeY);
                line.x = laneWidth * j;
                line.y = i * lineAndSpacing;
                lines.add(line);
            }

        }

        gameThread=new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while(isAlive){
            update();
            draw();
            refreshRate();
            score++;
            ladaLife+=0.001;
        }
    }


    private void draw() {
        if (surfaceHolder.getSurface().isValid()){
            canvas=surfaceHolder.lockCanvas();

            canvas.drawColor(Color.GRAY);
            paint.setColor(Color.BLACK);

            for(RoadDot dot: dots){
                canvas.drawPoint(dot.x,dot.y, paint);
            }
            for (RoadLine line:lines) {
                canvas.drawBitmap(line.bitmap, line.x, line.y, paint);
            }
            if (wrench!=null){
                canvas.drawBitmap(wrench.bitmap,wrench.x, wrench.y, paint);

            }
            for(Competitor comp: competitors) {
                canvas.drawBitmap(comp.bitmap,comp.x,comp.y, paint);
                if (comp.crashed){
                    canvas.drawBitmap(bitmapExplosion, comp.x+2, comp.y, paint);
                }
            }
            
            paint.setColor(Color.WHITE);
            paint.setTextSize(60);

            canvas.drawText("Score: "+score, 35, 70, paint);

            paint.setColor(Color.YELLOW);
            if (((screenSizeX/3+30)*ladaLife)<(screenSizeX-screenSizeX/3-5)) {
                canvas.drawRect(new Rect((int) ((screenSizeX / 3 + 30) * ladaLife), 30, screenSizeX - screenSizeX / 3 - 5, 70), paint);
            }else{
                lives--;
                if(lives==0){
                    isAlive=false;
                    player.crashed=true;
                }
                ladaLife=1;
             }

            for (int i=1; i<=lives; i++) {
                canvas.drawBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.heart),
                        screenSizeX-30-80*i, 15, paint);
            }
            canvas.drawBitmap(player.bitmap, player.x,player.y, paint);
            if (player.crashed){
                canvas.drawBitmap(bitmapExplosion,player.x+ 2, player.y,paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void refreshRate() {
        try {
            gameThread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update() {

        if (score%200==0){
            player.speed++;
            MOVEMENT=player.speed/2;
        }
        if (score% 1000==0){
            competitors.add(new Competitor(getContext(), screenSizeX, screenSizeY, player.speed));
        }
        if (wrench!=null){
             wrench.update(player.speed);
             if (Rect.intersects(player.collisionDetection,wrench.collisionDetection))
             {
                 ladaLife+=0.3;
                 wrench.y=screenSizeY+1;
             }

            if(wrench.y>screenSizeY){
                wrench=null;
            }
        }
      // if(score>1&&score%1000==0){
       //    wrench= new PowerUp(getContext(), screenSizeX, screenSizeY, player.speed);
      //  }
        for (RoadLine line: lines) {
            line.update(player.speed);
        }
        for(RoadDot dot :dots){
            dot.update(player.speed);
        }
        for (Competitor comp: competitors) {
            comp.update(player.speed);
            if((!comp.playerCrashed)&&Rect.intersects(comp.collisionDetector, player.collisionDetection)) {
                lives--;
                if(lives==0) {
                    isAlive = false;
                    player.crashed = true;
                }
                comp.crashed();
                comp.playerCrashed=true;
            }
        }
        for (Competitor comp: competitors){
            for (Competitor secondComp: competitors){
                if(comp==secondComp){
                    continue;
                }
                if (Rect.intersects(comp.collisionDetector, secondComp.collisionDetector)){
                    comp.crashed();
                    secondComp.crashed();
                }
            }
        }
    }

    VelocityTracker velocityTracker;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionIndex();
        int pointer = event.getPointerId(action);

         switch(event.getActionMasked()){
             case MotionEvent.ACTION_DOWN:
                 if(velocityTracker ==null){
                     velocityTracker=velocityTracker.obtain();
                 }else{
                     velocityTracker.clear();
                 }
                velocityTracker.addMovement(event);
                 break;

             case MotionEvent.ACTION_MOVE:

                 velocityTracker.addMovement(event);
                 velocityTracker.computeCurrentVelocity(1000);
                 // Log.wtf("velocity ", velocityTracker.getXVelocity()+"X");
                 // Log.wtf("velocity ", velocityTracker.getYVelocity()+"Y");

                 int xMovement=0;
                 int yMovement=0;

                 if (velocityTracker.getXVelocity()>0){
                     xMovement= MOVEMENT;
                 }else if(velocityTracker.getXVelocity()<0){
                    xMovement=-MOVEMENT;
                 }

                /* if(velocityTracker.getYVelocity()>0){
                     yMovement=MOVEMENT;
                 }else if (velocityTracker.getYVelocity()<0){
                     yMovement=-MOVEMENT;
                 }
*/
                 player.update(xMovement, yMovement);
                 break;
         }

        return true;
    }
}
