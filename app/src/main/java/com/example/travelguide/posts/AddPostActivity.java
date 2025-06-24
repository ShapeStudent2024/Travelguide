package com.example.travelguide.posts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelguide.R;
import com.example.travelguide.adapter.ImagePreviewAdapter;
import com.example.travelguide.data.entity.Post;
import com.example.travelguide.utils.FileUtils;
import com.example.travelguide.viewmodel.PostViewModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private static final int REQUEST_CAMERA = 200;
    private static final int REQUEST_GALLERY = 201;

    private EditText editTitle;
    private EditText editContent;
    private ImageButton btnAddImage;
    private ImageButton btnTakePhoto;
    private ImageButton btnLocation;
    private Button btnCancel;
    private Button btnPublish;
    private RecyclerView recyclerViewImages;

    private ImagePreviewAdapter imageAdapter;
    private List<String> imagePaths;
    private PostViewModel postViewModel;
    private String currentPhotoPath;
    private int editPostId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initViews();
        setupImageRecyclerView();
        setupViewModel();
        setupClickListeners();
        checkIfEditing();
    }

    private void initViews() {
        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);
        btnAddImage = findViewById(R.id.btn_add_image);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnLocation = findViewById(R.id.btn_location);
        btnCancel = findViewById(R.id.btn_cancel);
        btnPublish = findViewById(R.id.btn_publish);
        recyclerViewImages = findViewById(R.id.recycler_view_images);

        imagePaths = new ArrayList<>();
    }

    private void setupImageRecyclerView() {
        imageAdapter = new ImagePreviewAdapter(imagePaths, this::removeImage);
        recyclerViewImages.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setAdapter(imageAdapter);
    }

    private void setupViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
    }

    private void setupClickListeners() {
        btnAddImage.setOnClickListener(v -> checkStoragePermissionAndOpenGallery());
        btnTakePhoto.setOnClickListener(v -> checkCameraPermissionAndTakePhoto());
        btnLocation.setOnClickListener(v -> getCurrentLocation());
        btnCancel.setOnClickListener(v -> finish());
        btnPublish.setOnClickListener(v -> publishPost());
    }

    private void checkIfEditing() {
        editPostId = getIntent().getIntExtra("edit_post_id", -1);
        if (editPostId != -1) {
            // 编辑模式，加载现有贴文数据
            loadPostForEditing(editPostId);
            btnPublish.setText("更新");
        }
    }

    private void loadPostForEditing(int postId) {
        postViewModel.getPostById(postId).observe(this, post -> {
            if (post != null) {
                editTitle.setText(post.getTitle());
                editContent.setText(post.getContent());
                if (post.getImagePaths() != null) {
                    imagePaths.clear();
                    imagePaths.addAll(post.getImagePaths());
                    imageAdapter.notifyDataSetChanged();
                    updateImageRecyclerVisibility();
                }
            }
        });
    }

    private void checkStoragePermissionAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            takePhoto();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = FileUtils.createImageFile(this);
            if (photoFile != null) {
                currentPhotoPath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.travelguide.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private void getCurrentLocation() {
        // TODO: 实现获取当前位置功能
        Toast.makeText(this, "获取位置功能待实现", Toast.LENGTH_SHORT).show();
    }

    private void publishPost() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            editTitle.setError("请输入标题");
            return;
        }

        if (TextUtils.isEmpty(content)) {
            editContent.setError("请输入内容");
            return;
        }

        if (editPostId == -1) {
            // 新建贴文
            Post post = new Post(title, content, new ArrayList<>(imagePaths), "");
            postViewModel.insertPost(post);
            Toast.makeText(this, "贴文发布成功", Toast.LENGTH_SHORT).show();
        } else {
            // 更新贴文
            postViewModel.getPostById(editPostId).observe(this, post -> {
                if (post != null) {
                    post.setTitle(title);
                    post.setContent(content);
                    post.setImagePaths(new ArrayList<>(imagePaths));
                    post.setUpdateTime(new java.util.Date());
                    postViewModel.updatePost(post);
                    Toast.makeText(this, "贴文更新成功", Toast.LENGTH_SHORT).show();
                }
            });
        }

        finish();
    }

    private void removeImage(int position) {
        if (position >= 0 && position < imagePaths.size()) {
            imagePaths.remove(position);
            imageAdapter.notifyItemRemoved(position);
            updateImageRecyclerVisibility();
        }
    }

    private void updateImageRecyclerVisibility() {
        recyclerViewImages.setVisibility(imagePaths.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GALLERY:
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        String imagePath = FileUtils.getPathFromUri(this, imageUri);
                        if (imagePath != null) {
                            addImagePath(imagePath);
                        }
                    }
                    break;

                case REQUEST_CAMERA:
                    if (currentPhotoPath != null) {
                        addImagePath(currentPhotoPath);
                        currentPhotoPath = null;
                    }
                    break;
            }
        }
    }

    private void addImagePath(String imagePath) {
        imagePaths.add(imagePath);
        imageAdapter.notifyItemInserted(imagePaths.size() - 1);
        updateImageRecyclerVisibility();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(this, "需要相机权限才能拍照", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "需要存储权限才能选择图片", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}