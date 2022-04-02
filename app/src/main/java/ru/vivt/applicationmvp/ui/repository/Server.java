package ru.vivt.applicationmvp.ui.repository;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicInteger;

import kotlin.RequiresOptIn;

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
    private static final String apiSaveResultTest = "api/saveResultTest";

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

    private static String getUrl(String api, String param) {
        return String.format(Server.url, api, param);
    }

    public boolean tokenActive() {
        return true;
//        try {
//            return new JsonParser().parse(sendInquiry(apiStatusToken, String.format("token=%s", tokenConnection)))
//                    .getAsJsonObject().get("result").getAsBoolean();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    public JsonObject getApiPersonData() throws Exception {
//        return new JsonParser().parse(sendInquiry(apiPersonDataGet, String.format("token=%s", tokenConnection))).getAsJsonObject();
        return null;
    }

    public String resetPassword(String email) {
        return null;
//        String result = "error";
//        try {
//            result = sendInquiry(apiResetPassword, String.format("email=%s", email));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
    }

    public String getNews() {
//        try {
//            return Server.getInstance().getNewsJson().get("News").getAsString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "https://habr.com/ru/news/";
//        }
        return "https://habr.com/ru/news/";
    }

    public String updateDataAboutProfile(String userName, String email, String password) throws Exception {
//        String result = sendInquiry(apiPersonData, String.format("token=%s&username=%s&email=%s&password=%s", tokenConnection, userName, email, password));
//        System.out.println(result);
//        return result;
        return null;
    }

    public String authorization(String email, String password) throws Exception {
//        return sendInquiry(apiAuthorization, String.format("email=%s&password=%s", email, password));
        return null;
    }

    private JsonObject getTestServer() throws Exception {
//        JsonObject test = new JsonParser().parse(sendInquiry(apiTestAll, "")).getAsJsonObject();
//        System.out.println(test);
//        return test;
        return null;
    }

    private JsonObject getQuestionServer(int id) throws Exception {
//        JsonObject test = new JsonParser().parse(sendInquiry(apiTestCurrent, String.format("id=%d", id))).getAsJsonObject();
//        System.out.println(test);
//        return test;
        return null;
    }

    public Question[] getQuestion(int id) {
        try {
            Gson gson = new Gson();
            Question[] question;
            JsonArray jsonArrayQuestion = getQuestionServer(id).getAsJsonArray("question");
            question = new Question[jsonArrayQuestion.size()];
            AtomicInteger i = new AtomicInteger();
            for (JsonElement r : jsonArrayQuestion) {
                JsonObject jsonQuestion = r.getAsJsonObject();
                question[i.getAndIncrement()] = gson.fromJson(jsonQuestion, Question.class);
            }
            return question;
        } catch (Exception e) {
            e.printStackTrace();
            return new Question[]{};
        }
    }

    public Test[] getTest() {
        try {
            Test[] news;
            JsonArray jsonArrayTest = getTestServer().getAsJsonArray("test");
            news = new Test[jsonArrayTest.size()];
            AtomicInteger i = new AtomicInteger();
            for (JsonElement r : jsonArrayTest) {
                JsonObject jsonNews = r.getAsJsonObject();
                news[i.getAndIncrement()] = new Test(
                        jsonNews.get("idTest").getAsInt(),
                        jsonNews.get("test").getAsString(),
                        jsonNews.get("description").getAsString()
                );
            }
            return news;
        } catch (Exception e) {
            e.printStackTrace();
            return new Test[]{};
        }
    }

    public JsonObject saveResultTest(int idTest,
                                     String time,
                                     String countRightAnswer,
                                     String answerJson) throws Exception {
//        return new JsonParser().parse(sendInquiry(apiSaveResultTest,
//                String.format("token=%s&time=%s&idTest=%s&countRightAnswer=%s&jsonAnswer=%s", this.tokenConnection, time, "" + idTest, countRightAnswer, answerJson)
//        )).getAsJsonObject();
        return null;
    }


    public void registration() {
//        try {
//            String result = (sendInquiry(apiRegistration, ""));
//            JsonObject jsonReg = new JsonParser().parse(result).getAsJsonObject();
//            tokenConnection = jsonReg.get("token").getAsString();
//            System.out.println(result);
//        } catch (Exception | Error e) {
//            e.printStackTrace();
//        }
        tokenConnection = "test";
    }


    public JsonObjectRequest registration2(Response.Listener<JSONObject> response) {
        return new JsonObjectRequest(
                Request.Method.POST,
                getUrl(apiRegistration, ""),
                null,
                response,
                error -> {
                    System.out.println("error when registration2");
                }
        );
    }

    private String getQrCode() {
        return "test";
//        try {
//            String result = sendInquiry(apiQrCode, String.format("token=%s", tokenConnection));
//            JsonObject json = new JsonParser().parse(result).getAsJsonObject();
//            return json.get("qrCode").getAsString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error";
//        }
    }

    public JsonObjectRequest getNewsRequest(Response.Listener<JSONObject> response) throws Exception {
        JsonObjectRequest j = new JsonObjectRequest(
                Request.Method.GET,
                getUrl(apiNews, ""),
                null,
                response,
                error -> {
                    System.out.println("error when get news");;
                }
        );
        return j;
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

//    private String sendInquiry(String api, String json) throws Exception {
//        json = json.replace("+", "%20"); // fix space encoder
////        String jsonEncoding = URLEncoder.encode(json, "UTF-8");
//        URL url = new URL(String.format(Server.url, api, json));
//        HttpURLConnection connection = getResponseServer(url);
//        String response = connectionResponseToString(connection);
//
//        System.out.println("URL: " + url.toString());
//        return response;
//    }
//
//    private HttpURLConnection getResponseServer(URL url) throws Exception {
//        URLConnection urlConnection = url.openConnection();
//
//        return (HttpURLConnection) urlConnection;
//    }
//
//    private String connectionResponseToString(HttpURLConnection connection) throws IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        StringBuilder urlString = new StringBuilder();
//
//        String current;
//        while ((current = in.readLine()) != null) {
//            urlString.append(current);
//        }
//
//        connection.disconnect();
//
//        return urlString.toString();
//    }
}
