package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Visualization_all extends AppCompatActivity {
    EditText mEdit;
    EditText mEdit1,mEdit2,mEdit3,mEdit4,mEdit5;
    private Button Vis,Vis1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_all);

                Vis = (Button) findViewById(R.id.button2);
        Vis1 = (Button) findViewById(R.id.button5);
        Vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c=new Intent(Visualization_all.this,Day_wise_visualization.class);
                mEdit   = (EditText)findViewById(R.id.editTextNumber);
                String m2=mEdit.getText().toString();
                mEdit1   = (EditText)findViewById(R.id.editTextNumber2);

                String y2=mEdit1.getText().toString();
                String day2="0";
                System.out.println(m2+"yesssss     ");
                Bundle b = new Bundle();
                b.putString("m2",m2);
                b.putString("y2",y2);
               b.putString("d2",day2);
                c.putExtras(b);
                startActivity(c);
            }

        });
        Vis1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c=new Intent(Visualization_all.this,MonthWiseVis.class);
                mEdit2=(EditText)findViewById(R.id.editTextNumber3);
                String m1=mEdit2.getText().toString();
                mEdit3=(EditText)findViewById(R.id.editTextNumber6);
                String m2=mEdit3.getText().toString();
                mEdit4   = (EditText)findViewById(R.id.editTextNumber4);
                String y1=mEdit4.getText().toString();
                mEdit5   = (EditText)findViewById(R.id.editTextNumber7);
                String y2=mEdit5.getText().toString();

                String day2="0";
                System.out.println(m2+"yesssss     ");
                Bundle b = new Bundle();
                b.putString("m1",m1);
                b.putString("y1",y1);
                b.putString("d1",day2);
                b.putString("m2",m2);
                b.putString("y2",y2);
                b.putString("d2",day2);

                c.putExtras(b);
                startActivity(c);
            }

        });
    }
}