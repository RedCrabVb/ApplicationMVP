package ru.vivt.applicationmvp.ui.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static Server server;
    public static String error = "error", status = "status";

    private static String url = "";
    private static final String apiNews = "api/news";
    private static final String apiTestAll = "api/testAll";
    private static final String apiTestCurrent = "api/test";
    private static final String apiQrCode = "api/qrCode";
    private static final String apiPersonData = "api/setPersonDate";
    private static final String apiRegistration = "api/registration";
    private static final String apiStatusToken = "api/getStatusToken";
    private static final String apiResetPassword = "api/resetPassword";
    private static final String apiAuthorization = "api/authorization";
    private static final String apiPersonDataGet = "api/personData";


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

    public static Server getInstance(String ip) {
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
        return new JsonParser().parse(sendInquiry(apiPersonDataGet, String.format("token=%s",  tokenConnection))).getAsJsonObject();
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
            return Server.getInstance().getNewsJson().get("News").getAsString();
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
        return sendInquiry(apiAuthorization, String.format("email=%s&password=%s",  email, password));
    }

    private JsonObject getTestServer() throws Exception {
        JsonObject test = new JsonParser().parse(sendInquiry(apiTestAll, "")).getAsJsonObject();
        System.out.println(test);
        return test;
    }

    public Test[] getTest() {
        try {
            Test[] news;
            JsonArray jsonArrayTest = getTestServer().getAsJsonArray("test");
            news = new Test[jsonArrayTest.size()];
            AtomicInteger i = new AtomicInteger();
            for(JsonElement r : jsonArrayTest){
//                i.getAndIncrement();
                JsonObject jsonNews = r.getAsJsonObject();
                news[i.getAndIncrement()] = new Test(jsonNews.get("test").getAsString(),
                        jsonNews.get("description").getAsString());
            }
//            System.out.println(jsonArrayTest);
            return news;
        } catch (Exception e) {
            e.printStackTrace();
            return new Test[]{};
        }
    }


    public void registration() {
        try {
            String result = (sendInquiry(apiRegistration, ""));
            JsonObject jsonReg =  new JsonParser().parse(result).getAsJsonObject();
            tokenConnection = jsonReg.get("token").getAsString();
            System.out.println(result);
        } catch (Exception | Error e) {
            e.printStackTrace();
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
        URL url = new URL(String.format(Server.url, api, json));
        HttpURLConnection connection = getResponseServer(url);
        String response = connectionResponseToString(connection);

        System.out.println("URL: " + url.toString());
        return response;
    }

    private HttpURLConnection getResponseServer(URL url) throws Exception {
        URLConnection urlConnection = url.openConnection();

        return (HttpURLConnection) urlConnection;
    }

    private String connectionResponseToString(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder urlString = new StringBuilder();

        String current;
        while ((current = in.readLine()) != null) {
            urlString.append(current);
        }

        connection.disconnect();

        return urlString.toString();
    }
}
