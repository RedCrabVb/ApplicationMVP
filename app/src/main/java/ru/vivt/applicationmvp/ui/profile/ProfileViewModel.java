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
        this.username.postValue(username);
    }

    public void putEmail(String email) {
        this.email.postValue(email);
    }

}