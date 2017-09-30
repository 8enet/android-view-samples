package com.zzzmode.android.views.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.animation.DynamicAnimation;
import android.support.animation.DynamicAnimation.OnAnimationEndListener;
import android.support.animation.DynamicAnimation.OnAnimationUpdateListener;
import android.support.animation.SpringAnimation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.zzzmode.android.views.R;

/**
 * Created by zl on 2017/9/28.
 */

public class VPFrameLayout extends FrameLayout {

  private static final String TAG = "VPFrameLayout";

  public VPFrameLayout(@NonNull Context context) {
    this(context,null);
  }

  public VPFrameLayout(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs,0);
  }

  public VPFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr,0);
  }

  public VPFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);

  }


  private ViewDragHelper viewDragHelper;
  private SpringAnimation animX,animY;

  private Point point=new Point();

  private Point mOriginPoint=new Point();

  private View mDragView;

  private SparseArray<View> mTrackViews = new SparseArray<>();

  private void init(){
    initTkViews();

    SpringAnimation prvAnimX = new SpringAnimation(mTrackViews.valueAt(0),DynamicAnimation.TRANSLATION_X,0);
    SpringAnimation prvAnimY = new SpringAnimation(mTrackViews.valueAt(0),DynamicAnimation.TRANSLATION_Y,0);

    final SpringAnimation ax = prvAnimX;
    final SpringAnimation ay =prvAnimY;

    animX = ax;
    animY = ay;

    float[] stiffs={0,750,500,350,200,50};
    float[] ratio={0,0.80f,0.70f,0.6f,0.5f,0.4f};

    for (int i = 1; i < mTrackViews.size(); i++) {
      final SpringAnimation currAnimX = new SpringAnimation(mTrackViews.valueAt(i),DynamicAnimation.TRANSLATION_X,0);
      final SpringAnimation currAnimY = new SpringAnimation(mTrackViews.valueAt(i),DynamicAnimation.TRANSLATION_Y,0);

      //currAnimX.getSpring().setStiffness(800f-i*150).setDampingRatio(1f-i*0.15f);
      //currAnimY.getSpring().setStiffness(800f-i*150).setDampingRatio(1f-i*0.15f);

      currAnimX.getSpring().setStiffness(stiffs[i]).setDampingRatio(ratio[i]);
      currAnimY.getSpring().setStiffness(stiffs[i]).setDampingRatio(ratio[i]);

      Log.e(TAG, "init --> "+i+"    "+prvAnimX+"    "+currAnimX);

      prvAnimX.addUpdateListener(new OnAnimationUpdateListener() {
        @Override
        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
          currAnimX.animateToFinalPosition(value);
        }
      });
      prvAnimY.addUpdateListener(new OnAnimationUpdateListener() {
        @Override
        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
          currAnimY.animateToFinalPosition(value);
        }
      });

      prvAnimX=currAnimX;
      prvAnimY=currAnimY;

    }

    viewDragHelper = ViewDragHelper.create(this, new Callback() {

      @Override
      public boolean tryCaptureView(View view, int i) {
        return view.getId() == R.id.view;
      }

      @Override
      public void onViewDragStateChanged(int state) {
        super.onViewDragStateChanged(state);

      }

      @Override
      public int clampViewPositionHorizontal(View child, int left, int dx) {
        return Math.max(left,0);
      }

      @Override
      public int clampViewPositionVertical(View child, int top, int dy) {
        return Math.max(top,0);
      }

      @Override
      public void onViewReleased(final View releasedChild, float xvel, float yvel) {
        super.onViewReleased(releasedChild, xvel, yvel);

        //释放时回去
        viewDragHelper.settleCapturedViewAt(mOriginPoint.x,mOriginPoint.y);
        invalidate();


      }


      @Override
      public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        super.onViewPositionChanged(changedView, left, top, dx, dy);

        point.x = left;
        point.y = top;



        animX.animateToFinalPosition(left);
        animY.animateToFinalPosition(top);


      }
    });

  }


  @Override
  public void computeScroll() {
    super.computeScroll();
    if(viewDragHelper.continueSettling(true)){
      invalidate();
    }
  }

  private boolean isMoving(){
    //return viewDragHelper != null && viewDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING;
    return true;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return viewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if(isMoving()){
      viewDragHelper.processTouchEvent(event);
      return true;
    }
    return super.onTouchEvent(event);
  }


  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    mOriginPoint.x = mDragView.getLeft();
    mOriginPoint.y = mDragView.getTop();

  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    mDragView = findViewById(R.id.view);


    OvalShape ovalShape=new OvalShape();
    ShapeDrawable drawable=new ShapeDrawable(ovalShape);
    drawable.getPaint().setColor(ContextCompat.getColor(getContext(),android.R.color.darker_gray));
    drawable.setBounds(0,0,mDragView.getLayoutParams().width,mDragView.getLayoutParams().height);
    mDragView.setBackground(drawable);

    init();

  }

  private void initTkViews(){
    int[] colors={android.R.color.holo_red_light,
        android.R.color.holo_orange_light,android.R.color.holo_green_light,
        android.R.color.holo_blue_bright,android.R.color.holo_purple};


    for (int i = 0; i < 5; i++) {
      View view=new View(getContext());

      view.setId(generateViewId());


      ViewGroup.LayoutParams layoutParams = mDragView.getLayoutParams();
      LayoutParams lp=new LayoutParams(layoutParams);


      OvalShape ovalShape=new OvalShape();
      ShapeDrawable drawable=new ShapeDrawable(ovalShape);
      drawable.getPaint().setColor(ContextCompat.getColor(getContext(),colors[i]));
      drawable.setBounds(0,0,lp.width,lp.height);

      view.setBackground(drawable);

      addViewInLayout(view,0,lp);

      if(layoutParams instanceof MarginLayoutParams){
        view.setTranslationX(((MarginLayoutParams) layoutParams).leftMargin);
        view.setTranslationY(((MarginLayoutParams) layoutParams).topMargin);
      }

      mTrackViews.put(view.getId(),view);
    }

  }

}
