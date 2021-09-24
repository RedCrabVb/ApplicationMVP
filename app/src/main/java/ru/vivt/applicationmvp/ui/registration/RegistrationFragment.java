package ru.vivt.applicationmvp.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentAutrizationBinding;
import ru.vivt.applicationmvp.databinding.FragmentRegestrationBinding;
import ru.vivt.applicationmvp.ui.home.HomeViewModel;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;

public class RegistrationFragment extends Fragment {
    private FragmentRegestrationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegestrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main2, new ProfileFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}
