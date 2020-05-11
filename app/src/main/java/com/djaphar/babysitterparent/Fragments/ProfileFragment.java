package com.djaphar.babysitterparent.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.djaphar.babysitterparent.Activities.MainActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.Adapters.MainDialog;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.Parent;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.UpdatePictureModel;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.ViewDriver;
import com.djaphar.babysitterparent.ViewModels.ProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

public class ProfileFragment extends MyFragment {

    private ProfileViewModel profileViewModel;
    private MainActivity mainActivity;
    private Context context;
    private ImageView parentPhoto;
    private ScrollView parentInfoSv;
    private LinearLayout parentNameContainer, parentPatronymicContainer, parentSurnameContainer, parentRoleContainer, parentKidContainer, parentPhoneNumContainer;
    private TextView parentNameContent, parentPatronymicContent, parentSurnameContent, parentRoleContent, parentKidContent, parentPhoneNumContent;
    private ImageButton selectPictureBtn, deletePictureBtn, cancelPictureBtn, savePictureBtn;
    private Parent parent;
    private HashMap<String, String> authHeader = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        parentPhoto = root.findViewById(R.id.parent_photo);
        parentInfoSv = root.findViewById(R.id.parent_info_sv);
        parentNameContainer = root.findViewById(R.id.parent_name_container);
        parentPatronymicContainer = root.findViewById(R.id.parent_patronymic_container);
        parentSurnameContainer = root.findViewById(R.id.parent_surname_container);
        parentRoleContainer = root.findViewById(R.id.parent_role_container);
        parentKidContainer = root.findViewById(R.id.parent_kid_container);
        parentPhoneNumContainer = root.findViewById(R.id.parent_phone_num_container);
        parentNameContent = root.findViewById(R.id.parent_name_content);
        parentPatronymicContent = root.findViewById(R.id.parent_patronymic_content);
        parentSurnameContent = root.findViewById(R.id.parent_surname_content);
        parentRoleContent = root.findViewById(R.id.parent_role_content);
        parentKidContent = root.findViewById(R.id.parent_kid_content);
        parentPhoneNumContent = root.findViewById(R.id.parent_phone_num_content);
        selectPictureBtn = root.findViewById(R.id.select_picture_btn);
        deletePictureBtn = root.findViewById(R.id.delete_picture_btn);
        cancelPictureBtn = root.findViewById(R.id.cancel_picture_btn);
        savePictureBtn = root.findViewById(R.id.save_picture_btn);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_profile));
            setBackBtnState(View.GONE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
            profileViewModel.requestProfile(authHeader);
        });

        profileViewModel.getParent().observe(getViewLifecycleOwner(), parent -> {
            if (parent == null) {
                return;
            }
            this.parent = parent;
            setParentInfo();
        });

        parentNameContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.name_title_text), parent.getName(), lView)
                .show(getParentFragmentManager(), "dialog"));
        parentPatronymicContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.patronymic_title_text), parent.getPatronymic(), lView)
                .show(getParentFragmentManager(), "dialog"));
        parentSurnameContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.surname_title_text), parent.getSurname(), lView)
                .show(getParentFragmentManager(), "dialog"));
        parentRoleContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.parent_role_title_text), parent.getRelationDegree(), lView)
                .show(getParentFragmentManager(), "dialog"));
