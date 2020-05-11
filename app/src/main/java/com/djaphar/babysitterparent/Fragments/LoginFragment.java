package com.djaphar.babysitterparent.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.djaphar.babysitterparent.Activities.AuthActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.ViewModels.AuthViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class LoginFragment extends Fragment implements TextWatcher {

    private AuthViewModel authViewModel;
    private EditText loginEd, passwordEd;
    private Button loginBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        loginEd = root.findViewById(R.id.login_ed);
        passwordEd = root.findViewById(R.id.password_ed);
        loginBtn = root.findViewById(R.id.login_btn);
        AuthActivity authActivity = (AuthActivity) getActivity();
        if (authActivity != null) {
            authActivity.setActionBarTitle(getString(R.string.login_title));
        }
        root.findViewById(R.id.register_nav_tv).setOnClickListener(lView -> {
            if (authActivity != null) {
                authActivity.navigate(R.id.navigation_register);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginBtn.setOnClickListener(lView -> authViewModel.requestLogin(loginEd.getText().toString(), passwordEd.getText().toString()));

        loginEd.addTextChangedListener(this);
        passwordEd.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void afterTextChanged(Editable editable) {
        loginBtn.setEnabled(!loginEd.getText().toString().equals("") && !passwordEd.getText().toString().equals(""));
    }
}
