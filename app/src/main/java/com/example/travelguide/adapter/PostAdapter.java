package com.example.travelguide.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.travelguide.R;
import com.example.travelguide.data.entity.Post;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public interface OnPostClickListener {
        void onPostClick(Post post);
        void onPostLongClick(Post post);
    }

    private List<Post> posts;
    private OnPostClickListener clickListener;
    private SimpleDateFormat dateFormat;

    public PostAdapter(List<Post> posts, OnPostClickListener clickListener) {
        this.posts = posts;
        this.clickListener = clickListener;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_card, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePosts(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textContent;
        private TextView textTime;
        private ImageView imagePreview;
        private View imageContainer;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textContent = itemView.findViewById(R.id.text_content);
            textTime = itemView.findViewById(R.id.text_time);
            imagePreview = itemView.findViewById(R.id.image_preview);
            imageContainer = itemView.findViewById(R.id.image_container);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onPostClick(posts.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener.onPostLongClick(posts.get(position));
                    return true;
                }
                return false;
            });
        }

        public void bind(Post post) {
            textTitle.setText(post.getTitle());
            textContent.setText(post.getContent());
            textTime.setText(dateFormat.format(post.getCreateTime()));

            // 显示第一张图片作为预览
            if (post.getImagePaths() != null && !post.getImagePaths().isEmpty()) {
                imageContainer.setVisibility(View.VISIBLE);
                String imagePath = post.getImagePaths().get(0);

                // 检查是否为本地文件路径
                if (imagePath.startsWith("/")) {
                    // 本地文件路径
                    Glide.with(itemView.getContext())
                            .load(new java.io.File(imagePath))
                            .centerCrop()
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .into(imagePreview);
                } else {
                    // 网络URL或其他路径
                    Glide.with(itemView.getContext())
                            .load(imagePath)
                            .centerCrop()
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .into(imagePreview);
                }
            } else {
                imageContainer.setVisibility(View.GONE);
            }
        }
    }
}