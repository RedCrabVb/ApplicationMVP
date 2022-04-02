package ru.vivt.applicationmvp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class HomeViewModel extends ViewModel {

    private Server server;
    private MutableLiveData<String> qrCode;
    private MemoryValues memoryValues;
    public HomeViewModel() {
        memoryValues = MemoryValues.getInstance();
        qrCode = new MutableLiveData<>();
        new Thread(() -> {
                server = Server.getInstance();
                qrCode.postValue(memoryValues.getQrCode());
        }).start();

    }

    public LiveData<String> getQrCode() {
        return qrCode;
    }

}