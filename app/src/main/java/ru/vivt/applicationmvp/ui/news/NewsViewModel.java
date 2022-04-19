package ru.vivt.applicationmvp.ui.news;

import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.Test;

public class NewsViewModel extends ViewModel {

    private MutableLiveData<String> newsLink;
    private MutableLiveData<ArrayAdapter<Test>> arrayAdapter = new MutableLiveData();


    public NewsViewModel() {
        newsLink = new MutableLiveData<>();
    }

    public LiveData<String> getNewsLink() {
        return newsLink;
    }

    public LiveData<ArrayAdapter<Test>> getArrayAdapter() {
        return arrayAdapter;
    }

    public void setArrayAdapter(ArrayAdapter<Test> arrayAdapter) {
        this.arrayAdapter.postValue(arrayAdapter);
    }
}