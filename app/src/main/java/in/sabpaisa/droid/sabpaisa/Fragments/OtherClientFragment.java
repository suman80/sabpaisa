package in.sabpaisa.droid.sabpaisa.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sabpaisa.droid.sabpaisa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherClientFragment extends Fragment {


    public OtherClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_client, container, false);
    }

}
