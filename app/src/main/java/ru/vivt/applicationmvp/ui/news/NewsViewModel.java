package ru.vivt.applicationmvp.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.concurrent.atomic.AtomicInteger;

import ru.vivt.applicationmvp.ui.repository.News;
import ru.vivt.applicationmvp.ui.repository.Server;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private News[] news = new News[]{};

    public NewsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is news fragment");
        new Thread(() -> {
            {
                news = Server.getInstance().getNews();
            }
        }).start();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public News[] getNews() {
        return news;
    }
}