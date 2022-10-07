package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ShowTransActivity extends AppCompatActivity {
    private Firebase mRootRef;
    private Firebase RefUid,RefTran;
    private Firebase RefName,RefEmail,RefPhnnum;
    private Firebase RefCat,RefFood,RefHealth,RefTravel,RefEdu,RefBills,RefHomeNeeds,RefOthers,RefUncat;
    private List<Transaction> transList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trans);

        mRootRef=new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefTran = RefUid.child("Transactions");
        RefName = RefUid.child("Name");
        RefEmail=RefUid.child("Email");
        RefPhnnum=RefUid.child("Phone Number");
        RefCat=RefUid.child("Categories");
        RefUncat=RefCat.child("Uncategorised");


        recyclerView = (RecyclerView) findViewById(R.id.rv_alltrans);

        mAdapter = new TransactionAdapter(transList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //prepareTransData();



        RefTran.addChildEventListener(new ChildEventListener() {
            String amount,cat,shname,shDay,shMonth,shYear,shMsg;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int i=0;
                String tid = dataSnapshot.getKey().toString().trim();

                for (DataSnapshot S:dataSnapshot.getChildren()) {
                    //String t_id=S.getValue().toString().trim();
                    //Toast.makeText(getApplicationContext(),"->"+i,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),t_id,Toast.LENGTH_SHORT).show();
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
                            shname=S.getValue().toString().trim();
                            break;
                        case 5:
                            shYear=S.getValue().toString().trim();
                            break;
                        case 6:
                            shMsg=S.getValue().toString().trim();
                            break;
                    }
                    //Transaction transaction=S.getValue(Transaction.class);
                    //transList.add(transaction);
                    i++;
                }
                String shdate= shDay+" - "+shMonth+" - "+shYear;
                Transaction transaction=new Transaction(tid,amount,cat,shname,shdate,shMsg);
                amount="";
                cat="";
                shname="";
                shdate="";
                //Toast.makeText(getApplicationContext(),transaction.getT_amt(),Toast.LENGTH_SHORT).show();
                transList.add(transaction);
                mAdapter.notifyDataSetChanged();
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
    private void prepareTransData() {
        //Transaction transaction = new Transaction("1234","FOOD","Zucca");
        //transList.add(transaction);
    }
}