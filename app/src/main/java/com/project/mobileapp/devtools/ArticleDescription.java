package com.project.mobileapp.devtools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ArticleDescription extends AppCompatActivity {

    SharedPreferences preferences;
    TextView description,nodf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_description);
        preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        description=(TextView)findViewById(R.id.description);
        nodf=(TextView)findViewById(R.id.nodf);
        description.setText(preferences.getString("description",""));
        if (description.getText().toString().length()<1)
        {
            nodf.setVisibility(View.VISIBLE);
        }else
        {
            nodf.setVisibility(View.INVISIBLE);
        }
    }
    public void gobacktoarticles(View view)
    {
        finish();
        startActivity(new Intent(this, ListofArticles.class));
    }
}
