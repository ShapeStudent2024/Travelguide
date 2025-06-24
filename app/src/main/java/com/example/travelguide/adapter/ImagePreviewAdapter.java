package com.example.travelguide.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.travelguide.R;
import java.util.List;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ImageViewHolder> {

    public interface OnImageRemoveListener {
        void onImageRemove(int position);
    }

    private List<String> imagePaths;
    private OnImageRemoveListener removeListener;

    public ImagePreviewAdapter(List<String> imagePaths, OnImageRemoveListener removeListener) {
        this.imagePaths = imagePaths;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_preview, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);
        holder.bind(imagePath, position);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton btnRemove;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        public void bind(String imagePath, int position) {
            // 检查是否为本地文件路径
            if (imagePath.startsWith("/")) {
                // 本地文件路径
                Glide.with(itemView.getContext())
                        .load(new java.io.File(imagePath))
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(imageView);
            } else {
                // 网络URL或其他路径
                Glide.with(itemView.getContext())
                        .load(imagePath)
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(imageView);
            }

            btnRemove.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onImageRemove(position);
                }
            });
        }
    }
}