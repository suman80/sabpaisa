package in.sabpaisa.droid.sabpaisa.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by abc on 15-06-2017.
 */

public class CustomSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
    private Drawable thumb;
    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
        this.thumb = thumb;
    }

    @Override
    public Drawable getThumb() {
        return thumb;
    }
}