package ru.vivt.applicationmvp.ui.profile;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.vivt.applicationmvp.StartActivity;
import ru.vivt.applicationmvp.ui.repository.Server;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> username, email;
    private JsonObject json = null;

    private static final String usernameKey = "username", emailKey = "email";
    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();
        mText.setValue("This is profile fragment");

        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(Server.getGetFilesDir(), Server.fileNameConfig)))) {
            String str = inputStream.readUTF();
            json = new JsonParser().parse(str).getAsJsonObject();

            boolean conJson = json.has(usernameKey) && json.has(emailKey);
            if (conJson) {
                username.setValue(json.get(usernameKey).getAsString());
                email.setValue(json.get(emailKey).getAsString());
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public void putUsername(String username) {
        this.username.setValue(username);
    }

    public void setDataInMemory() {
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(new File(Server.getGetFilesDir(), Server.fileNameConfig)))){
            if (json.has(usernameKey)) {
                json.remove(usernameKey);
            }
            if (json.has(emailKey)) {
                json.remove(emailKey);
            }
            json.addProperty(usernameKey, username.getValue());
            json.addProperty(emailKey, email.getValue());
            outputStream.writeUTF(json.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}