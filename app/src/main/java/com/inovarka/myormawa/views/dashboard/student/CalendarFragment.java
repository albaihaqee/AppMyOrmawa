package com.inovarka.myormawa.views.dashboard.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.inovarka.myormawa.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private LinearLayout containerEvents;
    private TextView tvSelectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        containerEvents = view.findViewById(R.id.containerEvents);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);

        CalendarDay today = CalendarDay.today();
        calendarView.setDateSelected(today, true);
        updateSelectedDate(today);

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) updateSelectedDate(date);
        });

        return view;
    }

    private void updateSelectedDate(CalendarDay date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
        String formatted = sdf.format(date.getDate());

        // Cek apakah tanggal ini adalah hari ini
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date.getDate());
        boolean isToday = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        if (isToday) {
            tvSelectedDate.setText(formatted + " (Hari Ini)");
        } else {
            tvSelectedDate.setText(formatted);
        }

        showEventsForDate(formatted);
    }


    private void showEventsForDate(String date) {
        containerEvents.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        // Simulasi data kegiatan
        boolean hasEvents = false;

        // Contoh: hanya 28 Oktober 2025 yang punya kegiatan
        if (date.contains("28 Oktober")) {
            hasEvents = true;
        }

        if (hasEvents) {
            // tampilkan daftar kegiatan
            for (int i = 1; i <= 3; i++) {
                View item = inflater.inflate(R.layout.fragment_event_item, containerEvents, false);

                TextView tvTime = item.findViewById(R.id.tvTime);
                TextView tvTitle = item.findViewById(R.id.tvTitle);
                TextView tvLocation = item.findViewById(R.id.tvLocation);

                tvTime.setText("09:0" + i + " WIB");
                tvTitle.setText("Kegiatan ke-" + i);
                tvLocation.setText("Lapangan Hijau A3, Politeknik Negeri Jember");

                containerEvents.addView(item);
            }
        } else {
            // tampilkan tampilan kosong
            View emptyView = inflater.inflate(R.layout.layout_empty_event, containerEvents, false);
            containerEvents.addView(emptyView);
        }
    }
}
