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
import ru.vivt.applicationmvp.databinding.FragmentTestBinding;
import ru.vivt.applicationmvp.ui.repository.Question;
import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.Test;
import ru.vivt.applicationmvp.ui.repository.TestAdapter;

public class TestFragment extends Fragment {
    private FragmentTestBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TestViewModel testViewModel = new ViewModelProvider(this).get(TestViewModel.class);

        binding = FragmentTestBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Server.getInstance().getTestServer(response -> {
            System.out.println(response);
            ArrayAdapter<Test> arrayAdapter = new TestAdapter(binding.getRoot().getContext(), R.layout.list_tests_header,
                    new ArrayList(Arrays.asList(Server.getInstance().getTest(response))));

            testViewModel.setArrayAdapter(arrayAdapter);
        }));

        testViewModel.getArrayAdapter().observe(getViewLifecycleOwner(), playerList -> {
            binding.listTest.setAdapter(playerList);
        });
        binding.listTest.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(binding.getRoot().getContext(), TestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle bundle = new Bundle();
            Test test = testViewModel.getArrayAdapter().getValue().getItem(position);
            if (test.isActive()) {
                long idTest = (long) test.getIdTest();
                requestQueue.add(Server.getInstance().getQuestionServer(idTest, response -> {
                    Question[] questions = Server.getInstance().getQuestion(response);
                    bundle.putString("questions", new Gson().toJson(questions));
                    bundle.putInt("idTest", (int) idTest);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }));
            }
        });

        return root;
    }

}
