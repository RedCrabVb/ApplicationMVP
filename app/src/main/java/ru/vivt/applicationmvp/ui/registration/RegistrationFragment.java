package ru.vivt.applicationmvp.ui.registration;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.function.Consumer;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentRegestrationBinding;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;
import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

import static ru.vivt.applicationmvp.ui.profile.ProfileFragment.keyUpdate;

public class RegistrationFragment extends Fragment {
    private FragmentRegestrationBinding binding;

    @SuppressLint("NewApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegestrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonRegistration.setOnClickListener(v -> {
            binding.textViewError.setVisibility(View.GONE);
            String editPass1 = binding.editTextTextPassword3.getText().toString();
            String editPass2 = binding.editTextTextPassword4.getText().toString();
            String username = binding.editTextTextPersonName3.getText().toString();
            String email = binding.editTextTextEmailAddress.getText().toString();

            Consumer<String> errorConsumer = (errorText) -> {
                binding.textViewError.setText(errorText);
                binding.textViewError.setVisibility(View.VISIBLE);
            };

            if (editPass1.isEmpty() || editPass2.isEmpty() || email.isEmpty() || username.isEmpty()) {
                errorConsumer.accept(getString(R.string.notEmpty));
                return;
            }

            if (!editPass1.equals(editPass2)) {
                errorConsumer.accept(getString(R.string.passwordIdentity));
                return;
            }


            if (email.length() < 4) {
                errorConsumer.accept(getString(R.string.mailMin));
                return;
            }

            if (editPass1.length() < 4) {
                errorConsumer.accept(getString(R.string.passwordMin));
                return;
            }

            try {
                new Thread(() -> {
                    try {
                        Server server = Server.getInstance();

                        String strResponse = server.updateDataAboutProfile(username, email, editPass1);
                        JsonObject json = new JsonParser().parse(strResponse).getAsJsonObject();

                        if (json.has(Server.error)) {
                            errorConsumer.accept(getString(R.string.error) + ": " + json.get(Server.error).getAsString());
                            throw new Exception(Server.error);
                        } else {
                            binding.textViewError.setText("Статус: " + json.get(Server.status).getAsString());
                            binding.textViewError.setTextColor(Color.GRAY);

                            MemoryValues memoryValues = MemoryValues.getInstance();
                            memoryValues.setEmail(email);
                            memoryValues.setUsername(username);
                        }
                        Bundle bundle = new Bundle();
                        ProfileFragment profileFragment = new ProfileFragment();
                        bundle.putBoolean(keyUpdate, true);
                        profileFragment.setArguments(bundle);

                        Thread.sleep(1000);

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment_activity_main2, profileFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.println(Log.ERROR, "error", e.getMessage());
                    }
                }).start();

            } catch (Exception e) {
                e.printStackTrace();
                Log.println(Log.ERROR, "error", e.getMessage());
            }
        });

        return root;
    }
}
