package ru.vivt.applicationmvp.ui.test.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ru.vivt.applicationmvp.databinding.FragmentTestResultBinding;
import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.ResultTest;
import ru.vivt.applicationmvp.ui.repository.Server;

public class TestResultFragment extends Fragment {
    private FragmentTestResultBinding binding;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTestResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Gson gson = new Gson();

        ResultTest rt = gson.fromJson(getActivity().getIntent()
                .getExtras().get("resultTest").toString(), ResultTest.class);


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        long timeLong = Long.parseLong(rt.getTime());
        long time = TimeUnit.MILLISECONDS.toMinutes(timeLong);
        long second = TimeUnit.MILLISECONDS.toSeconds(timeLong) % 60;

        StringRequest request = Server.getInstance().saveResultTest(rt.getIdTest(),
                timeLong + "",
                rt.getCountWrongAnswer() + "",
                response -> this.getActivity().runOnUiThread(() -> {
                    binding.saveResultTest.setOnClickListener(v -> getActivity().finish());
                    binding.saveResultTest.setEnabled(true);
                }),
                responseError -> this.getActivity().runOnUiThread(() -> {
                    String error;
                    if (responseError.networkResponse != null) {
                        error = "Код ошибки: " + responseError.networkResponse.statusCode;
                    } else {
                        error = "Нет сети";
                        MemoryValues.getInstance().setResultLastTest(String.format("token=%s&time=%s&idTest=%s&countRightAnswer=%s",
                                Server.getInstance().getTokenConnection(), timeLong + "", rt.getIdTest(), rt.getCountWrongAnswer() + ""));
                    }
                    binding.textViewError.setText("Ошибка при сохранении данных. Попробуйте сохранить ещё раз. " + error + " \nmsg: " + responseError.getMessage());
                    binding.textViewError.setVisibility(View.VISIBLE);
                    binding.saveResultTest.setEnabled(true);

                }));

        Runnable runnable = () -> requestQueue.add(request);


        runnable.run();

        binding.textViewResult.setText(gson.toJson(rt));


        binding.textViewResult.setText("Время: " + time + " мин. " + second + " c.\n"
                + "Количество вопросов: " + rt.getCountWrongAnswer() + " \n"
                + "Количество неверных ответов: " + rt.getCountWrongAnswer() + "\n"
        );

        binding.saveResultTest.setEnabled(false);
        binding.saveResultTest.setOnClickListener(v -> {
            binding.saveResultTest.setEnabled(false);
            runnable.run();
        });

        return root;
    }
}
