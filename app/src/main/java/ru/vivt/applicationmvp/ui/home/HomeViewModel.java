package ru.vivt.applicationmvp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonArray;

import java.util.concurrent.atomic.AtomicInteger;

import ru.vivt.applicationmvp.ui.repository.Server;

public class HomeViewModel extends ViewModel {

    private Server server;
    private MutableLiveData<String> mText;
    private String[] itemNews = new String[]{};
    private String[] itemNewsBody = new String[]{};
    private String[] itemNewsImgPath = new String[]{};
    private String qrCode;

    public HomeViewModel() {
        new Thread(() -> {
            {
                try {
                    server = Server.getInstance();
                    qrCode = server.getQrCode();

                    JsonArray jsonArrayNews = server.getNews().getAsJsonArray("News");
                    itemNews = new String[jsonArrayNews.size()];
                    itemNewsBody = new String[jsonArrayNews.size()];
                    itemNewsImgPath = new String[jsonArrayNews.size()];
                    AtomicInteger i = new AtomicInteger();
                    jsonArrayNews.forEach(r -> {
                        i.getAndIncrement();
                        itemNews[i.get() - 1] = r.getAsJsonObject().get("title").getAsString();
                        itemNewsBody[i.get() - 1] = r.getAsJsonObject().get("body").getAsString();
                        itemNewsImgPath[i.get() - 1] = r.getAsJsonObject().get("imgPath").getAsString();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
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

    public String[] getItemNewsImgPath() {
        return itemNewsImgPath;
    }

    public String[] getItemNewsBody() {
        return itemNewsBody;
    }
}