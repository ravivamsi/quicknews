package com.example.abdulsamad.quickboxnewaplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
/**
 * Created by DELL on 24-08-2016.
 */
public class ArticlesAdapter extends BaseAdapter {
    Context context;
    ArrayList<ArticlesDataProvider> serviceManualArrayList;
    LayoutInflater inflater;
    public ArticlesAdapter(Context context, ArrayList<ArticlesDataProvider> serviceManualArrayList) {
        this.context = context;
        this.serviceManualArrayList = serviceManualArrayList;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return serviceManualArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return i;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.customview_articles, viewGroup, false);
            holder.id = (TextView) view.findViewById(R.id.id);
            holder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        ArticlesDataProvider serviceManual = serviceManualArrayList.get(i);
        holder.id.setText(serviceManual.getId());
        holder.title.setText(serviceManual.getTitle());
        return view;
    }
    class Holder {
        TextView id,title;
    }
}