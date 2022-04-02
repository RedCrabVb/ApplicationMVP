package ru.vivt.applicationmvp.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.Server;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> newsLink;

    public NewsViewModel() {
        newsLink = new MutableLiveData<>();
//        new Thread(() -> {
//                newsLink.postValue(Server.getInstance().getNews());
//
//        }).start();

    }

    public LiveData<String> getNewsLink() {
        return newsLink;
    }
}