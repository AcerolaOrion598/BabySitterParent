package com.djaphar.babysitterparent.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.ViewModels.AuthViewModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

public class AuthActivity extends AppCompatActivity {

    private TextView actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            actionBarTitle = findViewById(R.id.action_bar_title);
        }

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getUser().observe(this, user -> {
            if (user == null) {
                return;
            }
            startMainActivity();
        });
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
    }

    public void navigate(int fragment) {
        Navigation.findNavController(this, R.id.nav_host_auth_fragment).navigate(fragment);
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
