package ru.vivt.applicationmvp.ui.profile;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.MemoryValues;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> username, email;
    private MemoryValues memoryValues;

    public ProfileViewModel() {
        username = new MutableLiveData<>();
        email = new MutableLiveData<>();

        memoryValues = MemoryValues.getInstance();
        String _username = memoryValues.getUsername(), _email = memoryValues.getEmail();
        if (_username != null) {
            username.setValue(_username);
        }
        if (_email != null) {
            email.setValue(_email);
        }
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