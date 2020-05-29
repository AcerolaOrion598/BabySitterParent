package com.djaphar.babysitterparent.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djaphar.babysitterparent.Activities.MainActivity;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.Adapters.GalleryRecyclerViewAdapter;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.GalleryPicture;
import com.djaphar.babysitterparent.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitterparent.ViewModels.GalleryViewModel;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryFragment extends MyFragment {

    private GalleryViewModel galleryViewModel;
    private MainActivity mainActivity;
    private Context context;
    private RecyclerView galleryRecyclerView;
    private ConstraintLayout galleryContainer, picDescriptionContainer;
    private RelativeLayout galleryListLayout;
    private ImageView singlePicture;
    private TextView picDescriptionTv;
    private HashMap<String, String> authHeader = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryRecyclerView = root.findViewById(R.id.gallery_recycler_view);
        galleryContainer = root.findViewById(R.id.gallery_container);
        picDescriptionContainer = root.findViewById(R.id.pic_description_container);
        galleryListLayout = root.findViewById(R.id.gallery_list_layout);
        singlePicture = root.findViewById(R.id.single_picture);
        picDescriptionTv = root.findViewById(R.id.pic_description_tv);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.title_gallery));
            setBackBtnState(View.GONE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        galleryViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
            galleryViewModel.requestMyGallery(authHeader);
        });

        galleryViewModel.getPictures().observe(getViewLifecycleOwner(), pictures -> {
            galleryRecyclerView.setAdapter(new GalleryRecyclerViewAdapter(pictures, this));
            galleryRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        });

        singlePicture.setOnClickListener(lView -> togglePicDescriptionContainer());
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(int visibilityState) {
        mainActivity.setBackBtnState(visibilityState);
    }

    public boolean everythingIsClosed() {
        return singlePicture.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        if (picDescriptionContainer.getVisibility() == View.VISIBLE) {
            picDescriptionContainer.setVisibility(View.GONE);
            return;
        }
        galleryListLayout.setVisibility(View.VISIBLE);
        galleryContainer.setBackgroundColor(Color.TRANSPARENT);
        singlePicture.setVisibility(View.GONE);
        setBackBtnState(View.GONE);
    }

    public void showSinglePicture(GalleryPicture picture) {
        Glide.with(context).load(picture.getPhotoLink()).into(singlePicture);
        String description = picture.getDescription();
        if (description == null || description.equals("")) {
            description = getString(R.string.null_description);
        }
        picDescriptionTv.setText(description);
        galleryListLayout.setVisibility(View.GONE);
        galleryContainer.setBackgroundColor(context.getResources().getColor(R.color.colorBlack87));
        singlePicture.setVisibility(View.VISIBLE);
        setBackBtnState(View.VISIBLE);
    }

    private void togglePicDescriptionContainer() {
        if (picDescriptionContainer.getVisibility() == View.VISIBLE) {
            picDescriptionContainer.setVisibility(View.GONE);
            return;
        }
        picDescriptionContainer.setVisibility(View.VISIBLE);
    }
}
