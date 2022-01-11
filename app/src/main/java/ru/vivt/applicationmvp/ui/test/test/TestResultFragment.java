package ru.vivt.applicationmvp.ui.test.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import javax.xml.transform.Result;

import ru.vivt.applicationmvp.databinding.FragmentTestResultBinding;
import ru.vivt.applicationmvp.ui.repository.ResultTest;
import ru.vivt.applicationmvp.ui.repository.Server;

public class TestResultFragment extends Fragment {
    private FragmentTestResultBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTestResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.saveResultTest.setOnClickListener(v -> {
            getActivity().finish();
        });

        Gson gson = new Gson();

        ResultTest rt = gson.fromJson(getActivity().getIntent().getExtras().get("resultTest").toString(), ResultTest.class);


        new Thread(() -> {
            try {
                Server.getInstance().saveResultTest(rt.getIdTest(),
                        rt.getTime(),
                        rt.getCountRightAnswer(),
                        rt.getAnswer().replaceAll("\\[|\\]", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ).start();

        binding.textViewResult.setText(gson.toJson(rt));

        binding.textViewResult.setText("Время: " + rt.getTime() + " м\n"
                + "Количество верных ответов: " + rt.getCountRightAnswer() + "\n"
        );

        return root;
    }
}
