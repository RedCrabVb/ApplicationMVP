package ru.vivt.applicationmvp.ui.repository;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Arrays;

import ru.vivt.applicationmvp.ActivityNews;
import ru.vivt.applicationmvp.R;

public class ObserverArrayNews implements Observer<News[]> {
    private Context context;
    private ListView listView;

    public ObserverArrayNews(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    public void onChanged(News[] newsArr) {
        ArrayAdapter<News> arrayAdapter = new NewsAdapter(context, R.layout.list_news,
                new ArrayList<>(Arrays.asList(newsArr)));
        listView.setAdapter(arrayAdapter);

        /*show news list*/
        listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("click item + " + position);

            Intent intent = new Intent(context, ActivityNews.class);
            Bundle bundle = new Bundle();
            News news = newsArr[position];
            bundle.putString("header", news.getTitle());
            bundle.putString("body", news.getBody());
            bundle.putString("imgPath", news.getImgPath());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }
}
