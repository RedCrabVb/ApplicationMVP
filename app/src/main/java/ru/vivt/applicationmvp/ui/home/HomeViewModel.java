package ru.vivt.applicationmvp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class HomeViewModel extends ViewModel {

    private Server server;
    private MutableLiveData<String> mText;
    private MutableLiveData<String> newsLink;
    private MutableLiveData<String> qrCode;
    private MemoryValues memoryValues;
    public HomeViewModel() {
        memoryValues = MemoryValues.getInstance();

        newsLink = new MutableLiveData<>();
        qrCode = new MutableLiveData<>();
        new Thread(() -> {
                server = Server.getInstance();
                qrCode.postValue(memoryValues.getQrCode());
                newsLink.postValue(server.getInstance().getNews());
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