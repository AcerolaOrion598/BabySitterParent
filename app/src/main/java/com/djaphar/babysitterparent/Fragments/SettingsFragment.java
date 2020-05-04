package com.djaphar.babysitterparent.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djaphar.babysitterparent.Activities.MainActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitterparent.ViewModels.SettingsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class SettingsFragment extends MyFragment {

    private SettingsViewModel settingsViewModel;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_settings));
            setBackBtnState(false);
        }
        return root;
    }

    public boolean everythingIsClosed() {
        return true;
    }

    public void backWasPressed() {

    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(boolean visible) {
        mainActivity.setBackBtnState(visible);
    }
}
