package in.sabpaisa.droid.sabpaisa.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.sabpaisa.droid.sabpaisa.Adapter.TransactionAdapter;
import in.sabpaisa.droid.sabpaisa.Model.TransactionDetail;
import in.sabpaisa.droid.sabpaisa.R;

/**
 * Created by abc on 17-06-2017.
 */

public class TransactionPendingFragment extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    TransactionAdapter adapter;
    ArrayList<TransactionDetail> transactionDetails;

    public TransactionPendingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_transaction, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_transaction);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),10));
        recyclerView.setLayoutManager(llm);
        transactionDetails = new ArrayList<>();
        loadArrayList();
        adapter = new TransactionAdapter(getContext(),transactionDetails);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void loadArrayList() {
        transactionDetails.add(new TransactionDetail(false,"Mr Gaurav Singh","gaurav@upi",1200,"12 May 17","12:00 IST"));
        transactionDetails.add(new TransactionDetail(false,"Mr Gaurav Singh","gaurav@upi",800,"12 May 17","12:00 IST"));
        transactionDetails.add(new TransactionDetail(false,"Mr Gaurav Singh","gaurav@upi",1000,"12 May 17","12:00 IST"));
    }
}
