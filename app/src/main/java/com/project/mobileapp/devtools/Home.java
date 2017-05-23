package com.project.mobileapp.devtools;
import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.mobileapp.devtools.Helper.Dialogs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Home extends AppCompatActivity {
    Spinner countries,sources;
    Dialogs myDialogs;
    ProgressBar progressBar;
    ArrayList<String> s_id;
    String source_id="";
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        handleSSLHandshake();
        preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        myDialogs=new Dialogs(this);
        countries=(Spinner)findViewById(R.id.countries);
        sources=(Spinner)findViewById(R.id.sources);
        loadCountries();
        getSoucrse();
    }
    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
    private void loadCountries() {
        List<String> categories = new ArrayList<String>();
        categories.add("Australia");
        categories.add("Germany");
        categories.add("United Kingdom");
        categories.add("India");
        categories.add("Italy");
        categories.add("United States of America");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        countries.setAdapter(dataAdapter);
        countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reloadSources();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadSources(JSONObject json) {
        List<String> categories = new ArrayList<String>();
        s_id=new ArrayList<>();
        try{
            if (json.getString("status").equalsIgnoreCase("ok"))
            {
                JSONArray array=json.getJSONArray("sources");
                for(int i=0;i<array.length();i++) {
                    JSONObject data=array.getJSONObject(i);
                    categories.add(data.getString("name"));
                    s_id.add(data.getString("id"));
                }
            }else {

                Toast.makeText(this, "No Sources Found", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex)
        {
            Log.d("error", "parse siources: ");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sources.setAdapter(dataAdapter);
        sources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                source_id=s_id.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private String getC_id() {
        String code="";
        if (countries.getSelectedItem().toString().equalsIgnoreCase("Australia"))
        {
            code="au";
        }else   if (countries.getSelectedItem().toString().equalsIgnoreCase("Germany"))
        {
            code="de";
        }else   if (countries.getSelectedItem().toString().equalsIgnoreCase("United Kingdom"))
        {
            code="gb";
        }else   if (countries.getSelectedItem().toString().equalsIgnoreCase("India"))
        {
            code="in";
        }else   if (countries.getSelectedItem().toString().equalsIgnoreCase("Italy"))
        {
            code="it";
        }else   if (countries.getSelectedItem().toString().equalsIgnoreCase("United States of America"))
        {
            code="us";
        }

            return code;
    }
    public void getNews(View view) {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("s_id",source_id);
        editor.apply();
        finish();
        startActivity(new Intent(this,ListofArticles.class));
    }
    public void getSoucrse() {
        myDialogs.showProgressDialog();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("newsapi.org")
                .appendPath("v1")
                .appendPath("sources");
        String url=builder.build().toString();
        System.out.print("url="+url);
        JsonObjectRequest R=new JsonObjectRequest(Request.Method.GET, url, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onResponse:", response.toString());
                myDialogs.hideProgessDialog();
                loadSources(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(error.toString(), "onErrorResponse: ");
                        myDialogs.hideProgessDialog();
                        Toast.makeText(Home.this, "Internal Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(R);

    }
    public void reloadSources() {
        myDialogs.showProgressDialog();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("newsapi.org")
                .appendPath("v1")
                .appendPath("sources")
                .appendQueryParameter("country",getC_id());
        String url=builder.build().toString();
        System.out.print("url="+url);
        JsonObjectRequest R=new JsonObjectRequest(Request.Method.GET, url, "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onResponse:", response.toString());
                myDialogs.hideProgessDialog();
                loadSources(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        myDialogs.hideProgessDialog();
                        System.out.print(error.toString());
                        Log.d(error.toString(), "onErrorResponse: ");
                        Toast.makeText(Home.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(R);
    }
}