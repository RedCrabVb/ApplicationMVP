package ru.vivt.applicationmvp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentRegestrationBinding;
import ru.vivt.applicationmvp.databinding.FragmentScannerQrBinding;

public class ScannerQrFragment extends Fragment {
    private FragmentScannerQrBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScannerQrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}