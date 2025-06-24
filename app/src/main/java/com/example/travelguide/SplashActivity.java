package com.example.travelguide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);


        // 模擬進度條動畫
        progressBar.setProgress(0);
        new Thread(() -> {
            for (int i = 0; i <= 100; i += 5) {
                try {
                    Thread.sleep(100); // 每 100ms 更新進度，總共 2 秒
                    final int progress = i;
                    runOnUiThread(() -> progressBar.setProgress(progress));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 延遲 2 秒後檢查用戶登錄狀態並跳轉
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));

            finish();
        }, 2000);
    }
}