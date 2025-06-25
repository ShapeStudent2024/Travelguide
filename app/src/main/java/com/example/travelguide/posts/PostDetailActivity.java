package com.example.travelguide.posts;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelguide.R;
import com.example.travelguide.adapter.ImageDetailAdapter;
import com.example.travelguide.data.entity.Post;
import com.example.travelguide.viewmodel.PostViewModel;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    private TextView textTitle;
    private TextView textContent;
    private TextView textTime;
    private TextView textLocation;
    private RecyclerView recyclerViewImages;

    private PostViewModel postViewModel;
    private ImageDetailAdapter imageAdapter;
    private SimpleDateFormat dateFormat;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postId = getIntent().getIntExtra("post_id", -1);
        if (postId == -1) {
            finish();
            return;
        }

        initViews();
        setupImageRecyclerView();
        setupViewModel();
        loadPostDetail();
    }

    private void initViews() {
        textTitle = findViewById(R.id.text_title);
        textContent = findViewById(R.id.text_content);
        textTime = findViewById(R.id.text_time);
        textLocation = findViewById(R.id.text_location);
        recyclerViewImages = findViewById(R.id.recycler_view_images);

        dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm", Locale.getDefault());

        // 设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Post Details");
        }
    }

    private void setupImageRecyclerView() {
        imageAdapter = new ImageDetailAdapter();
        recyclerViewImages.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imageAdapter);
    }

    private void setupViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
    }

    private void loadPostDetail() {
        postViewModel.getPostById(postId).observe(this, post -> {
            if (post != null) {
                displayPost(post);
            }
        });
    }

    private void displayPost(Post post) {
        textTitle.setText(post.getTitle());
        textContent.setText(post.getContent());
        textTime.setText(dateFormat.format(post.getCreateTime()));

        if (post.getLocation() != null && !post.getLocation().isEmpty()) {
            textLocation.setText("📍 " + post.getLocation());
            textLocation.setVisibility(TextView.VISIBLE);
        } else {
            textLocation.setVisibility(TextView.GONE);
        }

        // 显示图片
        if (post.getImagePaths() != null && !post.getImagePaths().isEmpty()) {
            imageAdapter.updateImages(post.getImagePaths());
            recyclerViewImages.setVisibility(RecyclerView.VISIBLE);
        } else {
            recyclerViewImages.setVisibility(RecyclerView.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}