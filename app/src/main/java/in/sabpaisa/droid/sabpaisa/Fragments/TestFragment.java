package in.sabpaisa.droid.sabpaisa.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 19-06-2017.
 */

public class TestFragment extends Fragment {
    public TestFragment(){

    }

    public static TestFragment newInstance(int i) {

        Bundle args = new Bundle();
        args.putInt("PageNumber",i+1);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString("Section ", getArguments().getInt("PageNumber")));
        return rootView;
    }

    private String getString(String s, int pageNumber) {
        return s+" "+pageNumber;
    }
}
