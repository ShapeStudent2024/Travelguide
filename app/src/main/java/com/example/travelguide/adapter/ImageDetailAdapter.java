package com.example.travelguide.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.travelguide.R;
import java.util.ArrayList;
import java.util.List;

public class ImageDetailAdapter extends RecyclerView.Adapter<ImageDetailAdapter.ImageViewHolder> {

    private List<String> imagePaths;

    public ImageDetailAdapter() {
        this.imagePaths = new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_detail, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);
        holder.bind(imagePath);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public void updateImages(List<String> newImagePaths) {
        this.imagePaths = newImagePaths;
        notifyDataSetChanged();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }

        public void bind(String imagePath) {
            if (imagePath.startsWith("/")) {
                Glide.with(itemView.getContext())
                        .load(new java.io.File(imagePath))
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(imageView);
            } else {
                Glide.with(itemView.getContext())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(imageView);
            }
        }
    }
}