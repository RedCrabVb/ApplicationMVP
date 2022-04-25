package ru.vivt.applicationmvp.ui.repository;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MemoryValues {
    private static MemoryValues memoryValues;
    public static final String qrCode = "qrCode",
            fileNameConfig = "config.json",
            token = "token",
            username = "username",
            email = "email",
            resultLastTest = "resultLastTest";

    private final Context context;

    private File file;

    public MemoryValues(Context context) {
        this.context = context;
        file = new File(context.getCacheDir(), fileNameConfig);
    }

    public static MemoryValues getInstance() {
        return memoryValues;
    }

    public static MemoryValues init(Context context) {
        memoryValues = new MemoryValues(context);
        return getInstance();
    }


    public String getQrCode() {
        return getFiled(qrCode);
    }

    public String getToken() {
        return getFiled(token);
    }

    public String getUsername() {
        return getFiled(username);
    }

    public String getResultLastTest() {
        return getFiled(resultLastTest);
    }

    public String getEmail() {
        return getFiled(email);
    }

    public void resetData() {
        new File(context.getCacheDir(), fileNameConfig).delete();
    }


    public void setQrCode(String _qrCode) {
        setFiled(qrCode, _qrCode);
    }

    public void setResultLastTest(String _resultLastTest) {
        setFiled(resultLastTest, _resultLastTest);
    }

    public void setToken(String _token) {
        setFiled(token, _token);
    }

    public void setEmail(String _email) {
        setFiled(email, _email);
    }

    public void setUsername(String _username) {
        setFiled(username, _username);
    }

    private synchronized void setFiled(String nameFiled, String value) {
        JsonObject json = getFile();

        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file))) {
            json.addProperty(nameFiled, value);
            outputStream.writeUTF(json.toString());
        } catch (IOException | UnsupportedOperationException e) {
            e.printStackTrace();
            cleanFile();
        }
    }

    private synchronized String getFiled(String nameFiled) {
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(file))) {
            String str = inputStream.readUTF();
            JsonObject json = new JsonParser().parse(str).getAsJsonObject();
            return json.has(nameFiled) ? json.get(nameFiled).getAsString() : null;
        } catch (IOException | UnsupportedOperationException e) {
            cleanFile();
            e.printStackTrace();
            return null;
        }
    }

    private synchronized JsonObject getFile() {
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(file))) {
            if (inputStream.available() > 0) {
                String str = inputStream.readUTF();
                JsonObject json = new JsonParser().parse(str).getAsJsonObject();
                return json;
            } else {
                return new JsonObject();
            }
        } catch (IOException | UnsupportedOperationException e) {
            cleanFile();
            e.printStackTrace();
            return new JsonObject();
        }
    }

    private synchronized void cleanFile() {
        file.delete();
        try {
            if (file.createNewFile()) {
                try (DataOutputStream printWriter = new DataOutputStream(new FileOutputStream(file))) {
                    printWriter.writeUTF("{}");
                    printWriter.flush();
                }
                System.out.println(getFile());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