//        parentKidContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.kid), parent.getPhoneNum(), lView)
//                .show(getParentFragmentManager(), "dialog"));
        parentPhoneNumContainer.setOnClickListener(lView -> new MainDialog(getString(R.string.parent_phone_num_title_text), parent.getPhone(), lView)
                .show(getParentFragmentManager(), "dialog"));

        selectPictureBtn.setOnClickListener(lView -> {
            setBackBtnState(View.VISIBLE);
            if (!parent.getPhotoLink().equals(getString(R.string.no_picture_url))) {
                if (deletePictureBtn.getVisibility() == View.VISIBLE) {
                    mainActivity.selectPicture();
                } else {
                    ViewDriver.showView(deletePictureBtn, R.anim.show_round_btn, context);
                }
            } else {
                mainActivity.selectPicture();
            }
        });

        deletePictureBtn.setOnClickListener(lView -> new AlertDialog.Builder(mainActivity)
                .setTitle(R.string.delete_picture_title)
                .setMessage(R.string.delete_picture_message)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                    profileViewModel.requestDeletePicture(authHeader,
                            new UpdatePictureModel(parent.getParentId(), getString(R.string.parent_profile), null));
                    ViewDriver.hideView(deletePictureBtn, R.anim.hide_round_btn, context);
                    setBackBtnState(View.GONE);
                })
                .show());

        cancelPictureBtn.setOnClickListener(lView -> backWasPressed());

        savePictureBtn.setOnClickListener(lView -> {
            new PictureUploader(((BitmapDrawable) parentPhoto.getDrawable()).getBitmap(), profileViewModel,
                    authHeader, parent.getParentId(), getString(R.string.parent_profile)).execute();
            hideCancelBtn();
        });
    }

    public boolean everythingIsClosed() {
        return deletePictureBtn.getVisibility() != View.VISIBLE && cancelPictureBtn.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        setBackBtnState(View.GONE);

        if (deletePictureBtn.getVisibility() == View.VISIBLE) {
            ViewDriver.hideView(deletePictureBtn, R.anim.hide_round_btn, context);
            return;
        }

        if (cancelPictureBtn.getVisibility() == View.VISIBLE) {
            hideCancelBtn();
            profileViewModel.requestProfile(authHeader);
        }
    }

    private void hideCancelBtn() {
        setBackBtnState(View.GONE);
        ViewDriver.hideView(cancelPictureBtn, R.anim.hide_round_btn, context).setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                savePictureBtn.setVisibility(View.GONE);
                selectPictureBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    public void returnFieldValue(String fieldValue, View calledView) {
        switch (calledView.getId()) {
            case R.id.parent_name_container:
                parent.setName(fieldValue);
                break;
            case R.id.parent_patronymic_container:
                parent.setPatronymic(fieldValue);
                break;
            case R.id.parent_surname_container:
                parent.setSurname(fieldValue);
                break;
            case R.id.parent_role_container:
                parent.setRelationDegree(fieldValue);
                break;
//            case R.id.parent_kid_container:
//                break;
            case R.id.parent_phone_num_container:
                parent.setPhone(fieldValue);
                break;
        }
        profileViewModel.requestUpdateParent(authHeader, parent);
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(int visibilityState) {
        mainActivity.setBackBtnState(visibilityState);
    }

    public void setSelectedPicture(Uri selectedPictureUri) {
        selectPictureBtn.setVisibility(View.GONE);
        deletePictureBtn.setVisibility(View.GONE);
        cancelPictureBtn.setVisibility(View.VISIBLE);
        savePictureBtn.setVisibility(View.VISIBLE);
        parentPhoto.setImageURI(selectedPictureUri);
    }

    private void setParentInfo() {
        parentInfoSv.setVisibility(View.VISIBLE);
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .load(parent.getPhotoLink())
                .into(parentPhoto);
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

        String role = parent.getRelationDegree();
        if (role == null || role.equals("")) {
            role = getString(R.string.some_field_is_null);
        }
        parentRoleContent.setText(role);

        String phoneNum = parent.getPhone();
        if (phoneNum == null || phoneNum.equals("")) {
            phoneNum = getString(R.string.some_field_is_null);
        }
        parentPhoneNumContent.setText(phoneNum);
    }

    private static class PictureUploader extends AsyncTask<Void, Void, Void> {
        private Bitmap picture;
        private ProfileViewModel profileViewModel;
        private HashMap<String, String> authHeader;
        private String parentId, profile;

        PictureUploader(Bitmap picture, ProfileViewModel profileViewModel, HashMap<String, String> authHeader, String parentId, String profile) {
            this.picture = picture;
            this.profileViewModel = profileViewModel;
            this.authHeader = authHeader;
            this.parentId = parentId;
            this.profile = profile;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            String pictureStr = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            profileViewModel.requestUpdatePicture(authHeader, new UpdatePictureModel(parentId, profile, pictureStr));
            return null;
        }
    }
}
