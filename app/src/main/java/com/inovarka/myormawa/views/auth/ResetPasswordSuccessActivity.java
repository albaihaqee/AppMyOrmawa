package com.inovarka.myormawa.views.auth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.inovarka.myormawa.R;

public class ResetPasswordSuccessActivity extends AppCompatActivity {

    private View viewBlueCircle;
    private ConstraintLayout layoutSuccessContent;
    private View checkIconContainer;

    private static final int DURATION_EXPAND = 700;
    private static final int DURATION_SHRINK = 700;
    private static final int DURATION_CONTENT_FADE = 550;
    private static final int DELAY_BEFORE_SHRINK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBlueStatusBar();
        setContentView(R.layout.activity_reset_password_success);
        initViews();
        setupBackPressHandler();
        viewBlueCircle.post(this::startSuccessAnimation);
    }

    private void setBlueStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_theme_light_primary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(0);
        }
    }

    private void setWhiteStatusBar() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void initViews() {
        viewBlueCircle = findViewById(R.id.view_blue_circle);
        layoutSuccessContent = findViewById(R.id.layout_success_content);
        checkIconContainer = findViewById(R.id.check_icon_container);
        MaterialButton btnContinue = findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Disabled - prevent back navigation from success screen
            }
        });
    }

    private void startSuccessAnimation() {
        viewBlueCircle.setVisibility(View.VISIBLE);
        viewBlueCircle.setScaleX(0f);
        viewBlueCircle.setScaleY(0f);
        viewBlueCircle.setAlpha(1f);

        AnimatorSet masterSet = new AnimatorSet();
        masterSet.playSequentially(
                createExpandAnimation(),
                createShrinkAnimation(),
                createContentAnimation()
        );
        masterSet.start();
    }

    private AnimatorSet createExpandAnimation() {
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        float screenHeight = getResources().getDisplayMetrics().heightPixels;
        float maxDimension = Math.max(screenWidth, screenHeight);
        float targetScale = (maxDimension / 100f) * 1.8f;

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(viewBlueCircle, "scaleX", 0f, targetScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(viewBlueCircle, "scaleY", 0f, targetScale);

        scaleX.setDuration(DURATION_EXPAND);
        scaleY.setDuration(DURATION_EXPAND);
        scaleX.setInterpolator(new DecelerateInterpolator(1.5f));
        scaleY.setInterpolator(new DecelerateInterpolator(1.5f));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        return set;
    }

    private AnimatorSet createShrinkAnimation() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(viewBlueCircle, "scaleX", 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(viewBlueCircle, "scaleY", 1.0f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(viewBlueCircle, "alpha", 1f, 0f);

        scaleX.setDuration(DURATION_SHRINK);
        scaleY.setDuration(DURATION_SHRINK);
        fadeOut.setDuration(DURATION_SHRINK);

        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleX.addUpdateListener(animation -> {
            float progress = animation.getAnimatedFraction();

            if (progress >= 0.35f) {
                if (layoutSuccessContent.getVisibility() != View.VISIBLE) {
                    setWhiteStatusBar();
                    layoutSuccessContent.setVisibility(View.VISIBLE);
                    checkIconContainer.setAlpha(0f);
                    checkIconContainer.setScaleX(0.7f);
                    checkIconContainer.setScaleY(0.7f);
                }

                float iconProgress = (progress - 0.35f) / 0.65f;
                float smoothProgress = (float) (1 - Math.pow(1 - iconProgress, 2));

                checkIconContainer.setAlpha(smoothProgress);
                float scale = 0.7f + (0.3f * smoothProgress);
                checkIconContainer.setScaleX(scale);
                checkIconContainer.setScaleY(scale);
            }
        });

        scaleX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewBlueCircle.setVisibility(View.GONE);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setStartDelay(DELAY_BEFORE_SHRINK);
        set.playTogether(scaleX, scaleY, fadeOut);
        return set;
    }

    private AnimatorSet createContentAnimation() {
        ObjectAnimator iconScaleX = ObjectAnimator.ofFloat(checkIconContainer, "scaleX", checkIconContainer.getScaleX(), 1f);
        ObjectAnimator iconScaleY = ObjectAnimator.ofFloat(checkIconContainer, "scaleY", checkIconContainer.getScaleY(), 1f);
        ObjectAnimator iconAlpha = ObjectAnimator.ofFloat(checkIconContainer, "alpha", checkIconContainer.getAlpha(), 1f);

        iconScaleX.setDuration(DURATION_CONTENT_FADE);
        iconScaleY.setDuration(DURATION_CONTENT_FADE);
        iconAlpha.setDuration(DURATION_CONTENT_FADE);

        iconScaleX.setInterpolator(new OvershootInterpolator(0.6f));
        iconScaleY.setInterpolator(new OvershootInterpolator(0.6f));
        iconAlpha.setInterpolator(new DecelerateInterpolator(1.0f));

        AnimatorSet iconSet = new AnimatorSet();
        iconSet.playTogether(iconScaleX, iconScaleY, iconAlpha);

        AnimatorSet titleSet = createFadeSlide(findViewById(R.id.txt_success_title), 150);
        AnimatorSet subtitleSet = createFadeSlide(findViewById(R.id.txt_success_subtitle), 280);
        AnimatorSet buttonSet = createFadeSlide(findViewById(R.id.btn_continue), 410);

        AnimatorSet masterSet = new AnimatorSet();
        masterSet.playTogether(iconSet, titleSet, subtitleSet, buttonSet);
        return masterSet;
    }

    private AnimatorSet createFadeSlide(View view, int delay) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator translate = ObjectAnimator.ofFloat(view, "translationY", 25f, 0f);

        alpha.setDuration(DURATION_CONTENT_FADE);
        translate.setDuration(DURATION_CONTENT_FADE);

        alpha.setInterpolator(new DecelerateInterpolator(1.2f));
        translate.setInterpolator(new DecelerateInterpolator(1.4f));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translate);
        set.setStartDelay(delay);
        return set;
    }
}