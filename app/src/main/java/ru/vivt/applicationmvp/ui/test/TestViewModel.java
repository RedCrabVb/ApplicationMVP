package ru.vivt.applicationmvp.ui.test;

import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.vivt.applicationmvp.ui.repository.Test;

public class TestViewModel extends ViewModel {

    private MutableLiveData<ArrayAdapter<Test>> arrayAdapter = new MutableLiveData();


    public LiveData<ArrayAdapter<Test>> getArrayAdapter() {
        return arrayAdapter;
    }

    public void setArrayAdapter(ArrayAdapter<Test> arrayAdapter) {
        this.arrayAdapter.postValue(arrayAdapter);
    }
}
