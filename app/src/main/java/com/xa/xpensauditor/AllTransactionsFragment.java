package com.xa.xpensauditor;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.xa.xpensauditor.databinding.FragmentTransactionsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.DateFormatSymbols;

public class AllTransactionsFragment extends Fragment {
    private Firebase mRootRef;
    private Firebase RefUid, RefTran, RefCat, RefCatTran;
    int pos, currentDay, currentMonth, currentYear;
    private String tagId, delCategory;
    public static final String TAG = "ALL_TRANSACTIONS";


    private ArrayList<String> CatgTF = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterTF;

    private List<Transaction> TransactionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransAdapter mAdapter1;

    private FragmentTransactionsBinding binding;
    private Context context;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos1", position);
        AllTransactionsFragment allTransactionsFragment = new AllTransactionsFragment();
        allTransactionsFragment.setArguments(bundle);
        return allTransactionsFragment;
    }

    public AllTransactionsFragment() {
        // Required empty public constructor
    }

    public void sortList(int sortType) {
        mAdapter1.sort(sortType);
    }

    public void refreshTransactionList() {
        mAdapter1.refreshTransactionList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Calendar calendar = Calendar.getInstance();
        currentDay = (calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth = (calendar.get(Calendar.MONTH) + 1);
        currentYear = (calendar.get(Calendar.YEAR));
        context = view.getContext();
        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid = auth.getUid();
        if (getArguments() != null) {
            String groupName = getArguments().getString("group_key");
            Uid = groupName;
        }
        RefUid = mRootRef.child(Uid);
        RefTran = RefUid.child("DateRange").child(currentMonth + "-" + currentYear).child("Transactions");
//        RefCatTran = RefUid.child("DateRange").child(currentMonth + "-" + currentYear).child("CatTran");
        RefCat = RefUid.child("Categories");

        arrayAdapterTF = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, CatgTF);

        RefCat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getKey().trim();
                CatgTF.add(value);
                arrayAdapterTF.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        recyclerView = binding.recyclerView;

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter1 = new TransAdapter(TransactionList);
        recyclerView.setAdapter(mAdapter1);
        prepareTransactionData();


        registerForContextMenu(recyclerView);

        mAdapter1.setOnItemClickListener(new TransAdapter.ClickListener() {
            @Override
            public void OnItemClick(int position, View v) {

//                Intent i = new Intent(getActivity(), SMSDBFetchActivity.class);
//                i.putExtra("indexPos", TransactionList.get(position).getTid());
//                startActivity(i);
            }

            @Override
            public void OnItemLongClick(int position, View v) {
                Log.i("yoyoyo", "Here: " + position);
                pos = position;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 11: {
                int show = item.getGroupId();

                tagId = TransactionList.get(show).getTid();
                delCategory = TransactionList.get(show).getCategory();


                RefTran.child(tagId).removeValue();
                RefUid.child("DateRange").child(currentMonth + "-" + currentYear).child("CatTran").child(delCategory).child(tagId).removeValue();
//                RefUid.child("UnCatTran").child(tagId).removeValue();
                mAdapter1.notifyDataSetChanged();//updated

                TransactionList.clear();
                prepareTransactionData();

            }
            break;
        }
        return super.onContextItemSelected(item);
    }

    private void prepareTransactionData() {

        RefTran.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Transaction transaction = parseTransaction(dataSnapshot);
                if(transaction != null) {
                    TransactionList.add(transaction);
                    mAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private Transaction parseTransaction(DataSnapshot dataSnapshot) {
        String amountStr = "";
        String category = "";
        String shname = "";
        String shDay = "";
        String shMonth = "";
        String shYear = "";
        String shMsg = "";
        int i = 0;
        String tid = dataSnapshot.getKey().toString().trim();
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            //Log.d("yomon",i+":"+S.getValue().toString().trim());
            Log.i(TAG, data.getKey());
            switch (data.getKey()) {
                case "Amount":
                    amountStr = data.getValue().toString().trim();
                    break;
                case "Category":
                    category = data.getValue().toString().trim();
                    break;
                case "Day":
                    shDay = data.getValue().toString().trim();
                    break;
                case "Month":
                    shMonth = data.getValue().toString().trim();
                    break;
                case "Shop Name":
                    shname = data.getValue().toString().trim();
                    break;
                case "Year":
                    shYear = data.getValue().toString().trim();
                    break;
                case "ZMessage":
                    shMsg = data.getValue().toString().trim();
                    break;
            }
            i++;
        }

        Transaction transaction = null;
        try {
            Log.i(TAG, tid);
            String monthString = new DateFormatSymbols().getMonths()[Integer.parseInt(shMonth) - 1];
            String shdate = shDay + " " + monthString.substring(0, 3).toUpperCase() + " " + shYear;

            int dateInt = Transaction.getDateInt(shYear, shMonth, shDay);
            transaction = new Transaction(tid, amountStr, category, shname, shdate, shMsg, dateInt);
        } catch (Exception e){

        }

        return transaction;
    }

    public void loadPrevMonth() {
        currentMonth -= 1;
        if (currentMonth <= 0) {
            currentMonth = 12;
            currentYear -= 1;
        }
        RefTran = RefUid.child("DateRange").child(currentMonth + "-" + currentYear).child("Transactions");
        mAdapter1.notifyDataSetChanged();

        TransactionList.clear();
        prepareTransactionData();
        Toast.makeText(context, "Loading " + currentYear + "-" + currentMonth + " data", Toast.LENGTH_SHORT).show();
    }
}

