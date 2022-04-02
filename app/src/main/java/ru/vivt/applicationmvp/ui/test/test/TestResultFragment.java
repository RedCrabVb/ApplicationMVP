package ru.vivt.applicationmvp.ui.test.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
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

        binding.saveResultTest.setOnClickListener(v -> getActivity().finish());

        Gson gson = new Gson();

        ResultTest rt = gson.fromJson(getActivity().getIntent().getExtras().get("resultTest").toString(), ResultTest.class);


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        long timeLong = Long.parseLong(rt.getTime());
        long time = TimeUnit.MILLISECONDS.toMinutes(timeLong);
        long second = TimeUnit.MILLISECONDS.toSeconds(timeLong) % 60;

        try {

            requestQueue.add(Server.getInstance().saveResultTest(rt.getIdTest(),
                    "Время: " + time + " м. " + second + " c\n",
                    rt.getCountRightAnswer(),
                    rt.getAnswer().replaceAll("\\[|\\]", ""),
                    response -> System.out.println(response)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        binding.textViewResult.setText(gson.toJson(rt));



        binding.textViewResult.setText("Время: " + time + " м. " + second + " c\n"
                + "Количество верных ответов: " + rt.getCountRightAnswer() + "\n"
        );

        return root;
    }
}
