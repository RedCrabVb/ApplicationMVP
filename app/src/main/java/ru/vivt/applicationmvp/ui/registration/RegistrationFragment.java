package ru.vivt.applicationmvp.ui.registration;

import static ru.vivt.applicationmvp.ui.profile.ProfileFragment.keyUpdate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.function.Consumer;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentRegestrationBinding;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;
import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class RegistrationFragment extends Fragment {
    private FragmentRegestrationBinding binding;

    @SuppressLint({"NewApi", "SetTextI18n"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegestrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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

            Server server = Server.getInstance();

            Runnable r = () -> {
                JsonObjectRequest j = null;
                try {
                    j = server.updateDataAboutProfile(username, email, editPass1, response -> {
                        try {
                            binding.textViewError.setText("Успешное обновление данных");
                            binding.textViewError.setTextColor(Color.GRAY);

                            MemoryValues memoryValues = MemoryValues.getInstance();
                            server.saveDataInMemory(memoryValues, response);

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
                        }
                    }, error -> errorConsumer.accept(getString(R.string.error)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                requestQueue.add(j);

            };
            new Thread(r).start();

        });
        return root;
    }
}
