package ru.vivt.applicationmvp.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.Server;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> newsLink;

    public NewsViewModel() {
        mText = new MutableLiveData<>();
        newsLink = new MutableLiveData<>();
        mText.setValue("This is news fragment");
        new Thread(() -> {
                newsLink.postValue(Server.getInstance().getNews());
        }).start();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getNewsLink() {
        return newsLink;
    }
}