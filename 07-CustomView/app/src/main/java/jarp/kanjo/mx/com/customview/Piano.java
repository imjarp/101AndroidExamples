package jarp.kanjo.mx.com.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

/**
 * Created by JARP on 29/01/2015.
 */
public class Piano extends View {

    public static final String TAG = "PianoKeyboard";
    public static final int MAX_FINGERS = 5;
    public static final int WHITE_KEYS_COUNT = 7;
    public static final int BLACK_KEYS_COUNT = 5;
    public static final float BLACK_TO_WHITE_WIDTH_RATIO = 0.625f;
    public static final float BLACK_TO_WHITE_HEIGHT_RATIO = 0.54f;
    private Paint mWhiteKeyPaint, mBlackKeyPaint, mBlackKeyHitPaint, mWhiteKeyHitPaint;
    // Support up to five fingers
    private Point[] mFingerPoints = new Point[MAX_FINGERS];
    private int[] mFingerTones = new int[MAX_FINGERS];
    private SoundPool mSoundPool;
    private SparseIntArray mToneToIndexMap = new SparseIntArray();
    private Paint mCKeyPaint, mCSharpKeyPaint, mDKeyPaint,
            mDSharpKeyPaint, mEKeyPaint, mFKeyPaint,
            mFSharpKeyPaint, mGKeyPaint, mGSharpKeyPaint,
            mAKeyPaint, mASharpKeyPaint, mBKeyPaint;
    private Rect mCKey = new Rect(), mCSharpKey = new Rect(),
            mDKey = new Rect(), mDSharpKey = new Rect(),
            mEKey = new Rect(), mFKey = new Rect(),
            mFSharpKey = new Rect(), mGKey = new Rect(),
            mGSharpKey = new Rect(), mAKey = new Rect(),
            mASharpKey = new Rect(), mBKey = new Rect();
    private MotionEvent.PointerCoords mPointerCoords;

    public Piano(Context context) {
        super(context);
    }

