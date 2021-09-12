package ru.vivt.applicationmvp.ui.repository;

import android.graphics.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    public static final String qrCode = "qrCode", fileNameConfig = "config.json", token = "token";
    private static File getFilesDir;

    private static Server server;

    private static String url = "";
    private final String apiNews = "api/news";
    private final String apiQrCode = "api/qrCode";
    private final String apiPersonData = "api/setPersonDate";
    private final String apiRegistration = "api/registration";
    private final String apiImgNews = "src/img";
    private final String apiStatusToken = "api/getStatusToken";

    private String tokenConnection = null;
    private String qrCodeConntion = null;
    private JsonObject jsonObjectNews = null;

    private Server() {

    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public static Server getInstance(String ip) {
        if (server == null) {
            url = "http://" + ip + ":8080/%s?%s";
            server = new Server();
        }
        return server;
    }

    public static File getGetFilesDir() {
        return getFilesDir;
    }

    public static void setGetFilesDir(File getFilesDir) {
        Server.getFilesDir = getFilesDir;
    }

    public boolean tokenActive() {
        try {
            return new JsonParser().parse(sendInquiry(apiStatusToken, String.format("token=%s", tokenConnection)))
                    .getAsJsonObject().get("result").getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPathUrlToDownloadImgNews(String imgPath) {
        return String.format(url, apiImgNews, imgPath).replace('?', '/');
    }

    public News[] getNews() {
        try {
            News news[];
            JsonArray jsonArrayNews = Server.getInstance().getNewsJson().getAsJsonArray("News");
            news = new News[jsonArrayNews.size()];
            AtomicInteger i = new AtomicInteger();
            jsonArrayNews.forEach(r -> {
                i.getAndIncrement();
                JsonObject jsonNews = r.getAsJsonObject();
                news[i.get() - 1] = new News(jsonNews.get("title").getAsString(),
                        jsonNews.get("body").getAsString(),
                        jsonNews.get("imgPath").getAsString());
            });
            return news;
        } catch (Exception e) {
            e.printStackTrace();
            return new News[]{};
        }
    }

    /*
    Error set data (phoneNumber(Server, DB) - password(Android))
    */
    public void setData(String userName, String email, String password) throws Exception {
        String result = sendInquiry(apiPersonData, String.format("token=%s&username=%s&email=%s&phoneNumber=%s", tokenConnection, userName, email, password));
        System.out.println(result);
    }


    public boolean registration() {
        try {
            String api = "api/registration";
            String result = (sendInquiry(api, ""));
            JsonObject jsonReg =  new JsonParser().parse(result).getAsJsonObject();
            tokenConnection = jsonReg.get("token").getAsString();
            qrCodeConntion = jsonReg.get("qrCode").getAsString();
            System.out.println(result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }

    public String getQrCodeConntion() {
        System.out.println(qrCodeConntion + " get qr code");
        return qrCodeConntion;
    }

    private JsonObject getNewsJson() throws Exception {
        if (jsonObjectNews == null) {
            String result = sendInquiry(apiNews, "");
            jsonObjectNews = new JsonParser().parse(result).getAsJsonObject();
        }
        return jsonObjectNews;
    }

    private String sendInquiry(String api, String json) throws Exception {
        json = json.replace("+", "%20"); // fix space encoder
        URL url = new URL(String.format(this.url, api, json));
        HttpURLConnection connection = getResponseServer(url);
        String response = connectionResponseToString(connection);

        System.out.println("URL: " + url.toString());
        return response;
    }

    private HttpURLConnection getResponseServer(URL url) throws Exception {
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) urlConnection;

        return connection;
    }

    private String connectionResponseToString(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String urlString = "";

        String current;
        while ((current = in.readLine()) != null) {
            urlString += current;
        }

        connection.disconnect();

        return urlString;
    }

    public String getTokenConnection() {
        return tokenConnection;
    }

    public void setTokenConnection(String tokenConnection) {
        this.tokenConnection = tokenConnection;
    }

    public void setQrCodeConntion(String qrCodeConntion) {
        this.qrCodeConntion = qrCodeConntion;
    }
}
