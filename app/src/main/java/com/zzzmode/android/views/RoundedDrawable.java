package com.zzzmode.android.views;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public  class RoundedDrawable extends Drawable {

        private int width;
        private int height;
        private RectF mRectF = new RectF();
        private Paint mPaint;

        public RoundedDrawable(Bitmap bm,int width,int height) {
            this.width=width;
            this.height=height;
            mRectF.set(0, 0, width, height);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setDither(true);
            BitmapShader shader = new BitmapShader(bm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            Matrix m=new Matrix();
            float scale =Math.max(width / (float) bm.getWidth(),height / (float) bm.getHeight());
            m.postScale(scale, scale);
            shader.setLocalMatrix(m);
            mPaint.setShader(shader);
        }

        @Override
        public void draw(Canvas canvas) {
            int canvasSize = Math.min(width, height);
            int circleCenter = canvasSize / 2;
            canvas.drawRoundRect(mRectF, circleCenter, circleCenter, mPaint);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRectF.set(bounds);
        }

        @Override
        public int getIntrinsicWidth() {
            return width;
        }

        @Override
        public int getIntrinsicHeight() {
            return height;
        }

        @Override
        public void setAlpha(int alpha) {
            if (mPaint.getAlpha() != alpha) {
                mPaint.setAlpha(alpha);
                invalidateSelf();
            }
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }