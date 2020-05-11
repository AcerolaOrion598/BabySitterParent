package com.djaphar.babysitterparent.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.djaphar.babysitterparent.Activities.AuthActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.LoginModel;
import com.djaphar.babysitterparent.ViewModels.AuthViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class RegisterFragment extends Fragment {

    private AuthViewModel authViewModel;
    private EditText regLoginEd, regPasswordEd, regRepeatPasswordEd;
    private Button registerBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        regLoginEd = root.findViewById(R.id.reg_login_ed);
        regPasswordEd = root.findViewById(R.id.reg_password_ed);
        regRepeatPasswordEd = root.findViewById(R.id.reg_repeat_password_ed);
        registerBtn = root.findViewById(R.id.register_btn);
        AuthActivity authActivity = (AuthActivity) getActivity();
        if (authActivity != null) {
            authActivity.setActionBarTitle(getString(R.string.register_title));
        }
        root.findViewById(R.id.login_nav_tv).setOnClickListener(lView -> {
            if (authActivity != null) {
                authActivity.navigate(R.id.navigation_login);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerBtn.setOnClickListener(lView -> authViewModel.requestRegistration(new LoginModel(regLoginEd.getText().toString(),
                regPasswordEd.getText().toString(), regRepeatPasswordEd.getText().toString())));
    }
}
