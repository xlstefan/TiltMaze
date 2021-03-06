package com.azaric.tiltmaze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Stefan on 1/15/17.
 */
public class MyImageView extends ImageView{


    Controller controller;
    Polygon model;
    Paint paintRed=new Paint();
    Paint paintMagenta =new Paint();
    Paint paintGreen=new Paint();
    Paint paintWhite=new Paint();
    Paint paintBlack =new Paint();


    Paint paint=new Paint();
    Paint paintBlue=new Paint();

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

    public MyImageView(Context context, AttributeSet attrs) { super(context, attrs); }

    public MyImageView(Context context) { super(context); }

    public Controller getController() { return controller; }

    public void setController(GameActivity activity, Controller controller) { this.controller = controller; }

    public Polygon getModel() { return model; }

    public void setModel(Polygon model) { this.model = model; }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(model!=null) {
            model.setSize(h,w);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Log.d("draw", "draw");
        paintRed.setColor(Color.RED);
        paintGreen.setColor(Color.GREEN);
        paintWhite.setColor(Color.WHITE);
        paintMagenta.setColor(Color.MAGENTA);
        paintBlack.setColor(Color.BLACK);

        if (model != null) {
            float width = (float) model.getWidth();
            float height = (float) model.getHeight();

            for (Wall wall : model.getWalls())
                canvas.drawRect((float) wall.getxS() * height, (float) wall.getyS() * height,
                        (float) wall.getxE() * height, (float) wall.getyE() * height, paintBlack);

            for (Point h : model.getHoles()) {
                canvas.drawCircle((float) h.getX() * height, (float) h.getY() * height, (float) model.getR() * height, paintRed);
            }
            if (model.getGoal() != null)
                canvas.drawCircle((float) model.getGoal().getX() * height, (float) model.getGoal().getY() * height,
                        (float) model.getR() * height, paintGreen);
            if (model.getBall() != null)
                canvas.drawCircle((float) model.getBall().getX() * height, (float) model.getBall().getY() * height,
                        (float) model.getrBall() * height, paintWhite);

            Paint textPaintWhite = new Paint();
            textPaintWhite.setColor(Color.WHITE);
            textPaintWhite.setTextSize(50);
            if(!model.isGameOver())
                canvas.drawText(controller.getTime(),(float)0.5*width,(float)0.05*height,textPaintWhite);
        }
    }


}
