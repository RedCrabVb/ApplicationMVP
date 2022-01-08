package ru.vivt.applicationmvp.ui.test.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.vivt.applicationmvp.databinding.FragmentTestResultBinding;

public class TestResultFragment extends Fragment {
    private FragmentTestResultBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTestResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.saveResultTest.setOnClickListener(v -> {
            getActivity().finish();
        });

        binding.textViewResult.setText(getActivity().getIntent().getExtras().get("resultTest").toString());

        return root;
    }
}
