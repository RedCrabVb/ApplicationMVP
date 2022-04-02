package ru.vivt.applicationmvp.ui.authorization;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.util.function.Consumer;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentAutrizationBinding;
import ru.vivt.applicationmvp.databinding.FragmentHomeBinding;
import ru.vivt.applicationmvp.ui.home.HomeViewModel;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;
import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class AuthorizationFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentAutrizationBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentAutrizationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        binding.buttonAuthorization.setOnClickListener(v -> {
            Server server = Server.getInstance();
            String email = binding.editTextTextEmailAddress.getText().toString();
            String password = binding.editTextTextPassword.getText().toString();

            Consumer<String> errorConsumer = (errorText) -> {
                binding.textError.setText(errorText);
                binding.textError.setVisibility(View.VISIBLE);
            };

            if (password.isEmpty() || email.isEmpty()) {
                errorConsumer.accept(getString(R.string.notEmpty));
                return;
            }


            if (email.length() < 4) {
                errorConsumer.accept(getString(R.string.mailMin));
                return;
            }

            if (password.length() < 4) {
                errorConsumer.accept(getString(R.string.passwordMin));
                return;
            }


            requestQueue.add(server.authorization(email, password, response -> {
                try {
                    binding.textError.setText("Вы авторизованы, подождите");
                    binding.textError.setTextColor(Color.GRAY);

                    String token = response.getString("token");
                    MemoryValues memoryValues = MemoryValues.getInstance();
                    server.setTokenConnection(token);
                    server.saveDataInMemory(memoryValues, response);

                    Thread.sleep(2000);

                    ProfileFragment profileFragment = new ProfileFragment();

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main2, profileFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, error -> {
                binding.textError.setText("Ошибка на сервере");
            }));
        });


        return root;
    }
}
