package in.sabpaisa.droid.sabpaisa.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 13-06-2017.
 */

public class PrefixEditText extends AppCompatEditText {

    private String mPrefix = "+91"; // add your prefix here for example $
    private Rect mPrefixRect = new Rect(); // actual prefix size

    public PrefixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getPaint().getTextBounds(mPrefix, 0, mPrefix.length(), mPrefixRect);
        mPrefixRect.right += getPaint().measureText(" ")*3; // add some offset

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        paint.setTextSize(50);
        paint.setFakeBoldText(true);
        canvas.drawText(mPrefix, super.getCompoundPaddingLeft(), getBaseline(), paint);
    }

    @Override
    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + mPrefixRect.width();
    }
}