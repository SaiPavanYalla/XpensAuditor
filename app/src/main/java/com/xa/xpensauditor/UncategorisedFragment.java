package com.xa.xpensauditor;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.text.DateFormatSymbols;


public class UncategorisedFragment extends Fragment {

    private String tagId, catChangeTo, tagDate;
    private String [] tagDateSplit;
    private Firebase mRootRef;
    private Firebase RefUid,RefTran,RefCat;
    int pos, intSum,mm,yyyy;
    private TextView textView;
    private ArrayList<String> Catg=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView changeCat;
    private Context context;

    private List<Transaction> TransactionListUF = new ArrayList<>();
    private RecyclerView recyclerViewUF;

    private TransactionAdapter mAdapterUF;


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos2", position);
        UncategorisedFragment uncategorisedFragment = new UncategorisedFragment();
        uncategorisedFragment.setArguments(bundle);
        return uncategorisedFragment;
    }
    public UncategorisedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uncategorised, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRootRef=new Firebase("https://xpensauditor-g11-default-rtdb.firebaseio.com/");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefTran = RefUid.child("UnCatTran");
        RefCat=RefUid.child("Categories");

        arrayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,Catg);

        RefCat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value= dataSnapshot.getKey().trim();
                Catg.add(value);
                arrayAdapter.notifyDataSetChanged();
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


        recyclerViewUF = (RecyclerView) view.findViewById(R.id.rv_uncat);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewUF.setLayoutManager(mLayoutManager);
        recyclerViewUF.setItemAnimator(new DefaultItemAnimator());
        mAdapterUF = new TransactionAdapter(TransactionListUF);
        recyclerViewUF.setAdapter(mAdapterUF);
        prepareTransactionData();
        registerForContextMenu(recyclerViewUF);


        mAdapterUF.setOnItemClickListener(new TransactionAdapter.ClickListener() {
            @Override
            public void OnItemClick(int position, View v) {

                Intent i = new Intent(getActivity(),SMSTransacShowActivity.class);
                i.putExtra("indexPos",TransactionListUF.get(position).getTid());
                startActivity(i);
            }

            @Override
            public void OnItemLongClick(int position, View v) {
                Log.i("yoyoyo","yoyoyooyoyoyoyo");
                pos=position;
            }
        });




    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case 21:{
                int show = item.getGroupId();
                tagId=TransactionListUF.get(show).getTid();
                tagDate = TransactionListUF.get(show).getT_date();
                tagDateSplit = tagDate.split(" ");

                switch (tagDateSplit[1]){
                    case "JAN": {
                        mm = 1;
                        break;
                    }
                    case "FEB": {
                        mm = 2;
                        break;
                    }
                    case "MAR": {
                        mm = 3;
                        break;
                    }
                    case "APR": {
                        mm = 4;
                        break;
                    }
                    case "MAY": {
                        mm = 5;
                        break;
                    }
                    case "JUN": {
                        mm = 6;
                        break;
                    }
                    case "JUL": {
                        mm = 7;
                        break;
                    }
                    case "AUG": {
                        mm = 8;
                        break;
                    }
                    case "SEP": {
                        mm = 9;
                        break;
                    }
                    case "OCT": {
                        mm = 10;
                        break;
                    }
                    case "NOV": {
                        mm = 11;
                        break;
                    }
                    case "DEC": {
                        mm = 12;
                        break;
                    }
                }
                yyyy=Integer.parseInt(tagDateSplit[2]);
                Log.d("yocheck",mm+"/"+yyyy+tagId+tagDateSplit[1]);
                RefUid.child("DateRange").child(mm+"-"+yyyy).child("Transactions").child(tagId).removeValue();


                RefTran.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String checkID = dataSnapshot.getKey().toString().trim();
                        if(tagId.equals(checkID)) {
                            dataSnapshot.getRef().removeValue(new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                    //Toast.makeText(getActivity(), tagId + "-Deleted", Toast.LENGTH_LONG).show();
                                    TransactionListUF.clear();
                                    prepareTransactionData();
                                    mAdapterUF.notifyDataSetChanged();
                                }
                            });
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
            }break;
            case 22:{
                Toast.makeText(getActivity(), "To be updated in later versions", Toast.LENGTH_SHORT).show();
            }break;

        }
        return super.onContextItemSelected(item);
    }


    private void prepareTransactionData() {
        RefTran.addChildEventListener(new ChildEventListener() {
            String amount,cat,shname,shDay,shMonth,shYear,shMsg, sharedWith;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int i=0;

                String tid = dataSnapshot.getKey().toString().trim();
                for (DataSnapshot S:dataSnapshot.getChildren()) {

                    switch(i)
                    {
                        case 0:
                            amount=S.getValue().toString().trim();
                            break;
                        case 1:
                            cat=S.getValue().toString().trim();
                            break;
                        case 2:
                            shDay=S.getValue().toString().trim();
                            break;
                        case 3:
                            shMonth=S.getValue().toString().trim();
                            break;
                        case 4:
                            String label = (S.getChildrenCount() > 1) ? "Shared With: " : "";
                            sharedWith=S.getValue().toString().replace("[", label).replace("]", "");
                            break;
                        case 5:
                            shname=S.getValue().toString().trim();
                            break;
                        case 6:
                            shYear=S.getValue().toString().trim();
                            break;
                        case 7:
                            shMsg=S.getValue().toString().trim();
                            break;

                    }

                    i++;
                }
                //Log.d("yomon",Integer.parseInt(shMonth)-1+" ");
                try{
                    String monthString = new DateFormatSymbols().getMonths()[Integer.parseInt(shMonth)-1];
                    String shdate= shDay+" " + monthString.substring(0,3).toUpperCase() +" "+shYear;

                    Transaction transaction=new Transaction(tid,amount,cat,shname,shdate,shMsg,sharedWith);

                    TransactionListUF.add(transaction);
                    mAdapterUF.notifyDataSetChanged();
                }
                catch (Exception e){

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


}