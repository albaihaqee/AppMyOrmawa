package com.inovarka.myormawa.views.dashboard.student;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.inovarka.myormawa.R;
import com.inovarka.myormawa.adapters.BannerAdapter;
import com.inovarka.myormawa.models.BannerModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int AUTO_SLIDE_DELAY = 5000;
    private static final int DOT_SIZE_DP = 6;
    private static final int DOT_MARGIN_DP = 4;

    private ViewPager2 bannerViewPager;
    private LinearLayout dotsContainer;
    private BannerAdapter bannerAdapter;
    private List<BannerModel> bannerList;
    private Handler sliderHandler;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupBanner();
        setupClickListeners(view);

        return view;
    }

    private void initViews(View view) {
        bannerViewPager = view.findViewById(R.id.banner_viewpager);
        dotsContainer = view.findViewById(R.id.dots_container);
        sliderHandler = new Handler(Looper.getMainLooper());
    }

    private void setupBanner() {
        initBannerList();
        setupBannerAdapter();
        setupPageTransformer();
        setupDotsIndicator();
        setupPageChangeCallback();
        startAutoSlide();
    }

    private void initBannerList() {
        bannerList = new ArrayList<>();
        bannerList.add(new BannerModel(R.drawable.ill_banner_slide_1));
        bannerList.add(new BannerModel(R.drawable.ill_banner_slide_2));
        bannerList.add(new BannerModel(R.drawable.ill_banner_slide_3));
    }

    private void setupBannerAdapter() {
        bannerAdapter = new BannerAdapter(bannerList);
        bannerViewPager.setAdapter(bannerAdapter);

        bannerAdapter.setOnBannerClickListener((banner, position) ->
                Toast.makeText(getContext(), "Banner " + (position + 1) + " clicked!", Toast.LENGTH_SHORT).show()
        );
    }

    private void setupPageTransformer() {
        bannerViewPager.setPageTransformer((page, position) -> {
            float absPosition = Math.abs(position);

            if (absPosition >= 1) {
                page.setAlpha(0f);
            } else {
                page.setAlpha(1f - absPosition);
            }

            float scale = 1f - (absPosition * 0.05f);
            page.setScaleX(scale);
            page.setScaleY(scale);
            page.setTranslationZ(-absPosition);
        });
    }

    private void setupDotsIndicator() {
        dotsContainer.removeAllViews();

        for (int i = 0; i < bannerList.size(); i++) {
            View dot = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(DOT_SIZE_DP),
                    dpToPx(DOT_SIZE_DP)
            );
            params.setMargins(dpToPx(DOT_MARGIN_DP), 0, dpToPx(DOT_MARGIN_DP), 0);

            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.shape_indicator_inactive);
            dotsContainer.addView(dot);
        }

        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        for (int i = 0; i < dotsContainer.getChildCount(); i++) {
            View dot = dotsContainer.getChildAt(i);
            int drawableRes = (i == position) ?
                    R.drawable.shape_indicator_active :
                    R.drawable.shape_indicator_inactive;
            dot.setBackgroundResource(drawableRes);
        }
    }

    private void setupPageChangeCallback() {
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
                currentPage = position;
            }
        });
    }

    private void startAutoSlide() {
        sliderHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY);
    }

    private void stopAutoSlide() {
        sliderHandler.removeCallbacks(autoSlideRunnable);
    }

    private final Runnable autoSlideRunnable = new Runnable() {
        @Override
        public void run() {
            if (bannerList != null && !bannerList.isEmpty()) {
                currentPage--;
                if (currentPage < 0) {
                    currentPage = bannerList.size() - 1;
                }
                bannerViewPager.setCurrentItem(currentPage, true);
                sliderHandler.postDelayed(this, AUTO_SLIDE_DELAY);
            }
        }
    };

    private void setupClickListeners(View view) {
        view.findViewById(R.id.btn_oprec).setOnClickListener(v ->
                showToast("Oprec clicked"));

        view.findViewById(R.id.btn_event).setOnClickListener(v ->
                showToast("Event clicked"));

        view.findViewById(R.id.btn_kompetisi).setOnClickListener(v ->
                showToast("Kompetisi clicked"));

        view.findViewById(R.id.btn_pelatihan).setOnClickListener(v ->
                showToast("Pelatihan clicked"));

        view.findViewById(R.id.btn_beasiswa).setOnClickListener(v ->
                showToast("Beasiswa clicked"));

        view.findViewById(R.id.btn_notification).setOnClickListener(v ->
                showToast("Notification clicked"));

        view.findViewById(R.id.btn_filter).setOnClickListener(v ->
                showToast("Filter clicked"));
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoSlide();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerList != null && !bannerList.isEmpty()) {
            startAutoSlide();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAutoSlide();
    }
}