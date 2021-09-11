package ru.vivt.applicationmvp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import ru.vivt.applicationmvp.databinding.FragmentProfileBinding;
import ru.vivt.applicationmvp.ui.repository.Server;

public class ProfileFragment extends Fragment {

    private ProfileViewModel notificationsViewModel;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        binding.buttonDataReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(binding.getRoot().getContext().getFilesDir(), "config.json").delete();
            }
        });

        binding.buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editPass1 = binding.editTextTextPassword.getText().toString();
                String editPass2 = binding.editTextTextPassword2.getText().toString();
                if (!editPass1.equals(editPass2)) {
                    binding.textViewError.setText("Password incorrect");
                    return;
                }

                new Thread(() -> {
                    try {
                        Server.getInstance().setData(
                                binding.editTextTextPersonName.getText().toString(),
                                binding.editTextTextPersonEmail.getText().toString(),
                                editPass1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
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