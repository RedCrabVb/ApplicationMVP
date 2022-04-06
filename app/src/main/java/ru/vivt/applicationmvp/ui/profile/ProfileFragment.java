package ru.vivt.applicationmvp.ui.profile;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import ru.vivt.applicationmvp.R;
import ru.vivt.applicationmvp.databinding.FragmentProfileBinding;
import ru.vivt.applicationmvp.ui.account_reset.AccountResetFragment;
import ru.vivt.applicationmvp.ui.authorization.AuthorizationFragment;
import ru.vivt.applicationmvp.ui.registration.RegistrationFragment;
import ru.vivt.applicationmvp.ui.repository.MemoryValues;
import ru.vivt.applicationmvp.ui.test.TestFragment;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    public static String keyUpdate = "update";

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonDataReset.setOnClickListener(v -> {
            MemoryValues.getInstance().resetData();
            System.exit(0);
        });

        profileViewModel.getEmail().observe(getViewLifecycleOwner(), s -> binding.textViewEmail.setText(s));
        profileViewModel.getUsername().observe(getViewLifecycleOwner(), s -> binding.textViewUsername.setText(s));


        binding.buttonAutrization.setOnClickListener(this);
        binding.buttonRegestration.setOnClickListener(this);
        binding.buttonResetPassword.setOnClickListener(this);
        binding.buttonGoTest.setOnClickListener(this);

        MemoryValues memoryValues = MemoryValues.getInstance();


        if (!memoryValues.getEmail().isEmpty()) {
            binding.buttonRegestration.setVisibility(GONE);
            binding.buttonDataReset.setVisibility(View.VISIBLE);
        } else {
            binding.buttonDataReset.setVisibility(GONE);
        }

        if (memoryValues.getUsername().isEmpty()) {
            binding.textViewUsername.setVisibility(GONE);
            binding.textViewEmail.setVisibility(GONE);
            binding.textNotifications.setText("Нет данных о профиле");
        }

        profileViewModel.putUsername(memoryValues.getUsername());
        profileViewModel.putEmail(memoryValues.getEmail());

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(keyUpdate)) {
                profileViewModel.putUsername(memoryValues.getUsername());
                profileViewModel.putEmail(memoryValues.getEmail());
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAutrization:
                replaceFragment(new AuthorizationFragment());
                break;
            case R.id.buttonRegestration:
                replaceFragment(new RegistrationFragment());
                break;
            case R.id.buttonResetPassword:
                replaceFragment(new AccountResetFragment());
                break;
            case R.id.buttonGoTest:
                replaceFragment(new TestFragment());
                break;
            default:
                System.out.println("Not found R.id (ProfileFragment)");
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main2, someFragment);
        transaction.commit();
    }

}