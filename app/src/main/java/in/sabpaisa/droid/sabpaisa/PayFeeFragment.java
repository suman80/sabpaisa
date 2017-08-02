package in.sabpaisa.droid.sabpaisa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by SabPaisa on 27-07-2017.
 */

public  class PayFeeFragment extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    PayFeeAdapter adapter;
    ArrayList<String> formNames;

    public PayFeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_payfee, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_forms);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        formNames = new ArrayList<>();
        LoadFormNames();
        adapter = new PayFeeAdapter(getParentFragment().getContext(), formNames);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void LoadFormNames() {
        formNames.add("Admission Form");
        formNames.add("Sports Fee Form");
        formNames.add("Medical Leave Form");
        formNames.add("Sanctioned Leave Form");
        formNames.add("Late Fee Form");
        formNames.add("First Installment Fee Form");
        formNames.add("Service Form");
    }
}
