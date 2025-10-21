package com.inovarka.myormawa.views.auth.password;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.inovarka.myormawa.R;
import com.inovarka.myormawa.views.auth.login.LoginActivity;

public class ResetPasswordSuccessActivity extends AppCompatActivity {

    private View viewBlueOverlay, checkIconContainer;
    private ConstraintLayout layoutSuccessContent;
    private MaterialButton btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.md_theme_light_primary, false);
        setContentView(R.layout.activity_reset_password_success);

        initViews();
        setupBackPressHandler();

        new Handler(Looper.getMainLooper()).postDelayed(this::startSuccessAnimation, 500);
    }

    private void setStatusBarColor(int colorRes, boolean lightMode) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, colorRes));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = lightMode ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0;
            window.getDecorView().setSystemUiVisibility(flags);
        }
    }

    private void initViews() {
        viewBlueOverlay = findViewById(R.id.view_blue_overlay);
        layoutSuccessContent = findViewById(R.id.layout_success_content);
        checkIconContainer = findViewById(R.id.check_icon_container);
        btnContinue = findViewById(R.id.btn_continue);

        // Set background icon ke biru dulu (sama dengan overlay)
        checkIconContainer.setBackgroundColor(Color.parseColor("#2C4EEF"));

        btnContinue.setOnClickListener(v -> navigateToLogin());
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        });
    }

    private void startSuccessAnimation() {
        layoutSuccessContent.setVisibility(View.VISIBLE);

        viewBlueOverlay.post(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                animateCircularReveal();
            } else {
                animateFadeOut();
            }
        });
    }

    private void animateCircularReveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int[] loc = new int[2];
            checkIconContainer.getLocationInWindow(loc);

            int cx = loc[0] + checkIconContainer.getWidth() / 2;
            int cy = loc[1] + checkIconContainer.getHeight() / 2;
            int startRadius = (int) Math.hypot(viewBlueOverlay.getWidth(), viewBlueOverlay.getHeight());
            int endRadius = checkIconContainer.getWidth() / 2;

            Animator reveal = ViewAnimationUtils.createCircularReveal(viewBlueOverlay, cx, cy, startRadius, endRadius);
            reveal.setDuration(900);
            reveal.setInterpolator(new DecelerateInterpolator());

            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setStatusBarColor(android.R.color.white, true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    viewBlueOverlay.setVisibility(View.GONE);
                    // Ubah background icon ke putih setelah animasi selesai
                    checkIconContainer.setBackgroundResource(R.drawable.shape_indicator_success);
                    animateContent();
                }
            });

            reveal.start();
        }
    }

    private void animateFadeOut() {
        ObjectAnimator fade = ObjectAnimator.ofFloat(viewBlueOverlay, "alpha", 1f, 0f);
        fade.setDuration(900);
        fade.setInterpolator(new DecelerateInterpolator());

        fade.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setStatusBarColor(android.R.color.white, true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewBlueOverlay.setVisibility(View.GONE);
                checkIconContainer.setBackgroundResource(R.drawable.shape_indicator_success);
                animateContent();
            }
        });

        fade.start();
    }

    private void animateContent() {
        View[] views = {checkIconContainer, findViewById(R.id.txt_success_title),
                findViewById(R.id.txt_success_subtitle), btnContinue};

        for (View v : views) {
            v.setAlpha(0f);
            v.setTranslationY(20f);
        }

        checkIconContainer.setScaleX(0.5f);
        checkIconContainer.setScaleY(0.5f);

        // Icon animation dengan bounce
        animateView(checkIconContainer, 100, true, null);

        // Sequential animations untuk text & button
        animateView(views[1], 300, false, null);
        animateView(views[2], 450, false, null);
        animateView(views[3], 600, false, null);

        // Container fade in
        ObjectAnimator.ofFloat(layoutSuccessContent, "alpha", 0f, 1f)
                .setDuration(300).start();
    }

    private void animateView(View view, int delay, boolean withScale, Runnable endAction) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator transY = ObjectAnimator.ofFloat(view, "translationY", 20f, 0f);

        alpha.setDuration(400);
        transY.setDuration(withScale ? 500 : 400);
        transY.setInterpolator(new DecelerateInterpolator());

        if (withScale) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);
            scaleX.setDuration(500);
            scaleY.setDuration(500);
            scaleX.setInterpolator(new OvershootInterpolator(1.5f));
            scaleY.setInterpolator(new OvershootInterpolator(1.5f));
            set.playTogether(alpha, transY, scaleX, scaleY);
        } else {
            set.playTogether(alpha, transY);
        }

        set.setStartDelay(delay);
        if (endAction != null) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    endAction.run();
                }
            });
        }
        set.start();
    }

    private void navigateToLogin() {
        btnContinue.animate()
                .scaleX(0.95f).scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> btnContinue.animate()
                        .scaleX(1f).scaleY(1f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            Intent intent = new Intent(this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        })
                        .start())
                .start();
    }
}