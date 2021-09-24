package ru.vivt.applicationmvp.ui.profile;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentProfileBinding;
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

        binding.buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textViewError.setVisibility(View.GONE);
                String editPass1 = binding.editTextTextPassword.getText().toString();
                String editPass2 = binding.editTextTextPassword2.getText().toString();
                if (!editPass1.equals(editPass2)) {
                    binding.textViewError.setText("Password incorrect");
                    binding.textViewError.setVisibility(View.VISIBLE);
                    return;
                }

                SetDataInUI setDataInUI = new SetDataInUI(editPass1, binding.editTextTextPersonName.getText().toString(), binding.editTextTextPersonEmail.getText().toString());
                setDataInUI.execute("");
            }
        });

        binding.textViewError.setVisibility(View.GONE);

        profileViewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.editTextTextPersonName.setText(s);
            }
        });

        profileViewModel.getEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.editTextTextPersonEmail.setText(s);
            }
        });

        binding.buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    String strResponse = Server.getInstance().resetPassword(profileViewModel.getEmail().getValue());
                    binding.textViewError.setText(strResponse);
                    binding.textViewError.setVisibility(View.VISIBLE);
            }).start();
            }
        });


        binding.buttonAutrization.setOnClickListener(this);
        binding.buttonRegestration.setOnClickListener(this);

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
            case R.id.buttonResetPassword:
                replaceFragment(new ProfileFragment());
                break;
            case R.id.buttonRegestration:
                replaceFragment(new RegistrationFragment());
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main2, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    class SetDataInUI extends AsyncTask<String, Void, String> {
        private String status;
        private String editPass1;
        private String username;
        private String email;

        public SetDataInUI(String editPass1, String username, String email) {
            this.editPass1 = editPass1;
            this.username = username;
            this.email = email;
        }

        @Override
        protected String doInBackground(String... voids) {
            try {
                status = Server.getInstance().setData(
                        username,
                        email,
                        editPass1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            profileViewModel.putUsername(username);
            profileViewModel.putEmail(email);
            profileViewModel.setDataInMemory(binding.getRoot().getContext());
            binding.textViewError.setTextColor(Color.GRAY);
            binding.textViewError.setText("Data send to server, status: " + status);
            binding.textViewError.setVisibility(View.VISIBLE);
            super.onPostExecute(s);


        }
    }
}