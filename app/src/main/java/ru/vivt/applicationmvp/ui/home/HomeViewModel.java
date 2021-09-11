package ru.vivt.applicationmvp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.Server;

public class HomeViewModel extends ViewModel {

    private Server server;
    private MutableLiveData<String> mText;
    private String[] itemNews = new String[]{};
    private String qrCode;

    public HomeViewModel() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    server = Server.getInstance();
                    qrCode = server.getQrCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        itemNews = new String[] {"hello Title 1", "title 2", "title 3", "title 4", "title 4", "title 5", "title 6", "title 7", "title 8"};
    }

    public LiveData<String> getText() {
        return mText;
    }

    public String[] getItemNews() {
        return itemNews;
    }

    public String getQrCode() {

        return qrCode;
    }
}