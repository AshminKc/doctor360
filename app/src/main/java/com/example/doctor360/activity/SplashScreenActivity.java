package com.example.doctor360.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doctor360.R;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar splashProgress;
    public static int SPLASH_TIME_OUT=3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 195).setDuration(6000).start();
    }
}
