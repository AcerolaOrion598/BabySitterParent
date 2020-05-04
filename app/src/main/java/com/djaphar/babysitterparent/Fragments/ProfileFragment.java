package com.djaphar.babysitterparent.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djaphar.babysitterparent.Activities.MainActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Parent;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitterparent.ViewModels.ProfileViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class ProfileFragment extends MyFragment {

    private ProfileViewModel profileViewModel;
    private MainActivity mainActivity;
    private Context context;
    private ImageView parentPhoto;
    private TextView parentNameContent, parentPatronymicContent, parentSurnameContent, parentRoleContent, parentKidContent, parentPhoneNumContent;
    private Parent parent;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        parentPhoto = root.findViewById(R.id.parent_photo);
        parentNameContent = root.findViewById(R.id.parent_name_content);
        parentPatronymicContent = root.findViewById(R.id.parent_patronymic_content);
        parentSurnameContent = root.findViewById(R.id.parent_surname_content);
        parentRoleContent = root.findViewById(R.id.parent_role_content);
        parentKidContent = root.findViewById(R.id.parent_kid_content);
        parentPhoneNumContent = root.findViewById(R.id.parent_phone_num_content);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_profile));
            setBackBtnState();
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel.getParent().observe(getViewLifecycleOwner(), parent -> {
            if (parent == null) {
                return;
            }
            this.parent = parent;
            setParentInfo();
        });
    }

    public boolean everythingIsClosed() {
        return true;
    }

    public void backWasPressed() {

    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState() {
        mainActivity.setBackBtnState(false);
    }

    private void setParentInfo() {
        String photoUrl = parent.getPhotoUrl();
        if (photoUrl == null || photoUrl.equals("")) {
            photoUrl = "Ссылка на placeholder";
        }
        Glide.with(context).load(photoUrl).into(parentPhoto);

        String name = parent.getName();
        if (name == null || name.equals("")) {
            name = getString(R.string.some_field_is_null);
        }
        parentNameContent.setText(name);

        String patronymic = parent.getPatronymic();
        if (patronymic == null || patronymic.equals("")) {
            patronymic = getString(R.string.some_field_is_null);
        }
        parentPatronymicContent.setText(patronymic);

        String surname = parent.getSurname();
        if (surname == null || surname.equals("")) {
            surname = getString(R.string.some_field_is_null);
        }
        parentSurnameContent.setText(surname);

        String role = parent.getRole();
        if (role == null || role.equals("")) {
            role = getString(R.string.some_field_is_null);
        }
        parentRoleContent.setText(role);

        String phoneNum = parent.getPhoneNum();
        if (phoneNum == null || phoneNum.equals("")) {
            phoneNum = getString(R.string.some_field_is_null);
        }
        parentPhoneNumContent.setText(phoneNum);
    }
}
