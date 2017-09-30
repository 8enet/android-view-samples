package com.zzzmode.android.views.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * 只有上部分2个圆角的drawable
 *
 *
 * Ｏ-------Ｏ
 * |        |
 * |        |
 * ----------
 */
public class HalfRoundedDrawable extends Drawable {


    protected final RectF mRect = new RectF(),
            mBitmapRect;
    protected final BitmapShader bitmapShader;
    protected final Paint paint;
    private final Path mPath;


    public HalfRoundedDrawable(Bitmap bitmap, int width, int height, int rx, int ry) {


        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapRect = new RectF(0, 0, bitmap.getWidth() , bitmap.getHeight());

        mRect.set(0,0,width,height);

        Matrix matrix=new Matrix();
        matrix.setRectToRect(mBitmapRect,mRect, Matrix.ScaleToFit.FILL);
        bitmapShader.setLocalMatrix(matrix);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
        paint.setFilterBitmap(true);
        paint.setDither(true);


        mPath=new Path();
        mPath.moveTo(0,ry);
        mPath.quadTo(0,0,rx,0); //左上圆角
        mPath.lineTo(width-ry,0); //上
        mPath.quadTo(width,0,width,ry); //右上圆角
        mPath.lineTo(width,height); //右
        mPath.lineTo(0,height); //下
        mPath.lineTo(0,rx);  //左


        mPath.close();

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath,paint);

    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
