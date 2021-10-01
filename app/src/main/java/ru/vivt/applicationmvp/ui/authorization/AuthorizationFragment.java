package ru.vivt.applicationmvp.ui.authorization;

import android.os.Bundle;
import android.util.JsonReader;
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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentAutrizationBinding;
import ru.vivt.applicationmvp.databinding.FragmentHomeBinding;
import ru.vivt.applicationmvp.ui.home.HomeViewModel;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;
import ru.vivt.applicationmvp.ui.repository.Server;

public class AuthorizationFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentAutrizationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentAutrizationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonAuthorization.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    String email = binding.editTextTextEmailAddress.getText().toString();
                    String password = binding.editTextTextPassword.getText().toString();

                    JsonObject json = new JsonParser().parse(Server.getInstance().authorization(email, password)).getAsJsonObject();
                    if (json.has("error")) {
                        binding.textView2.setText(json.get("error").getAsString());
                        binding.textView2.setVisibility(View.VISIBLE);
                        return;
                    }
                    String token = json.get("token").getAsString();

                    Bundle bundle = new Bundle();
                    bundle.putString("token", token);
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_activity_main2, profileFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });

        return root;
    }
}
