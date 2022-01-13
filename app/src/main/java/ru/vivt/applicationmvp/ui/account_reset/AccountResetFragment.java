package ru.vivt.applicationmvp.ui.account_reset;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.function.Consumer;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentAccountResetBinding;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;
import ru.vivt.applicationmvp.ui.repository.Server;

public class AccountResetFragment extends Fragment {

    private FragmentAccountResetBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountResetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonReset.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    Consumer<String> errorConsumer = (errorText) -> {
                        binding.textError.setText(errorText);
                    };

                    String email = binding.editTextTextEmailAddress.getText().toString();
                    if (email.isEmpty()) {
                        errorConsumer.accept(getString(R.string.notEmpty));
                    }

                    if (email.length() < 4) {
                        errorConsumer.accept(getString(R.string.mailMin));
                        return;
                    }


                    JsonObject json = new JsonParser().parse(Server.getInstance().resetPassword(email)).getAsJsonObject();
                    if (json.has(Server.error)) {
                        errorConsumer.accept(getString(R.string.error) + ": " + json.get(Server.error).getAsString());
                        return;
                    }

                    if (json.has(Server.status)) {
                        errorConsumer.accept("Статус: " + json.get(Server.status).getAsString());
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
