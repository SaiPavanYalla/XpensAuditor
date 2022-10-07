package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Suggest extends AppCompatActivity implements View.OnClickListener{

    EditText e;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        e=(EditText)findViewById(R.id.editText5);
        b=(Button)findViewById(R.id.button3);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String str=e.getText().toString();
        Toast.makeText(this, "Suggestion submitted", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }
}