package in.sabpaisa.droid.sabpaisa.Util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 14-06-2017.
 */

public class CustomSliderView extends BaseSliderView {
    Context context;
    public CustomSliderView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(this.getContext()).inflate(R.layout.render_type_text, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        LinearLayout frame = (LinearLayout) v.findViewById(R.id.description_layout);
        frame.setBackgroundColor(context.getResources().getColor(R.color.main_screen_bottom_color));

//      if you need description
//      description.setText(this.getDescription());

        this.bindEventAndShow(v, target);
        return v;
    }
}
