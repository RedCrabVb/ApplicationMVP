package ru.vivt.applicationmvp.ui.registration;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.AsyncTaskLoader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kotlin.Experimental;
import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentAutrizationBinding;
import ru.vivt.applicationmvp.databinding.FragmentRegestrationBinding;
import ru.vivt.applicationmvp.ui.home.HomeViewModel;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;
import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.repository.Server;

public class RegistrationFragment extends Fragment {
    private FragmentRegestrationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegestrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonRegistration.setOnClickListener(v -> {
            binding.textViewError.setVisibility(View.GONE);
            String editPass1 = binding.editTextTextPassword3.getText().toString();
            String editPass2 = binding.editTextTextPassword4.getText().toString();

            if (!editPass1.equals(editPass2)) {
                binding.textViewError.setText("Password incorrect");
                binding.textViewError.setVisibility(View.VISIBLE);
                return;
            }

            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Server server = Server.getInstance();
                            String username = binding.editTextTextPersonName3.getText().toString();
                            String email = binding.editTextTextEmailAddress.getText().toString();

                            String strResponse = server.setData(username, email, editPass1);
                            JsonObject json = new JsonParser().parse(strResponse).getAsJsonObject();

                            if (json.has("error")) {
                                binding.textViewError.setText(json.get("error").getAsString());
                                binding.textViewError.setTextColor(Color.RED);
                                throw new Exception("error");
                            } else {
                                binding.textViewError.setText(json.get("status").getAsString());
                                binding.textViewError.setTextColor(Color.GRAY);

                                MemoryValues memoryValues = MemoryValues.getInstance();
                                memoryValues.setEmail(email);
                                memoryValues.setUsername(username);
                           }
                            Bundle bundle = new Bundle();
                            ProfileFragment profileFragment = new ProfileFragment();
                            bundle.putBoolean("update", true);
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
