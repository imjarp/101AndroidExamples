package mx.com.kanjo.jarp.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * custom view class.
 */
public class RotateView extends View {

    private Paint mPaint;
    private static final double MAX_ANGLE = 1e-1;
    private float mRotation;
    private Float mPreviousAngle;

    public RotateView(Context context) {
        super(context);
    }

    public RotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = (int) ( width > height ?
                                  height * 0.666f : width * 0.666f  ) / 2 ;

        canvas.drawCircle(width/2, height /2,radius,mPaint);
        canvas.save();
        canvas.rotate(mRotation,width/2, height/2);
        canvas.drawLine(width / 2 , height * 0.1f,
                                width / 2 , height *0.9f , mPaint);

        canvas.restore();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPreviousAngle = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Two fingers
        if(event.getPointerCount() == 2){

            float currentAngle = (float) angle(event);
            if(mPreviousAngle != null){
                mRotation += Math.toDegrees( clamp(mPreviousAngle-currentAngle,-MAX_ANGLE,MAX_ANGLE)  );
                invalidate();
            }
            mPreviousAngle = currentAngle;
        }
        else{

            mPreviousAngle = null;
        }

        return true;
    }

    private double clamp(double value, double min, double maxAngle) {

        if(value<min){
            return min;
        }
        if(value>maxAngle) {
            return maxAngle;
        }

        return value;

    }

    private static double angle(MotionEvent event) {

        double deltaX = event.getX(0) - event.getX(1);
        double deltaY = event.getY(0) - event.getY(1);

        return  Math.atan2(deltaX,deltaY);
    }


}
