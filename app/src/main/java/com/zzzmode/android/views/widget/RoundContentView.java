package com.zzzmode.android.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.zzzmode.android.views.LayoutHelper;
import com.zzzmode.android.views.R;
import com.zzzmode.android.views.drawable.RoundedDrawable;


public class RoundContentView extends View {

    private static final String TAG = "RoundContentView";

    private static final String TEXT_END_SUFFIX = "...";
    private TextPaint mTextPaint;
    private Paint mPaint;
    private RectF mRectF;
    private Rect mRect;
    private int mBgColor; //背景颜色


    private int bgHeight; //背景高度
    private int imgWidth; //头像宽高
    private int imgHeight;

    private int imgWPadding = LayoutHelper.Layout1080x1920.w(3); //头像边距
    private int imgHPadding = LayoutHelper.Layout1080x1920.h(3);

    private int textRightMargin = LayoutHelper.Layout1080x1920.w(21); //文字左右边距
    private int textLeftMargin = LayoutHelper.Layout1080x1920.w(12);


    private int textSize; //字体大小
    private int textColor;
    private int maxTextWidth; //最大字体宽度

    public RoundContentView(Context context) {
        this(context, null);
    }

    public RoundContentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundContentView);

        imgWidth = a.getDimensionPixelOffset(R.styleable.RoundContentView_image_width, 0);
        imgHeight = a.getDimensionPixelOffset(R.styleable.RoundContentView_image_height,0);

        bgHeight =a.getDimensionPixelOffset(R.styleable.RoundContentView_background_height, 0);
        mBgColor=a.getColor(R.styleable.RoundContentView_background_color,Color.argb(0x99, 0x0a, 0x15, 0x22));

        textSize=a.getDimensionPixelOffset(R.styleable.RoundContentView_text_size,LayoutHelper.Layout1080x1920.w(48));
        textColor=a.getColor(R.styleable.RoundContentView_text_color,Color.WHITE);
        maxTextWidth=a.getDimensionPixelOffset(R.styleable.RoundContentView_max_width,0)- imgWidth - imgWPadding - textLeftMargin - textRightMargin;
        a.recycle();

        init();
    }

    private String text;
    private Drawable mDrawable;

    private String mInnerText;

    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // options
        //mPaint.setDither(true);
        //mPaint.setFilterBitmap(true);

        mRectF = new RectF();
        mRect = new Rect();

        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getWidth();
        final int height = getHeight();

        final int sc = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

        //draw bg
        final float bgTop = (height - bgHeight) / 2f;
        mRectF.set(0, bgTop, width, bgHeight + bgTop);
        mPaint.setColor(mBgColor);
        canvas.drawRoundRect(mRectF, mRectF.height() / 2f, mRectF.height() / 2f, mPaint);

        //image region
        mRect.set(0, 0, imgWidth, imgHeight);

        if (mDrawable != null) {
            //清除图片显示的区域，否则半透明的图片会看到底部背景
            mPaint.setXfermode(mXfermode);
            mPaint.setColor(Color.TRANSPARENT);
            final float radius = Math.min(mRect.width(), mRect.height()) / 2;
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), radius, mPaint);

            mPaint.setXfermode(null);

            mDrawable.setBounds(0, 0, imgWidth, imgHeight);
            mDrawable.draw(canvas);
        }

        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

        //text
        if (mInnerText != null) {
            final Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            final float baseline = (height - fm.bottom + fm.top) / 2f - fm.top;
            canvas.drawText(mInnerText, imgWidth + textLeftMargin, baseline, mTextPaint);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mInnerText == null) {
            setVisibility(INVISIBLE);
            return;
        } else {
            setVisibility(VISIBLE);
        }
        int width = (int) (mTextPaint.measureText(mInnerText) + textLeftMargin + textRightMargin + imgWPadding + imgWidth);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));

    }

    private String getInnerText() {

        //final CharSequence ellipsize = TextUtils.ellipsize(text, mTextPaint, maxTextWidth, TextUtils.TruncateAt.END);


        float w = mTextPaint.measureText(text);

        if (w < maxTextWidth) {
            return text;
        }
        CharSequence c = text;
        int end = c.length();
        final float suffixWidth = mTextPaint.measureText(TEXT_END_SUFFIX);
        while (w > maxTextWidth) {
            end -= 1;
            if (end > 0) {
                w = mTextPaint.measureText(c, 0, end) + suffixWidth;
            } else {
                break;
            }
        }

        return text.substring(0, end) + TEXT_END_SUFFIX;
    }


    public boolean setImageDrawable(Drawable drawable) {
        if (mDrawable != drawable) {
            mDrawable = drawable;
            invalidate();
            return true;
        }
        return false;
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            return setImageDrawable(new RoundedDrawable(bitmap, imgWidth, imgHeight));
        }
        return false;
    }

    public void setText(String text) {
        if (this.text != text) {
            this.text = text;
            mInnerText = getInnerText();
            invalidate();
        }
    }


    public void setBgColor(int color) {
        if (this.mBgColor != color) {
            this.mBgColor = color;
            invalidate();
        }
    }
}


