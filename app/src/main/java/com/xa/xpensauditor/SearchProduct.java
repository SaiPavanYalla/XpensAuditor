package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import android.app.ProgressDialog;

public class SearchProduct extends AppCompatActivity{

    static ListView listView;
    EditText input;
    ImageView search_button;
    static ListViewAdapter adapter;
    static ArrayList<String> rows;
    static Context context;
    Handler fetchHandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        listView = findViewById(R.id.list);
        input = findViewById(R.id.input);
        search_button = findViewById(R.id.search_button);
        context = getApplicationContext();

        rows = new ArrayList<>();

        listView.setLongClickable(true);
        adapter = new ListViewAdapter(this, rows);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) listView.getItemAtPosition(position);
                Toast.makeText(SearchProduct.this, clickedItem, Toast.LENGTH_SHORT).show();
            }
        });

       search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString();
                if (text == null || text.length() == 0) {
                    makeToast("Enter a valid product name.");
                } else {
                    new FetchProduct().start();
                 //   addItem();
                }
            }
        });
        loadContent();
    }

    public void loadContent() {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "list.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(content);
            String s = new String(content);
            s = s.substring(1, s.length() - 1);
            String split[] = s.split(", ");

            if (split.length == 1 && split[0].isEmpty())
                rows = new ArrayList<>();
            else rows = new ArrayList<>(Arrays.asList(split));

            adapter = new ListViewAdapter(this, rows);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addItem() {
        listView.setAdapter(adapter);
    }

    class FetchProduct extends Thread{
        String data = "";

        @Override
        public void run(){

            fetchHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(SearchProduct.this);
                    progressDialog.setMessage("Fetching Data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
            try {
                String api_link = "https://online-product-scrapper.onrender.com/search_walmart?query="+input.getText().toString();
                URL url = new URL(api_link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null ){
                    data = data + line;
                }
                if(!data.isEmpty()){
                    JSONArray jSONArray = new JSONArray(data);
                    for(int i=0;i<jSONArray.length();i++){
                        JSONObject product = jSONArray.getJSONObject(i);
                        String title = product.getString("title");
                        String price = product.getString("price");
                        String website = product.getString("website");
                        if(!title.isEmpty()){
                            rows.add("Name: "+title+ "\nPrice: "+price+ "\nWebsite: "+website);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            fetchHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    static Toast t;
    private static void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        t.show();
    }
}