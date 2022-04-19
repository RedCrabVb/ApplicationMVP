package ru.vivt.applicationmvp.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentNewsBinding;
import ru.vivt.applicationmvp.ui.repository.Question;
import ru.vivt.applicationmvp.ui.repository.Server;
import ru.vivt.applicationmvp.ui.repository.Test;
import ru.vivt.applicationmvp.ui.repository.TestAdapter;
import ru.vivt.applicationmvp.ui.test.TestActivity;

public class NewsFragment extends Fragment {

    private NewsViewModel dashboardViewModel;
    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(Server.getInstance().getTestServer(response -> {
            System.out.println(response);
            ArrayAdapter<Test> arrayAdapter = new TestAdapter(binding.getRoot().getContext(), R.layout.list_tests_header,
                    new ArrayList(Arrays.asList(Server.getInstance().getTest(response))));

            dashboardViewModel.setArrayAdapter(arrayAdapter);
        }));

        dashboardViewModel.getArrayAdapter().observe(getViewLifecycleOwner(), playerList -> {
            binding.listTest.setAdapter(playerList);
        });
        binding.listTest.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(binding.getRoot().getContext(), TestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle bundle = new Bundle();
            Test test = dashboardViewModel.getArrayAdapter().getValue().getItem(position);
            if (test.isActive()) {
                long idTest = test.getIdTest();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}