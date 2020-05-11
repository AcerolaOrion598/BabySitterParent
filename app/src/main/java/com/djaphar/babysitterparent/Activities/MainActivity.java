package com.djaphar.babysitterparent.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.djaphar.babysitterparent.Fragments.ProfileFragment;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.Adapters.MainDialog;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements MainDialog.MainDialogListener {

    private final static int SELECT_PICTURE_ID = 1;
    private TextView actionBarTitle, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            actionBarTitle = findViewById(R.id.action_bar_title);
            backBtn = findViewById(R.id.back_btn);
            backBtn.setOnClickListener(lView -> onBackPressed());
        }
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }


    @Override
    public void onBackPressed() {
        MyFragment currentFragment = getMyFragment();
        if (currentFragment == null) {
            return;
        }
        if (currentFragment.everythingIsClosed()) {
            super.onBackPressed();
            return;
        }
        currentFragment.backWasPressed();
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
    }

    public void setBackBtnState(int visibilityState) {
        backBtn.setVisibility(visibilityState);
    }

    @Override
    public void returnFieldValue(String fieldValue, View calledView) {
        MyFragment myFragment = getMyFragment();
        if (myFragment == null) {
            return;
        }
        myFragment.returnFieldValue(fieldValue, calledView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE_ID && resultCode == RESULT_OK && data != null) {
            ProfileFragment profileFragment = null;
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment != null) {
                profileFragment = (ProfileFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            }
            if (profileFragment == null) {
                return;
            }
            profileFragment.setSelectedPicture(data.getData());
        }
    }

    private MyFragment getMyFragment() {
        MyFragment myFragment = null;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            myFragment = (MyFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
        }
        return myFragment;
    }

    public void selectPicture() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), SELECT_PICTURE_ID);
    }

    public void logout() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}
