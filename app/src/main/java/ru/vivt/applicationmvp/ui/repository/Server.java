package ru.vivt.applicationmvp.ui.repository;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static Server server;

    private static String url = "";
    private static final String apiNews = "api/news";
    private static final String apiTestAll = "api/testAll";
    private static final String apiTestCurrent = "api/test";
    private static final String apiQrCode = "api/qrCode";
    private static final String apiPersonData = "api/setPersonData";
    private static final String apiRegistration = "api/registration";
    private static final String apiStatusToken = "api/getStatusToken";
    private static final String apiResetPassword = "api/resetPassword/email";
    private static final String apiAuthorization = "api/authorization";
    private static final String apiPersonDataGet = "api/personData";
    public static final String apiSaveResultTest = "api/saveResultTest";

    private String tokenConnection = null;

    private Server() {

    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public static Server getInstance(String ip) {
        url = "http://" + ip + "/%s?%s";
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public static String getUrl(String api, String param) {
        return String.format(Server.url, api, param);
    }

    public boolean tokenActive() {
        return true;
    }

    public JsonObjectRequest resetPassword(String email,
                                           Response.Listener<JSONObject> response) {
        return new JsonObjectRequest(
                Request.Method.POST,
                getUrl(apiResetPassword, "email=" + email),
                null,
                response,
                error -> {
                    System.out.println("error when reset password");
                }
        );
    }

    public String getTokenConnection() {
        return tokenConnection;
    }

    public JsonObjectRequest updateDataAboutProfile(String userName, String email, String password,
                                                    Response.Listener<JSONObject> response,
                                                    Response.ErrorListener errorListener) {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("token", tokenConnection);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JsonObjectRequest(Request.Method.POST,
                getUrl(apiPersonData, String.format("username=%s&email=%s&password=%s", userName, email, password)),
                jsonRequest,
                response,
                errorListener
        );
    }

    public JsonObjectRequest authorization(String email, String password,
                                           Response.Listener<JSONObject> response,
                                           Response.ErrorListener errorListener) {
        return new JsonObjectRequest(Request.Method.POST,
                getUrl(apiAuthorization, String.format("email=%s&password=%s", email, password)),
                null,
                response,
                errorListener
        );
    }

    public JsonArrayRequest getTestServer(Response.Listener<JSONArray> response) {
        return new JsonArrayRequest(Request.Method.GET,
                getUrl(apiTestAll, ""),
                null,
                response,
                errorListener -> System.out.println("error getTestServer: " + errorListener)
        );
    }

    public JsonObjectRequest getQuestionServer(Long id, Response.Listener<JSONObject> response) {
        return new JsonObjectRequest(Request.Method.GET,
                getUrl(apiTestCurrent, "id=" + id),
                null,
                response,
                errorListener -> System.out.println("error question" + errorListener.networkResponse)
        );
    }

    public Question[] getQuestion(JSONObject questionJSON) {
        try {
            Gson gson = new Gson();
            Question[] question;
            JsonArray jsonArrayQuestion = new JsonParser().parse(questionJSON.getJSONArray("answerList").toString()).getAsJsonArray();
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

    public Test[] getTest(JSONArray test) {
        try {
            ArrayList<Test> tests = new ArrayList<>();
            JsonArray jsonArrayTest = new JsonParser().parse(test.toString())
                    .getAsJsonArray();
            for (JsonElement r : jsonArrayTest) {
                JsonObject j = r.getAsJsonObject();
                if (j.get("active").getAsBoolean()) {
                    tests.add(new Test(
                            j.get("idTest").getAsInt(),
                            j.get("test").getAsString(),
                            j.get("description").getAsString(),
                            true));
                }
            }
            Test[] testArr = new Test[tests.size()];
            for (int i = 0; i < tests.size(); i++) {
                testArr[i] = tests.get(i);
            }
            return testArr;
        } catch (Exception e) {
            e.printStackTrace();
            return new Test[]{};
        }
    }

    public StringRequest saveResultTest(int idTest,
                                            String time,
                                            String countWrongAnswer,
                                            Response.Listener<String> response, Response.ErrorListener errorListener) {
        return new StringRequest(Request.Method.GET,
                getUrl(apiSaveResultTest,
                        String.format("token=%s&time=%s&idTest=%s&countRightAnswer=%s",
                                tokenConnection, time, idTest, countWrongAnswer)),
                response,
                errorListener
        );
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

    public JsonObjectRequest getQrCode(Response.Listener<JSONObject> response) {
        return new JsonObjectRequest(
                Request.Method.POST,
                getUrl(apiQrCode, "token=" + tokenConnection),
                null,
                response,
                error -> {
                    System.out.println("error when getQrCode");
                }
        );
    }

    public JsonObjectRequest getNewsRequest(Response.Listener<JSONObject> response, Response.ErrorListener error) {
        return new JsonObjectRequest(
                Request.Method.GET,
                getUrl(apiNews, ""),
                null,
                response,
                error
        );
    }

    public void setTokenConnection(String tokenConnection) {
        this.tokenConnection = tokenConnection;
    }

    public void saveDataInMemory(MemoryValues memoryValues) {
        memoryValues.setToken(tokenConnection);
    }

    public void saveDataInMemory(MemoryValues memoryValues, JSONObject json) {
        try {
            server.setTokenConnection(json.getString(MemoryValues.token));

            memoryValues.setToken(tokenConnection);
            memoryValues.setEmail(json.getString(MemoryValues.email));
            memoryValues.setUsername(json.getString(MemoryValues.username));
            memoryValues.setQrCode(json.getString(MemoryValues.qrCode));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
