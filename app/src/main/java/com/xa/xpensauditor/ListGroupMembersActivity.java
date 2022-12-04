package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListGroupMembersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_group_members);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("XpensAuditor");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplicationContext(), AddMemberToGroupActivity.class);
                    startActivity(i);
                } catch(Exception e) {
                    System.out.println("Error: " + e.toString());
                }
            }
        });

        final ListView list = findViewById(R.id.grouplist);
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<String> emailList = new ArrayList<String>();

//        TODO: Get the list of group names from firebase and populate arrayList

        GroupMembersAdaptor groupMembersAdaptor = new GroupMembersAdaptor(this, nameList, emailList);
        list.setAdapter(groupMembersAdaptor);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i=new Intent(getApplicationContext(), GroupActivity.class);
                startActivity(i);
            }
        });

    }
}

class GroupMembersAdaptor implements ListAdapter {
    ArrayList<String> names;
    ArrayList<String> emailIds;
    Context context;

    public GroupMembersAdaptor(Activity context, ArrayList<String> names, ArrayList<String> emailIds) {

        this.context = context;
        this.names = names;
        this.emailIds = emailIds;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView==null)
            row = inflater.inflate(R.layout.list_group_member_row, null, true);
        TextView textViewName = (TextView) row.findViewById(R.id.textViewName);
        TextView textViewEmail = (TextView) row.findViewById(R.id.textViewEmail);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.imageViewMember);

        textViewName.setText(names.get(position));
        textViewEmail.setText(emailIds.get(position));
        imageFlag.setImageResource(R.drawable.ic_action_group_member_item);
        return  row;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}