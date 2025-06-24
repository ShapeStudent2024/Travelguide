package com.example.travelguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelguide.posts.AddPostActivity;
import com.example.travelguide.R;
import com.example.travelguide.adapter.PostAdapter;
import com.example.travelguide.data.entity.Post;
import com.example.travelguide.viewmodel.PostViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class PlanFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FloatingActionButton fabAddPost;
    private PostViewModel postViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupViewModel();
        setupFab();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_posts);
        fabAddPost = view.findViewById(R.id.fab_add_post);
    }

    private void setupRecyclerView() {
        postAdapter = new PostAdapter(new ArrayList<>(), new PostAdapter.OnPostClickListener() {
            @Override
            public void onPostClick(Post post) {
                PlanFragment.this.onPostClick(post);
            }

            @Override
            public void onPostLongClick(Post post) {
                PlanFragment.this.onPostLongClick(post);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postAdapter);
    }

    private void setupViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // 观察贴文数据变化
        postViewModel.getAllPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null) {
                postAdapter.updatePosts(posts);
            }
        });
    }

    private void setupFab() {
        fabAddPost.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPostActivity.class);
            startActivity(intent);
        });
    }

    private void onPostClick(Post post) {
        // 点击贴文，显示详情（暂时用Toast代替详情页面）
        Toast.makeText(getContext(), "点击了贴文: " + post.getTitle(), Toast.LENGTH_SHORT).show();
        // TODO: 实现PostDetailActivity后取消注释下面的代码
        // Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        // intent.putExtra("post_id", post.getId());
        // startActivity(intent);
    }

    private void onPostLongClick(Post post) {
        // 长按贴文，显示编辑/删除菜单
        showPostOptionsDialog(post);
    }

    private void showPostOptionsDialog(Post post) {
        String[] options = {"编辑", "删除"};

        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("选择操作")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // 编辑
                            editPost(post);
                            break;
                        case 1: // 删除
                            deletePost(post);
                            break;
                    }
                })
                .show();
    }

    private void editPost(Post post) {
        Intent intent = new Intent(getActivity(), AddPostActivity.class);
        intent.putExtra("edit_post_id", post.getId());
        startActivity(intent);
    }

    private void deletePost(Post post) {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("确认删除")
                .setMessage("确定要删除这个贴文吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    postViewModel.deletePost(post);
                    Toast.makeText(getContext(), "贴文已删除", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}