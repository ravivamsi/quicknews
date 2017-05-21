package com.example.abdulsamad.quickboxnewaplication;

/**
 * Created by ABDUL Samad on 5/19/2017.
 */

public class ArticlesDataProvider {
    String id,title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArticlesDataProvider(String id, String title) {

        this.id = id;
        this.title = title;
    }
}
