package com.project.mobileapp.devtools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.mobileapp.devtools.Helper.Dialogs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListofArticles extends AppCompatActivity {

    Dialogs myDialogs;
    ArticlesAdapter adapter;
    ArrayList<ArticlesDataProvider> arrayList;
    ListView listView;
    Spinner sorting;
    SharedPreferences preferences;
    ArrayList<String> descriptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_articles);
        myDialogs=new Dialogs(this);
        preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        listView=(ListView)findViewById(R.id.listview);
        sorting=(Spinner)findViewById(R.id.sort);
        loadSorts();
        loadArticles("top");
    }

    private  void loadSorts()
    {
        List<String> categories = new ArrayList<String>();
        categories.add("top");
        categories.add("latest");
        categories.add("popular");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sorting.setAdapter(dataAdapter);
        sorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadArticles(sorting.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void gobacktohome(View view)
    {
        finish();
        startActivity(new Intent(this,Home.class));
    }
    private  void parsejson(JSONObject json)
    {
        arrayList=new ArrayList<>();
        descriptions=new ArrayList<>();
        try
        {
            if (json.getString("status").equalsIgnoreCase("ok"))
            {
                JSONArray articles=json.getJSONArray("articles");
                for(int i=0;i<articles.length();i++)
                {
                    JSONObject data=articles.getJSONObject(i);
                    arrayList.add(new ArticlesDataProvider("",data.getString("title")));
                    descriptions.add(data.getString("description"));
                }
                adapter=new ArticlesAdapter(this,arrayList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("description",descriptions.get(position));
                        editor.apply();
                        finish();
                        startActivity(new Intent(ListofArticles.this,ArticleDescription.class));
                    }
                });
            }else
            {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception ex)
        {
            Log.d("erro", "parsejson: ");
        }

    }
    public void loadArticles(String sortby)
    {
        myDialogs.showProgressDialog();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("newsapi.org")
                .appendPath("v1")
                .appendPath("articles")
                .appendQueryParameter("source",preferences.getString("s_id",""))
                .appendQueryParameter("sortBy",sortby)
                .appendQueryParameter("apiKey", "2df3c32ef6ff497b8c422bdf33fdcf71");
        String url=builder.build().toString();
        System.out.print("url="+url);
        JsonObjectRequest R=new JsonObjectRequest(Request.Method.GET, url, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onResponse:", response.toString());
                myDialogs.hideProgessDialog();
                parsejson(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(error.toString(), "onErrorResponse: ");
                        myDialogs.hideProgessDialog();
                        myDialogs.showDialog("Error","The news sources you've selected isn't available sorted by "+sorting.getSelectedItem().toString());
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(R);
    }
}
