package com.example.travelguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travelguide.account.LoginActivity;
import com.example.travelguide.R;
import com.example.travelguide.account.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private LinearLayout loginLayout;
    private LinearLayout userLayout;
    private Button loginButton;
    private Button registerButton;
    private Button logoutButton;
    private TextView userNameText;
    private TextView welcomeText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化 Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initViews(view);
        setupClickListeners();
        updateUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 检查用户是否已登录
        updateUI();
    }

    private void initViews(View view) {
        loginLayout = view.findViewById(R.id.login_layout);
        userLayout = view.findViewById(R.id.user_layout);
        loginButton = view.findViewById(R.id.login_button);
        registerButton = view.findViewById(R.id.register_button);
        logoutButton = view.findViewById(R.id.logout_button);
        userNameText = view.findViewById(R.id.user_name_text);
        welcomeText = view.findViewById(R.id.welcome_text);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            updateUI();
            Toast.makeText(getContext(), "已登出", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // 已登录状态
            loginLayout.setVisibility(View.GONE);
            userLayout.setVisibility(View.VISIBLE);

            String email = currentUser.getEmail();
            String displayName = currentUser.getDisplayName();

            if (displayName != null && !displayName.isEmpty()) {
                userNameText.setText(displayName);
            } else if (email != null) {
                userNameText.setText(email.split("@")[0]);
            } else {
                userNameText.setText("用戶");
            }

        } else {
            // 未登录状态
            loginLayout.setVisibility(View.VISIBLE);
            userLayout.setVisibility(View.GONE);
        }
    }
}