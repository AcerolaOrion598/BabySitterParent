package com.djaphar.babysitterparent.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djaphar.babysitterparent.Activities.MainActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Kid;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitterparent.ViewModels.KidViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

public class KidFragment extends MyFragment {

    private KidViewModel kidViewModel;
    private MainActivity mainActivity;
    private Context context;
    private LinearLayout eventOpenContainer;
    private ConstraintLayout kidInfoContainer, eventContainer;
    private ImageView kidPhoto;
    private TextView kidNameContent, kidPatronymicContent, kidSurnameContent, kidAgeContent, kidLockerContent, kidBloodTypeContent;
    private Kid kid;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        kidViewModel = new ViewModelProvider(this).get(KidViewModel.class);
        View root = inflater.inflate(R.layout.fragment_kid, container, false);
        eventOpenContainer = root.findViewById(R.id.event_open_container);
        kidInfoContainer = root.findViewById(R.id.kid_info_container);
        eventContainer = root.findViewById(R.id.event_container);
        kidPhoto = root.findViewById(R.id.kid_photo);
        kidNameContent = root.findViewById(R.id.kid_name_content);
        kidPatronymicContent = root.findViewById(R.id.kid_patronymic_content);
        kidSurnameContent = root.findViewById(R.id.kid_surname_content);
        kidAgeContent = root.findViewById(R.id.kid_age_content);
        kidLockerContent = root.findViewById(R.id.kid_locker_content);
        kidBloodTypeContent = root.findViewById(R.id.kid_blood_type_content);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_kid));
            setBackBtnState(View.GONE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        kidViewModel.getKid().observe(getViewLifecycleOwner(), kid -> {
            if (kid == null) {
                return;
            }
            this.kid = kid;
            setKidInfo();
        });

        eventOpenContainer.setOnClickListener(lView -> showEvent());
    }

    public boolean everythingIsClosed() {
        return eventContainer.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        setActionBarTitle(getString(R.string.title_kid));
        setBackBtnState(View.GONE);
        kidInfoContainer.setVisibility(View.VISIBLE);
        ViewDriver.hideView(eventContainer, R.anim.hide_right_animation, context);
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(int visibilityState) {
        mainActivity.setBackBtnState(visibilityState);
    }

    private void setKidInfo() {
        String photoUrl = kid.getPhotoUrl();
        if (photoUrl == null || photoUrl.equals("")) {
            photoUrl = "Ссылка на placeholder";
        }
        Glide.with(context).load(photoUrl).into(kidPhoto);

        String name  = kid.getName();
        if (name == null || name.equals("")) {
            name = getString(R.string.some_field_is_null);
        }
        kidNameContent.setText(name);

        String patronymic = kid.getPatronymic();
        if (patronymic == null || patronymic.equals("")) {
            patronymic = getString(R.string.some_field_is_null);
        }
        kidPatronymicContent.setText(patronymic);

        String surname = kid.getSurname();
        if (surname == null || surname.equals("")) {
            surname = getString(R.string.some_field_is_null);
        }
        kidSurnameContent.setText(surname);

        String age = kid.getAge();
        if (age == null || age.equals("")) {
            age = getString(R.string.some_field_is_null);
        }
        kidAgeContent.setText(age);

        Integer locker = kid.getLocker();
        String lockerStr = getString(R.string.some_field_is_null);
        if (locker != null) {
            lockerStr = locker.toString();
        }
        kidLockerContent.setText(lockerStr);

        String bloodType = kid.getBloodType();
        if (bloodType == null || bloodType.equals("")) {
            bloodType = getString(R.string.some_field_is_null);
        }
        kidBloodTypeContent.setText(bloodType);
    }

    private void showEvent() {
        setActionBarTitle(getString(R.string.events));
        setBackBtnState(View.VISIBLE);
        ViewDriver.showView(eventContainer, R.anim.show_right_animation, context).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                kidInfoContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}
