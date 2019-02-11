package in.sabpaisa.droid.sabpaisa.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.sabpaisa.droid.sabpaisa.R;

public class SkipGroupFragment extends Fragment {



    //Values get from SkipClient details screen
    String clientName,clientLogoPath,clientImagePath,state;

    public SkipGroupFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clientName = getArguments().getString("clientName");
        clientLogoPath = getArguments().getString("clientLogoPath");
        clientImagePath = getArguments().getString("clientImagePath");
        state = getArguments().getString("state");

        Log.d("SkipGroupFrag","Recieved_Val_"+clientName+" "+clientLogoPath+" "+clientImagePath+" "+state);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_skip_group, container, false);
    }

}
