package ru.vivt.applicationmvp.ui.repository;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class Server {
    private static Server server;

    private static String url = "";
    private final String apiNews = "api/news";
    private final String apiQrCode = "api/qrCode";
    private final String apiPersonData = "api/setPersonDate";
    private final String apiRegistration = "api/registration";
    private final String apiImgNews = "src/img";

    private String token = null;
    private String qrCode = null;
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

    public boolean tokenActive() {
        return true;
    }

    public String getPathUrlToDownloadImgNews(String imgPath) {
        return String.format(url, apiImgNews, imgPath).replace('?', '/');
    }

    /*
    Error set data (phoneNumber(Server, DB) - password(Android))
    */
    public void setData(String userName, String email, String password) throws Exception {
        String result = sendInquiry(apiPersonData, String.format("token=%s&username=%s&email=%s&phoneNumber=%s",  token, userName, email, password));
        System.out.println(result);
    }


    public boolean registration() {
        try {
            String api = "api/registration";
            String result = (sendInquiry(api, ""));
            JsonObject jsonReg =  new JsonParser().parse(result).getAsJsonObject();
            token = jsonReg.get("token").getAsString();
            qrCode = jsonReg.get("qrCode").getAsString();
            System.out.println(result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }

    public String getQrCode() {
        System.out.println(qrCode + " get qr code");
        return qrCode;
    }

    public JsonObject getNews() throws Exception {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
