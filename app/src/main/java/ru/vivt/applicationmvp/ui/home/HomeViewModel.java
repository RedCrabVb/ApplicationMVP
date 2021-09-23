package ru.vivt.applicationmvp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.News;

public class HomeViewModel extends ViewModel {

    private Server server;
    private MutableLiveData<String> mText;
    private MutableLiveData<String> newsLink;
    private MutableLiveData<String> qrCode;

    public HomeViewModel() {
        newsLink = new MutableLiveData<>();
        qrCode = new MutableLiveData<>();
        new Thread(() -> {
                server = Server.getInstance();
                qrCode.postValue(server.getQrCodeConntion());
                newsLink.postValue(Server.getInstance().getNews());
        }).start();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getQrCode() {
        return qrCode;
    }

    public LiveData<String> getNewsLink() {
        return newsLink;
    }
}