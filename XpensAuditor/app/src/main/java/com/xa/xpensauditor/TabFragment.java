package com.xa.xpensauditor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TabFragment extends Fragment {
    private Firebase mRootRef;
    private Firebase RefUid,RefTran,RefCatTran,RefCatSum, RefCat;
    int pos, currentDay,currentMonth,currentYear;
    double intSum;
    private String tagId, delCategory, delAmt, catChangeTo;
    private TextView textView;

    private ArrayList<String> CatgTF=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterTF;
    private ListView changeCatTF;
    private List<Transaction> TransactionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransAdapter mAdapter1;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos1", position);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }
    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toast.makeText(view.getContext(),"position: "+position,Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
        currentDay = (calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth = (calendar.get(Calendar.MONTH)+1);
        currentYear = (calendar.get(Calendar.YEAR));
        mRootRef=new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefTran = RefUid.child("DateRange").child(currentMonth+"-"+currentYear).child("Transactions");
        RefCatTran = RefUid.child("DateRange").child(currentMonth+"-"+currentYear).child("CatTran");
        RefCatSum = RefUid.child("DateRange").child(currentMonth+"-"+currentYear).child("CatSum");
        RefCat = RefUid.child("Categories");

        arrayAdapterTF=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,CatgTF);

        RefCat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value= dataSnapshot.getKey().trim();
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




        //Toast.makeText(getContext(),currentMonth+"/"+currentYear,Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter1 = new TransAdapter(TransactionList);
        recyclerView.setAdapter(mAdapter1);
        prepareTransactionData();


        registerForContextMenu(recyclerView);

        mAdapter1.setOnItemClickListener(new TransAdapter.ClickListener() {
            @Override
            public void OnItemClick(int position, View v) {
                //Toast.makeText(getActivity(),TransactionList.get(position).getTid(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(),SMSDBFetchActivity.class);
                i.putExtra("indexPos",TransactionList.get(position).getTid());
                startActivity(i);
            }

            @Override
            public void OnItemLongClick(int position, View v) {
                Log.i("yoyoyo","Here: "+position);
                pos=position;
            }
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case 11:{
                int show = item.getGroupId();

                tagId=TransactionList.get(show).getTid();
                delCategory = TransactionList.get(show).getT_cat();
                delAmt = TransactionList.get(show).getT_amt();

                // Toast.makeText(getActivity(),tagId+"-"+"Delete it",Toast.LENGTH_SHORT).show();

                RefTran.child(tagId).removeValue();
                RefUid.child("DateRange").child(currentMonth+"-"+currentYear).child("CatTran").child(delCategory).child(tagId).removeValue();
                RefUid.child("DateRange").child(currentMonth+"-"+currentYear).child("CatSum").child(delCategory).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String sumCat = dataSnapshot.getValue().toString().trim();
                        intSum = Double.parseDouble((sumCat));
                        double newDelAmt =  Double.parseDouble((delAmt));
                        intSum = Math.round((intSum - newDelAmt)*100.0)/100.0;
                        if(intSum==0.00) {
                            dataSnapshot.getRef().removeValue();
                            mAdapter1.notifyDataSetChanged();
                        }
                        else
                            dataSnapshot.getRef().setValue(String.valueOf(intSum));

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                TransactionList.clear();
                prepareTransactionData();


            }break;

           /* case 12:{
                int show = item.getGroupId();
                tagId=TransactionList.get(show).getTid();
                Toast.makeText(getActivity(),tagId+"-"+"Change it",Toast.LENGTH_SHORT).show();

                final Transaction updateTransac = new Transaction(TransactionList.get(show)); //Transaction to be modified

                //Toast.makeText(getActivity(),tagId+"-"+"Change it",Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.tab_dialog);
                dialog.setTitle("Title...");

                changeCatTF = (ListView) dialog.findViewById(R.id.CatgListTF);
                changeCatTF.setAdapter(arrayAdapterTF);
                dialog.show();
                changeCatTF.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        catChangeTo = adapterView.getItemAtPosition(i).toString().trim();
                        Toast.makeText(getActivity(),catChangeTo,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        //Deleting from everywhere
                        RefTran.child(tagId).removeValue();
                        RefCatTran.child(updateTransac.getT_cat()).child(tagId).removeValue();

                        //Adding to the new category
                        String tempDate = updateTransac.getT_date();
                        String[] dateSet = tempDate.split(" - ");
                        RefTran.child(tagId).child("Amount").setValue(updateTransac.getT_amt());
                        RefTran.child(tagId).child("Category").setValue(catChangeTo);
                        RefTran.child(tagId).child("Day").setValue(dateSet[0]);
                        RefTran.child(tagId).child("Month").setValue(dateSet[1]);
                        RefTran.child(tagId).child("Shop Name").setValue(updateTransac.getT_shopname());
                        RefTran.child(tagId).child("Year").setValue(dateSet[2]);
                        RefTran.child(tagId).child("ZMessage").setValue(updateTransac.getT_msg());
                        RefCatTran.child(catChangeTo).child(tagId).child("Amount").setValue(updateTransac.getT_amt());
                        RefCatTran.child(catChangeTo).child(tagId).child("Category").setValue(catChangeTo);
                        RefCatTran.child(catChangeTo).child(tagId).child("Day").setValue(dateSet[0]);
                        RefCatTran.child(catChangeTo).child(tagId).child("Month").setValue(dateSet[1]);
                        RefCatTran.child(catChangeTo).child(tagId).child("Shop Name").setValue(updateTransac.getT_shopname());
                        RefCatTran.child(catChangeTo).child(tagId).child("Year").setValue(dateSet[2]);
                        RefCatTran.child(catChangeTo).child(tagId).child("ZMessage").setValue(updateTransac.getT_msg());


                        RefCatSum.child(updateTransac.getT_cat()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String sumCat = dataSnapshot.getValue().toString().trim();
                                intSum = Integer.parseInt(sumCat);
                                Integer newDelAmt = Integer.parseInt(updateTransac.getT_amt());
                                intSum = intSum - newDelAmt;
                                if(intSum==0)
                                    dataSnapshot.getRef().removeValue();
                                else
                                    dataSnapshot.getRef().setValue(String.valueOf(intSum));
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                        RefCatSum.child(catChangeTo).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String sumCat = dataSnapshot.getValue().toString().trim();
                                intSum = Integer.parseInt(sumCat);
                                Integer newDelAmt = Integer.parseInt(updateTransac.getT_amt());
                                intSum = intSum + newDelAmt;
                                if(intSum==0)
                                    dataSnapshot.getRef().removeValue();
                                else
                                    dataSnapshot.getRef().setValue(String.valueOf(intSum));
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                });



            }break;*/
        }
        return super.onContextItemSelected(item);
    }

    private void prepareTransactionData() {

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
                String shdate= shMonth+"/"+shDay+"/"+shYear;

                Transaction transaction=new Transaction(tid,amount,cat,shname,shdate,shMsg);
                //Toast.makeText(getApplicationContext(),transaction.getT_amt(),Toast.LENGTH_SHORT).show();
                TransactionList.add(transaction);
                mAdapter1.notifyDataSetChanged();
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

