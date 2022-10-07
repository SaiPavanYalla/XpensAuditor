package com.xa.xpensauditor;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.MyViewHolder2> {
    private List<Transaction> transList;
    private static TransAdapter.ClickListener mClickListener;

    private int position;

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }

    @Override
    public TransAdapter.MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alltrans_list_row, parent, false);
        return new TransAdapter.MyViewHolder2(itemView);
    }

    @Override
    public void onBindViewHolder(final TransAdapter.MyViewHolder2 holder, int position) {

        Transaction trans = transList.get(position);
        holder.tid.setText(trans.getTid());
        holder.tcat.setText(trans.getT_cat());
        holder.tamt.setText("$"+trans.getT_amt());
        holder.tshopname.setText(trans.getT_shopname());
        holder.tdate.setText(trans.getT_date());

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
        public TextView tid, tamt, tcat,tshopname,tdate;


        public MyViewHolder2(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            tid = (TextView) view.findViewById(R.id.tid);
            tamt = (TextView) view.findViewById(R.id.tamt);
            tcat = (TextView) view.findViewById(R.id.tcat);
            tshopname = (TextView) view.findViewById(R.id.tshopname);
            tdate=(TextView) view.findViewById(R.id.tdate);
            view.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onClick(View v)
        {
            mClickListener.OnItemClick(getAdapterPosition(),v);
        }

        @Override
        public boolean onLongClick(View v){
            mClickListener.OnItemLongClick(getAdapterPosition(),v);
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an action");
            menu.add(getAdapterPosition(),11,0,"Delete");

        }
    }

    public void setOnItemClickListener(TransAdapter.ClickListener clickListener){
        TransAdapter.mClickListener = clickListener;

    }


    public interface ClickListener{
        void OnItemClick(int position, View v);
        void OnItemLongClick(int position, View v);
    }

    public TransAdapter(List<Transaction> transList) {
        this.transList = transList;
    }

}
