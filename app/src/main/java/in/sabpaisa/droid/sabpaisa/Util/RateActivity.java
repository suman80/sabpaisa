package in.sabpaisa.droid.sabpaisa.Util;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import in.sabpaisa.droid.sabpaisa.R;

import static in.sabpaisa.droid.sabpaisa.R.id.background;

public class RateActivity extends AppCompatActivity {
    RatingBar ratingbar1;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setFullScreen(this);
        setContentView(R.layout.activity_rate);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SmileRating smileRating = (SmileRating) findViewById(R.id.ratingBar1);

        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        Toast.makeText(getApplication(),"Oops Better Luck next time", Toast.LENGTH_LONG).show();

                       // Log.i(TAG, "Bad");
                        break;
                    case SmileRating.GOOD:
                        Toast.makeText(getApplication(),"Thankyou for Liking us", Toast.LENGTH_LONG).show();

                       // Log.i(TAG, "Good");
                        break;
                    case SmileRating.GREAT:
                        Toast.makeText(getApplication(),"Thankyou for Liking us", Toast.LENGTH_LONG).show();


                        // Log.i(TAG, "Great");
                        break;
                    case SmileRating.OKAY:
                        Toast.makeText(getApplication(),"Thankyou for Liking us", Toast.LENGTH_LONG).show();

                       // Log.i(TAG, "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Toast.makeText(getApplication(),"Oops Better Luck next time ", Toast.LENGTH_LONG).show();

                       // Log.i(TAG, "Terrible");
                        break;
                }
            }
        });

       // ratingbar1=() findViewById(R.id.ratingBar1);
       // ratingbar1.setBackgroundColor(getResources().getColor(R.color.black));
       /* button=(Button) findViewById(R.id.button1);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting the rating and displaying it on the toast
                String rating=String.valueOf(ratingbar1.getRating());
                Toast.makeText(getApplication(),"Thankyou for giving us: " + rating, Toast.LENGTH_LONG).show();
            }

        });*/
    }
}
