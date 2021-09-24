package ru.vivt.applicationmvp.ui.authorization;

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

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentAutrizationBinding;
import ru.vivt.applicationmvp.databinding.FragmentHomeBinding;
import ru.vivt.applicationmvp.ui.home.HomeViewModel;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;

public class AuthorizationFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentAutrizationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentAutrizationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonAuthorization.setOnClickListener(new View.OnClickListener() {
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
