package in.sabpaisa.droid.sabpaisa;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class BanksFragment  extends Fragment {
    View rootView;
    RecyclerView recyclerView;
    ArrayList<BankHistory> bankHistoryArrayList;
    BankHistoryAdapter adapter;

    public BanksFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_banks, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_pay_history);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        bankHistoryArrayList = new ArrayList<>();
        LoadBankHistory();
        adapter = new BankHistoryAdapter(getContext(), bankHistoryArrayList);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void LoadBankHistory() {
        bankHistoryArrayList.add(new BankHistory("Admission Form", 5000));
        bankHistoryArrayList.add(new BankHistory("ABC Form", 2000));
        bankHistoryArrayList.add(new BankHistory("XYZ Form", 3400));
        bankHistoryArrayList.add(new BankHistory("PQR Form", 400));
    }


}

