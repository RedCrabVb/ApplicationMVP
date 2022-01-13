package ru.vivt.applicationmvp.ui.test.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

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

        long timeLong = Long.parseLong(rt.getTime());
        long time = TimeUnit.MILLISECONDS.toMinutes(timeLong);
        long second = TimeUnit.MILLISECONDS.toSeconds(timeLong) % 60;

        binding.textViewResult.setText("Время: " + time + " м. " + second + " м\n"
                + "Количество верных ответов: " + rt.getCountRightAnswer() + " с.\n"
        );

        return root;
    }
}
