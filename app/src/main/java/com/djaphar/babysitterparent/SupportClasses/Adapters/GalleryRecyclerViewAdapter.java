package com.djaphar.babysitterparent.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.djaphar.babysitterparent.Fragments.GalleryFragment;
import com.djaphar.babysitterparent.R;
import com.djaphar.babysitterparent.SupportClasses.ApiClasses.GalleryPicture;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    private ArrayList<GalleryPicture> pictures;
    private GalleryFragment galleryFragment;

    public GalleryRecyclerViewAdapter(ArrayList<GalleryPicture> pictures, GalleryFragment galleryFragment) {
        this.pictures = pictures;
        this.galleryFragment = galleryFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parentLayout.setOnClickListener(lView -> galleryFragment.showSinglePicture(pictures.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(galleryFragment).load(pictures.get(position).getPhotoLink()).into(holder.galleryPicture);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parentLayout;
        private ImageView galleryPicture;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_gallery);
            galleryPicture = itemView.findViewById(R.id.gallery_picture);
        }
    }
}
