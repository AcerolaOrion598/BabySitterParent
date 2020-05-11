package com.djaphar.babysitterparent.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djaphar.babysitterparent.Activities.MainActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitterparent.ViewModels.SettingsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

public class SettingsFragment extends MyFragment {

    private SettingsViewModel settingsViewModel;
    private MainActivity mainActivity;
    private Context context;
    private TextView logoutTv;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        logoutTv = root.findViewById(R.id.logout_tv);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_settings));
            setBackBtnState();
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                return;
            }
            mainActivity.logout();
        });

        logoutTv.setOnClickListener(lView -> new AlertDialog.Builder(context)
                .setTitle(R.string.logout_text)
                .setMessage(R.string.logout_message)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> settingsViewModel.logout())
                .show());
    }

    public boolean everythingIsClosed() {
        return true;
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState() {
        mainActivity.setBackBtnState(View.GONE);
    }
}
