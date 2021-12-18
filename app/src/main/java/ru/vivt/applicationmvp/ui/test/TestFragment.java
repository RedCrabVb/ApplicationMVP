package ru.vivt.applicationmvp.ui.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentTestBinding;
import ru.vivt.applicationmvp.ui.profile.ProfileViewModel;
import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.Test;
import ru.vivt.applicationmvp.ui.repository.TestAdapter;

public class TestFragment extends Fragment {
    private FragmentTestBinding binding;
    private TestViewModel testViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TestViewModel testViewModel = new ViewModelProvider(this).get(TestViewModel.class);

        binding = FragmentTestBinding.inflate(inflater, container, false);

        View root = binding.getRoot();


        new Thread(() -> {
            try {
                ArrayAdapter<Test> arrayAdapter = new TestAdapter(binding.getRoot().getContext(), R.layout.list_tests,
                        new ArrayList(Arrays.asList(Server.getInstance().getTest())));

                testViewModel.setArrayAdapter(arrayAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        testViewModel.getArrayAdapter().observe(getViewLifecycleOwner(), playerList -> {
            binding.listTest.setAdapter(playerList);
        });

        return root;
    }

}
