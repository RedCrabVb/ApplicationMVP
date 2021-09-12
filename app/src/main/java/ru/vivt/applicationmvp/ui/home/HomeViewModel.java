package ru.vivt.applicationmvp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.concurrent.atomic.AtomicInteger;

import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.News;

public class HomeViewModel extends ViewModel {

    private Server server;
    private MutableLiveData<String> mText;
    private News[] news = new News[]{};
    private String qrCode;

    public HomeViewModel() {
        new Thread(() -> {
            {
                server = Server.getInstance();
                qrCode = server.getQrCode();
                news = server.getNews();
            }
        }).start();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public String getQrCode() {
        return qrCode;
    }

    public News[] getNews() {
        return news;
    }
}