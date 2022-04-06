package ru.vivt.applicationmvp.ui.registration;

import static ru.vivt.applicationmvp.ui.profile.ProfileFragment.keyUpdate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

            CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    Bundle bundle = new Bundle();
                    ProfileFragment profileFragment = new ProfileFragment();
                    bundle.putBoolean(keyUpdate, true);
                    profileFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main2, profileFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            };

            if (!checkField(editPass1, editPass2, username, email, errorConsumer)) {
                return;
            }

            Server server = Server.getInstance();

            JsonObjectRequest j = server.updateDataAboutProfile(username, email, editPass1, response -> {
                binding.textViewError.setText("Успешное обновление данных");
                binding.textViewError.setTextColor(Color.GRAY);

                MemoryValues memoryValues = MemoryValues.getInstance();
                server.saveDataInMemory(memoryValues, response);

                countDownTimer.start();
            }, error -> errorConsumer.accept(getString(R.string.error)));

            requestQueue.add(j);

        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean checkField(String editPass1, String editPass2, String username, String email, Consumer<String> errorConsumer) {


        if (editPass1.isEmpty() || editPass2.isEmpty() || email.isEmpty() || username.isEmpty()) {
            errorConsumer.accept(getString(R.string.notEmpty));
            return false;
        }

        if (!editPass1.equals(editPass2)) {
            errorConsumer.accept(getString(R.string.passwordIdentity));
            return false;
        }


        if (email.length() < 4) {
            errorConsumer.accept(getString(R.string.mailMin));
            return false;
        }

        if (editPass1.length() < 4) {
            errorConsumer.accept(getString(R.string.passwordMin));
            return false;
        }

        return true;
    }
}
