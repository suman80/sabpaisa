package in.sabpaisa.droid.sabpaisa.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.olive.upi.transport.model.Bank;
//import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by rajdeeps on 12/12/17.
 */

public class BankListAdapter extends ArrayAdapter<Bank> implements AdapterView.OnItemClickListener{

    ArrayList<Bank> arrayList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public BankListAdapter( Context context,  int resource, ArrayList<Bank> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        arrayList = objects;

    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            //holder.imageview = (ImageView) v.findViewById(R.id.product_imageView);
            holder.BankName = (TextView) v.findViewById(R.id.bankName);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        //holder.imageview.setImageResource(R.drawable.exclamationmark);
        holder.BankName.setText(arrayList.get(position).getName());



        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    static class ViewHolder {
        public ImageView imageview;
        public TextView BankName;



    }


}
