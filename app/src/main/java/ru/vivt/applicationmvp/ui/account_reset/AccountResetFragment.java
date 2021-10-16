package ru.vivt.applicationmvp.ui.account_reset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentAccountResetBinding;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;
import ru.vivt.applicationmvp.ui.repository.Server;

public class AccountResetFragment extends Fragment {

    private FragmentAccountResetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountResetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonReset.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    String email = binding.editTextTextEmailAddress.getText().toString();
                    if (email.length() < 4) {
                        binding.textView2.setText("Error input data");
                        return;
                    }

                    JsonObject json = new JsonParser().parse(Server.getInstance().resetPassword(email)).getAsJsonObject();
                    if (json.has(Server.error)) {
                        binding.textView2.setText(json.get(Server.error).getAsString());
                        return;
                    }

                    if (json.has(Server.status)) {
                        binding.textView2.setText(json.get(Server.status).getAsString());
                        Thread.sleep(2000);
                    }


                    ProfileFragment profileFragment = new ProfileFragment();

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main2, profileFragment);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });

        return root;
    }
}
