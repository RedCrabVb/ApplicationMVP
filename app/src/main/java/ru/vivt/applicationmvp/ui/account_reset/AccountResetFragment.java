package ru.vivt.applicationmvp.ui.account_reset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentAccountResetBinding;
import ru.vivt.applicationmvp.ui.profile.ProfileFragment;

public class AccountResetFragment extends Fragment {

    private FragmentAccountResetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountResetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_activity_main2, profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}