    public Piano(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Piano(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Piano(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();

        int cappedPointerCount = pointerCount > MAX_FINGERS ? MAX_FINGERS :
                                                                                           pointerCount;

        int actionIndex = event.getActionIndex();
        int action = event.getActionMasked();
        int id = event.getPointerId(actionIndex);

        if ( (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN ) && id<MAX_FINGERS ){

            mFingerPoints[id] = new Point((int)event.getX(actionIndex),(int) event.getY(actionIndex));

        }
        else if ( (action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) && id <MAX_FINGERS ){

            mFingerPoints[id] = null;
            invalidateKey(mFingerTones[id]);
            mFingerTones[id] = -1;
        }

        for (int i  = 0 ; i < cappedPointerCount ; i++){
            int index = event.findPointerIndex(i);
            if(mFingerPoints[i]!=null && index != -1){
                mFingerPoints[i].set( (int)event.getX(index),(int) event.getY(index) );
                int tone = getToneForPoint(mFingerPoints[i]);
                if(tone != mFingerTones[i] && tone != -1){
                    invalidateKey(mFingerTones[i]);
                    mFingerTones[i]=tone;
                    invalidateKey(mFingerTones[i]);
                    if( ! isKeyDown( i )){
                        int poolIndex = mToneToIndexMap.get( mFingerTones[i] );
                        event.getPointerCoords(index,mPointerCoords );
                        float volume = mPointerCoords.getAxisValue(MotionEvent.AXIS_PRESSURE);
                        mSoundPool.play(poolIndex,volume,volume,0,0,1f);
                    }
                }
            }
        }

        updatePaints();
        return  true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPointerCoords = new MotionEvent.PointerCoords();
        Arrays.fill(mFingerPoints, null);
        Arrays.fill(mFingerTones, -1);
        loadKeySamples(getContext());
        setupPaints();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseKeySamples();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //Calculate the sizes for the keys
        int width = getWidth();
        int heigth = getHeight();

        int whiteKeyWidth = width / WHITE_KEYS_COUNT;
        int blackKeyWidth = (int) ( whiteKeyWidth * BLACK_TO_WHITE_WIDTH_RATIO  );
        int blackKeyHeight = (int) ( heigth * BLACK_TO_WHITE_HEIGHT_RATIO );

        //Define rectangles
        // set(int left, int top, int right, int bottom)
        mCKey.set(0,0,whiteKeyWidth,heigth);
        mDKey.set(whiteKeyWidth,0,whiteKeyWidth*2,heigth);
        mEKey.set(whiteKeyWidth * 2,0,whiteKeyWidth*3,heigth);
        mFKey.set(whiteKeyWidth * 3,0,whiteKeyWidth*4,heigth);
        mGKey.set(whiteKeyWidth * 4,0,whiteKeyWidth*5,heigth);
        mAKey.set(whiteKeyWidth * 5,0,whiteKeyWidth*6,heigth);
        mBKey.set(whiteKeyWidth * 6,0,whiteKeyWidth*7,heigth);


        mCSharpKey.set( whiteKeyWidth - ( blackKeyWidth / 2 ) ,0 ,whiteKeyWidth +  (blackKeyWidth / 2 ) , blackKeyHeight );
        mDSharpKey.set( whiteKeyWidth *2 - ( blackKeyWidth / 2 ),0,whiteKeyWidth *2 + ( blackKeyWidth / 2 ) ,blackKeyHeight);
        mFSharpKey.set( whiteKeyWidth *4 - ( blackKeyWidth / 2 ),0,whiteKeyWidth *4 + ( blackKeyWidth / 2 ) ,blackKeyHeight);
        mGSharpKey.set( whiteKeyWidth *5 - ( blackKeyWidth / 2 ),0,whiteKeyWidth *5 + ( blackKeyWidth / 2 ) ,blackKeyHeight);
        mASharpKey.set( whiteKeyWidth *6 - ( blackKeyWidth / 2 ),0,whiteKeyWidth *6 + ( blackKeyWidth / 2 ) ,blackKeyHeight);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mCKey,mCKeyPaint);
        canvas.drawRect(mDKey,mDKeyPaint);
        canvas.drawRect(mEKey,mEKeyPaint);
        canvas.drawRect(mFKey,mFKeyPaint);
        canvas.drawRect(mGKey,mGKeyPaint);
        canvas.drawRect(mAKey,mAKeyPaint);
        canvas.drawRect(mBKey,mBKeyPaint);

        canvas.drawRect(mCSharpKey,mCSharpKeyPaint);
        canvas.drawRect(mDSharpKey,mDSharpKeyPaint);
        canvas.drawRect(mFSharpKey,mFSharpKeyPaint);
        canvas.drawRect(mGSharpKey,mGSharpKeyPaint);
        canvas.drawRect(mASharpKey,mASharpKeyPaint);

    }

    private boolean isKeyDown(int finger) {

        int key = getToneForPoint(mFingerPoints[finger]);

        for ( int i = 0 ; i< mFingerPoints.length; i++   ){
            if(  i!= finger){
                Point fingerPoint = mFingerPoints[i];
                if(fingerPoint != null){
                    int otherKey = getToneForPoint(fingerPoint);
                    if(otherKey ==key) {
                        return true;
                    }
                }
            }
        }
        return  false;

    }

    private void invalidateKey(int tone) {

        switch (tone){
            case R.raw.c1:
                invalidate(mCKey);
                break;
            case R.raw.c1s:
                invalidate(mCSharpKey);
                break;
            case R.raw.d1:
                invalidate(mDKey);
                break;
            case R.raw.d1s:
                invalidate(mDSharpKey);
                break;
            case R.raw.e1:
                invalidate(mEKey);
                break;
            case R.raw.f1:
                invalidate(mFKey);
                break;
            case R.raw.f1s:
                invalidate(mFSharpKey);
                break;
            case R.raw.g1:
                invalidate(mGKey);
                break;
            case R.raw.g1s:
                invalidate(mGSharpKey);
                break;
            case R.raw.a1:
                invalidate(mAKey);
                break;
            case R.raw.a1s:
                invalidate(mASharpKey);
                break;
            case R.raw.b1:
                invalidate(mBKey);
                break;
        }
    }

    private void setupPaints() {
        mWhiteKeyPaint = new Paint();
        mWhiteKeyPaint.setStyle(Paint.Style.STROKE);
        mWhiteKeyPaint.setColor(Color.BLACK);
        mWhiteKeyPaint.setStrokeWidth(3);
        mWhiteKeyPaint.setAntiAlias(true);
        mCKeyPaint = mWhiteKeyPaint;
        mDKeyPaint = mWhiteKeyPaint;
        mEKeyPaint = mWhiteKeyPaint;
        mFKeyPaint = mWhiteKeyPaint;
        mGKeyPaint = mWhiteKeyPaint;
        mAKeyPaint = mWhiteKeyPaint;
        mBKeyPaint = mWhiteKeyPaint;

        mWhiteKeyHitPaint = new Paint(mWhiteKeyPaint);
        mWhiteKeyHitPaint.setColor(Color.LTGRAY);
        mWhiteKeyHitPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBlackKeyPaint = new Paint();
        mBlackKeyPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBlackKeyPaint.setColor(Color.BLACK);
        mBlackKeyPaint.setAntiAlias(true);
        mCSharpKeyPaint = mBlackKeyPaint;
        mDSharpKeyPaint = mBlackKeyPaint;
        mFSharpKeyPaint = mBlackKeyPaint;
        mGSharpKeyPaint = mBlackKeyPaint;
        mASharpKeyPaint = mBlackKeyPaint;

        mBlackKeyHitPaint = new Paint(mBlackKeyPaint);
        mBlackKeyHitPaint.setColor(Color.DKGRAY);
    }

    private void loadKeySamples(Context context) {


        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mToneToIndexMap.put(R.raw.c1, mSoundPool.load(context, R.raw.c1, 1));
        mToneToIndexMap.put(R.raw.c1s, mSoundPool.load(context, R.raw.c1s, 1));
        mToneToIndexMap.put(R.raw.d1, mSoundPool.load(context, R.raw.d1, 1));
        mToneToIndexMap.put(R.raw.d1s, mSoundPool.load(context, R.raw.d1s, 1));
        mToneToIndexMap.put(R.raw.e1, mSoundPool.load(context, R.raw.e1, 1));
        mToneToIndexMap.put(R.raw.f1, mSoundPool.load(context, R.raw.f1, 1));
        mToneToIndexMap.put(R.raw.f1s, mSoundPool.load(context, R.raw.f1s, 1));
        mToneToIndexMap.put(R.raw.g1, mSoundPool.load(context, R.raw.g1, 1));
        mToneToIndexMap.put(R.raw.g1s, mSoundPool.load(context, R.raw.g1s, 1));
        mToneToIndexMap.put(R.raw.a1, mSoundPool.load(context, R.raw.a1, 1));
        mToneToIndexMap.put(R.raw.a1s, mSoundPool.load(context, R.raw.a1s, 1));
        mToneToIndexMap.put(R.raw.b1, mSoundPool.load(context, R.raw.b1, 1));
    }

    private void releaseKeySamples(){

        mToneToIndexMap.clear();
        mSoundPool.release();

    }

    private void updatePaints() {

        mCKeyPaint = mWhiteKeyPaint;
        mDKeyPaint = mWhiteKeyPaint;
        mEKeyPaint = mWhiteKeyPaint;
        mFKeyPaint = mWhiteKeyPaint;
        mGKeyPaint = mWhiteKeyPaint;
        mAKeyPaint = mWhiteKeyPaint;
        mBKeyPaint = mWhiteKeyPaint;


        mCSharpKeyPaint= mBlackKeyPaint;
        mDSharpKeyPaint= mBlackKeyPaint;
        mFSharpKeyPaint= mBlackKeyPaint;
        mGSharpKeyPaint= mBlackKeyPaint;
        mASharpKeyPaint= mBlackKeyPaint;



        for (Point fingerPoint : mFingerPoints){

            if(fingerPoint != null){
                if(mCSharpKey.contains(fingerPoint.x,fingerPoint.y)){
                    mCSharpKeyPaint = mBlackKeyHitPaint;
                }else if(mDSharpKey.contains(fingerPoint.x,fingerPoint.y)){
                    mDSharpKeyPaint = mBlackKeyHitPaint;
                }else if(mFSharpKey.contains(fingerPoint.x,fingerPoint.y)){
                    mFSharpKeyPaint = mBlackKeyHitPaint;
                }else if(mGSharpKey.contains(fingerPoint.x,fingerPoint.y)){
                    mGSharpKeyPaint = mBlackKeyHitPaint;
                }else if(mASharpKey.contains(fingerPoint.x,fingerPoint.y)){
                    mASharpKeyPaint = mBlackKeyHitPaint;
                }else if(mCKey.contains(fingerPoint.x,fingerPoint.y)){
                    mCKeyPaint = mWhiteKeyHitPaint;
                }else if(mDKey.contains(fingerPoint.x,fingerPoint.y)){
                    mDKeyPaint = mWhiteKeyHitPaint;
                }else if(mEKey.contains(fingerPoint.x,fingerPoint.y)){
                    mEKeyPaint = mWhiteKeyHitPaint;
                }else if(mFKey.contains(fingerPoint.x,fingerPoint.y)){
                    mFKeyPaint = mWhiteKeyHitPaint;
                }else if(mGKey.contains(fingerPoint.x,fingerPoint.y)){
                    mGKeyPaint = mWhiteKeyHitPaint;
                }else if(mAKey.contains(fingerPoint.x,fingerPoint.y)){
                    mAKeyPaint = mWhiteKeyHitPaint;
                }else if(mBKey.contains(fingerPoint.x,fingerPoint.y)){
                    mBKeyPaint = mWhiteKeyHitPaint;
                }
            }
        }
    }



    private int getToneForPoint(Point point) {

        if(mCSharpKey.contains(point.x,point.y))
            return R.raw.c1s;
        if(mDSharpKey.contains(point.x,point.y))
            return R.raw.d1s;
        if(mFSharpKey.contains(point.x,point.y))
            return R.raw.f1s;
        if(mGSharpKey.contains(point.x,point.y))
            return R.raw.g1s;
        if(mASharpKey.contains(point.x,point.y))
            return R.raw.a1s;


        if(mCKey.contains(point.x,point.y))
            return R.raw.c1;
        if(mDKey.contains(point.x,point.y))
            return R.raw.d1;
        if(mEKey.contains(point.x,point.y))
            return R.raw.e1;
        if(mFKey.contains(point.x,point.y))
            return R.raw.f1;
        if(mGKey.contains(point.x,point.y))
            return R.raw.g1;
        if(mAKey.contains(point.x,point.y))
            return R.raw.a1;
        if(mBKey.contains(point.x,point.y))
            return R.raw.b1;

        return -1;
    }

}


