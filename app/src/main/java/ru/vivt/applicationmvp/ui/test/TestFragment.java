package ru.vivt.applicationmvp.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentTest2Binding;
import ru.vivt.applicationmvp.ui.repository.Question;
import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.Test;
import ru.vivt.applicationmvp.ui.repository.TestAdapter;

public class TestFragment extends Fragment {
    private FragmentTest2Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TestViewModel testViewModel = new ViewModelProvider(this).get(TestViewModel.class);

        binding = FragmentTest2Binding.inflate(inflater, container, false);

        View root = binding.getRoot();

        return root;
    }

}
