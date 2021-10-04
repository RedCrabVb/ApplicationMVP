package ru.vivt.applicationmvp.ui.profile;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentProfileBinding;
import ru.vivt.applicationmvp.ui.account_reset.AccountResetFragment;
import ru.vivt.applicationmvp.ui.authorization.AuthorizationFragment;
import ru.vivt.applicationmvp.ui.home.HomeFragment;
import ru.vivt.applicationmvp.ui.registration.RegistrationFragment;
import ru.vivt.applicationmvp.ui.repository.Server;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        binding.buttonDataReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(binding.getRoot().getContext().getFilesDir(), "config.json").delete();
                System.exit(0);
            }
        });

        profileViewModel.getDataFromFile(binding.getRoot().getContext().getCacheDir());
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.textViewEmail.setText(s);
                profileViewModel.setDataInMemory(binding.getRoot().getContext());
            }
        });
        profileViewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.textViewUsername.setText(s);
                profileViewModel.setDataInMemory(binding.getRoot().getContext());
            }
        });


        binding.buttonAutrization.setOnClickListener(this);
        binding.buttonRegestration.setOnClickListener(this);
        binding.buttonResetPassword.setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("token")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JsonObject json = new JsonParser().parse(
                                    Server.getInstance()
                                            .getApiPersonData(bundle.get(Server.token).toString()))
                                    .getAsJsonObject();
                            String email = json.get("email").getAsString();
                            String username = json.get("username").getAsString();

                            profileViewModel.putEmail(email);
                            profileViewModel.putUsername(username);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAutrization:
                replaceFragment(new AuthorizationFragment());
                break;
            case R.id.buttonRegestration:
                replaceFragment(new RegistrationFragment());
                break;
            case R.id.buttonResetPassword:
                replaceFragment(new AccountResetFragment());
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main2, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}