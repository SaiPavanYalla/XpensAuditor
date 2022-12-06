package com.xa.xpensauditor;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.MyViewHolder2> {
    private List<Transaction> transList;
    private static TransAdapter.ClickListener mClickListener;

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public TransAdapter.MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alltrans_list_row, parent, false);
        return new TransAdapter.MyViewHolder2(itemView);
    }

    public void refreshTransactionList() {
        this.notifyDataSetChanged();
    }

    public void sort(int sortType) {
        switch (sortType) {
            case 0:
                // SORT BY AMOUNT
                this.transList.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction transaction, Transaction t1) {
                        int tAmount = (int) (Float.parseFloat(transaction.getAmountStr()) * 100);
                        int t1Amount = (int) (Float.parseFloat(t1.getAmountStr()) * 100);
                        return t1Amount - tAmount;
                    }
                });
                this.notifyDataSetChanged();
                break;
            case 1:
                // SORT BY DATE
                this.transList.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction transaction, Transaction t1) {
                        return transaction.getDateInt() - t1.getDateInt();
                    }
                });
                this.notifyDataSetChanged();
                break;
            case 2:
                // SORT BY CATEGORY
                this.transList.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction transaction, Transaction t1) {
                        return transaction.getCategory().compareTo(t1.getCategory());
                    }
                });
                this.notifyDataSetChanged();
                break;
            case 3:
                // SORT BY MEMO
                this.transList.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction transaction, Transaction t1) {
                        return transaction.getShopname().compareTo(t1.getShopname());
                    }
                });
                this.notifyDataSetChanged();
                break;
            default:
        }
    }

    @Override
    public void onBindViewHolder(final TransAdapter.MyViewHolder2 holder, int position) {

        Transaction trans = transList.get(position);
        holder.tId.setText(trans.getTid());
        holder.tCategory.setText(trans.getCategory());
        holder.tAmount.setText("$" + trans.getAmountStr());
        holder.tShopname.setText(trans.getShopname());
        holder.tDate.setText(trans.getDateStr());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return transList.size();
    }

    @Override
    public void onViewRecycled(TransAdapter.MyViewHolder2 holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }


    public class MyViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {
        public TextView tId, tAmount, tCategory, tShopname, tDate;


        public MyViewHolder2(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            tId = (TextView) view.findViewById(R.id.tid);
            tAmount = (TextView) view.findViewById(R.id.tamt);
            tCategory = (TextView) view.findViewById(R.id.tcat);
            tShopname = (TextView) view.findViewById(R.id.tshopname);
            tDate = (TextView) view.findViewById(R.id.tdate);
            view.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onClick(View v) {
            mClickListener.OnItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            mClickListener.OnItemLongClick(getAdapterPosition(), v);
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an action");
            menu.add(getAdapterPosition(), 11, 0, "Delete");

        }
    }

    public void setOnItemClickListener(TransAdapter.ClickListener clickListener) {
        TransAdapter.mClickListener = clickListener;

    }


    public interface ClickListener {
        void OnItemClick(int position, View v);

        void OnItemLongClick(int position, View v);
    }

    public TransAdapter(List<Transaction> transList) {
        this.transList = transList;
    }

}
