package ru.vivt.applicationmvp.ui.profile;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.MemoryValues;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> username, email;
    private MemoryValues memoryValues;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();
        mText.setValue("This is profile fragment");

        memoryValues = MemoryValues.getInstance();
        String _username = memoryValues.getUsername(), _email = memoryValues.getEmail();
        if (_username != null) {
            username.setValue(_username);
        }
        if (_email != null) {
            email.setValue(_email);
        }
    }

/*    public void getDataFromFile(File file) {
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(file, Server.fileNameConfig)))) {
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
            json = new JsonObject();
        }
    }*/

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
        memoryValues.setUsername(username);
        this.username.postValue(username);
    }

    public void putEmail(String email) {
        memoryValues.setEmail(email);
        this.email.postValue(email);
    }

/*    public void setDataInMemory(Context context) {
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(new File(context.getCacheDir(), Server.fileNameConfig)))){
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
    }*/
}