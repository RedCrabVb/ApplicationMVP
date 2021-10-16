package ru.vivt.applicationmvp.ui.repository;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Server {
    private static Server server;
    public static String error = "error", status = "status";

    private static String url = "";
    private final String apiNews = "api/news";
    private final String apiQrCode = "api/qrCode";
    private final String apiPersonData = "api/setPersonDate";
    private final String apiRegistration = "api/registration";
    private final String apiStatusToken = "api/getStatusToken";
    private final String apiResetPassword = "api/resetPassword";
    private final String apiAuthorization = "api/authorization";
    private final String apiPersonDataGet = "api/personData";


    private String tokenConnection = null;
    private JsonObject jsonObjectNews = null;

    private Server() {

    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public static Server getInstance(String ip, Context context) {
        if (server == null) {
            url = "http://" + ip + "/%s?%s";
            server = new Server();
        }
        return server;
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

    public JsonObject getApiPersonData() throws Exception {
        JsonObject result = new JsonParser().parse(sendInquiry(apiPersonDataGet, String.format("token=%s",  tokenConnection))).getAsJsonObject();
        return result;
    }

    public String resetPassword(String email) {
        String result = "error";
        try {
            result = sendInquiry(apiResetPassword, String.format("email=%s", email));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getNews() {
        try {
            String linkNews = Server.getInstance().getNewsJson().get("News").getAsString();
            return linkNews;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String updateDataAboutProfile(String userName, String email, String password) throws Exception {
        String result = sendInquiry(apiPersonData, String.format("token=%s&username=%s&email=%s&password=%s", tokenConnection, userName, email, password));
        System.out.println(result);
        return result;
    }

    public String authorization(String email, String password) throws Exception {
        String result = sendInquiry(apiAuthorization, String.format("email=%s&password=%s",  email, password));
        return result;
    }


    public boolean registration() {
        try {
            String result = (sendInquiry(apiRegistration, ""));
            JsonObject jsonReg =  new JsonParser().parse(result).getAsJsonObject();
            tokenConnection = jsonReg.get("token").getAsString();
            System.out.println(result);
            return true;
        } catch (Exception | Error e) {
            e.printStackTrace();
            return  false;
        }
    }

    private String getQrCode() {
        try {
            String result = sendInquiry(apiQrCode, String.format("token=%s", tokenConnection));
            JsonObject json = new JsonParser().parse(result).getAsJsonObject();
            return json.get("qrCode").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private JsonObject getNewsJson() throws Exception {
        if (jsonObjectNews == null) {
            String result = sendInquiry(apiNews, "");
            jsonObjectNews = new JsonParser().parse(result).getAsJsonObject();
        }
        return jsonObjectNews;
    }

    public void setTokenConnection(String tokenConnection) {
        this.tokenConnection = tokenConnection;
    }

    public void saveDataInMemory(MemoryValues memoryValues) {
        memoryValues.setQrCode(getQrCode());
        memoryValues.setToken(tokenConnection);

        try {
            JsonObject json = getApiPersonData();
            String username = json.get(MemoryValues.username).getAsString();
            String email = json.get(MemoryValues.email).getAsString();
            memoryValues.setUsername(username.isEmpty() ? "No name" : username);
            memoryValues.setEmail(email.isEmpty() ? "No name" : email);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
