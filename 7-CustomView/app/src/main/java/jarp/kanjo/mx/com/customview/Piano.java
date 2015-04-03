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
        // set(int left, int top, int right, int bottom) {
        mCKey.set(0,0,whiteKeyWidth,heigth);
        mDKey.set(whiteKeyWidth,0,whiteKeyWidth*2,heigth);
        mEKey.set(whiteKeyWidth * 2,0,whiteKeyWidth*3,heigth);
        mFKey.set(whiteKeyWidth * 3,0,whiteKeyWidth*4,heigth);
        mGKey.set(whiteKeyWidth * 4,0,whiteKeyWidth*5,heigth);
        mAKey.set(whiteKeyWidth * 5,0,whiteKeyWidth*6,heigth);
        mBKey.set(whiteKeyWidth * 6,0,whiteKeyWidth*7,heigth);




        mCSharpKey.set( whiteKeyWidth - ( blackKeyWidth / 2 ) ,0 ,whiteKeyWidth +  (blackKeyWidth / 2 ) , blackKeyHeight );
        mDSharpKey.set( ( whiteKeyWidth * 2 ) + ( blackKeyWidth / 2 ) , 0 , whiteKeyWidth * 2 +  (blackKeyWidth / 2 ) , blackKeyHeight );



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


}


