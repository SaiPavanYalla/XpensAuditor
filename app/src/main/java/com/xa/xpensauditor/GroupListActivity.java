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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabgl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupListActivity.this,CreateGroupActivity.class);
                startActivity(i);
            }
        });

        final ListView groupListView = findViewById(R.id.grouplist);
        ArrayList<String> groupNameList = new ArrayList<>();
        ArrayList<String> groupCountList = new ArrayList<>();

//        TODO: Get the list of group names and members count from firebase and populate arrayList

        groupNameList.add("Group 1");
        groupCountList.add("5");
        groupNameList.add("Group 2");
        groupCountList.add("3");

        CustomGroupList customGroupList = new CustomGroupList(GroupListActivity.this, groupNameList, groupCountList);
        groupListView.setAdapter(customGroupList);

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent(GroupListActivity.this, GroupActivity.class);
                String groupName = groupNameList.get(position);
//                TODO: Get the group key from firebase based on groupName
                String groupKey = "";
                intent.putExtra("group_key", groupKey);
                startActivity(intent);
            }
        });
    }

}

class CustomGroupList extends ArrayAdapter {
    ArrayList<String> groupNames;
    ArrayList<String> groupCount;
    Activity context;

    public CustomGroupList(Activity context, ArrayList<String> groupNames, ArrayList<String> groupCount) {
        super(context, R.layout.group_list_row_item, groupNames);
        this.context = context;
        this.groupNames = groupNames;
        this.groupCount = groupCount;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.group_list_row_item, null, true);
        TextView textGroupName = (TextView) row.findViewById(R.id.textGroupName);
        TextView textGroupCount = (TextView) row.findViewById(R.id.textGroupCount);
        ImageView imageGroup = (ImageView) row.findViewById(R.id.imageGroupImage);

        textGroupName.setText(groupNames.get(position));
        String groupCountStr = groupCount.get(position)+" Group Members";
        textGroupCount.setText(groupCountStr);
        imageGroup.setImageResource(R.drawable.ic_avtion_group_list_item);
        return  row;
    }
}