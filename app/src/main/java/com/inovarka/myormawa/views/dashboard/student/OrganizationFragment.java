package com.inovarka.myormawa.views.dashboard.student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.inovarka.myormawa.R;
import com.inovarka.myormawa.adapters.OrganizationAdapter;
import com.inovarka.myormawa.models.Organization;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrganizationFragment extends Fragment {

    private EditText etSearch;
    private TextView txtOrganizationCount;
    private ChipGroup chipGroupCategory;
    private RecyclerView rvOrganizations;

    private OrganizationAdapter adapter;
    private List<Organization> allOrganizations;
    private List<Organization> filteredOrganizations;
    private String selectedCategory = "All";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDummyData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupStatusBar();
        initViews(view);
        setupRecyclerView();
        setupChipGroup();
        setupSearchListener();
        filterOrganizations();
    }

    private void setupStatusBar() {
        if (getActivity() != null && getActivity().getWindow() != null) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.primary_blue));

            WindowInsetsControllerCompat windowInsetsController =
                    new WindowInsetsControllerCompat(window, window.getDecorView());
            windowInsetsController.setAppearanceLightStatusBars(false);
        }
    }

    private void initViews(View view) {
        etSearch = view.findViewById(R.id.et_search_organization);
        txtOrganizationCount = view.findViewById(R.id.txt_organization_count);
        chipGroupCategory = view.findViewById(R.id.chip_group_category);
        rvOrganizations = view.findViewById(R.id.rv_organizations);
    }

    private void setupRecyclerView() {
        adapter = new OrganizationAdapter(new ArrayList<>(), organization -> {
        });
        rvOrganizations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrganizations.setAdapter(adapter);
    }

    private void setupChipGroup() {
        chipGroupCategory.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int checkedId = checkedIds.get(0);
                Chip chip = getView().findViewById(checkedId);
                if (chip != null) {
                    String chipText = chip.getText().toString();
                    selectedCategory = chipText.equals("Semua") ? "All" : chipText;
                    filterOrganizations();
                }
            }
        });
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOrganizations();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterOrganizations() {
        String searchQuery = etSearch.getText().toString().toLowerCase().trim();
        filteredOrganizations = new ArrayList<>();

        for (Organization org : allOrganizations) {
            boolean matchesCategory = selectedCategory.equals("All") ||
                    org.getCategory().equals(selectedCategory);
            boolean matchesSearch = searchQuery.isEmpty() ||
                    org.getName().toLowerCase().contains(searchQuery) ||
                    org.getDescription().toLowerCase().contains(searchQuery);

            if (matchesCategory && matchesSearch) {
                filteredOrganizations.add(org);
            }
        }

        Collections.sort(filteredOrganizations, Comparator.comparingInt(Organization::getPriority));

        adapter.updateOrganizations(filteredOrganizations);
        txtOrganizationCount.setText(filteredOrganizations.size() + " Organisasi");
    }

    private void initializeDummyData() {
        allOrganizations = new ArrayList<>();

        allOrganizations.add(new Organization(
                "1",
                "BEM KM",
                "Lembaga",
                "Badan Eksekutif Mahasiswa Keluarga Mahasiswa",
                45,
                1
        ));

        allOrganizations.add(new Organization(
                "2",
                "MPM",
                "Lembaga",
                "Majelis Permusyawaratan Mahasiswa",
                32,
                2
        ));

        allOrganizations.add(new Organization(
                "3",
                "KOPMA",
                "Lembaga",
                "Koperasi Mahasiswa",
                28,
                3
        ));

        allOrganizations.add(new Organization(
                "4",
                "HMJ Teknologi Informasi",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Teknologi Informasi",
                145,
                4
        ));

        allOrganizations.add(new Organization(
                "5",
                "HMJ Produksi Pertanian",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Produksi Pertanian",
                120,
                5
        ));

        allOrganizations.add(new Organization(
                "6",
                "HMJ Bisnis",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Bisnis",
                45,
                6
        ));

        allOrganizations.add(new Organization(
                "7",
                "HMJ Manajemen Agribisnis",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Manajemen Agribisnis",
                82,
                7
        ));

        allOrganizations.add(new Organization(
                "8",
                "HMJ Teknik",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Teknik",
                110,
                8
        ));

        allOrganizations.add(new Organization(
                "9",
                "HMJ Teknologi Pertanian",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Teknologi Pertanian",
                67,
                9
        ));

        allOrganizations.add(new Organization(
                "10",
                "HMJ Peternakan",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Peternakan",
                91,
                10
        ));

        allOrganizations.add(new Organization(
                "11",
                "HMJ Kesehatan",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Kesehatan",
                103,
                11
        ));

        allOrganizations.add(new Organization(
                "12",
                "HMJ Bahasa, Komunikasi & Pariwisata",
                "Akademik",
                "Himpunan Mahasiswa Jurusan Bahasa, Komunikasi dan Pariwisata",
                85,
                12
        ));

        allOrganizations.add(new Organization(
                "13",
                "UKM SKIM",
                "Akademik",
                "Unit Kegiatan Mahasiswa Studi Karya Ilmiah",
                41,
                13
        ));

        allOrganizations.add(new Organization(
                "14",
                "UKM Robotika",
                "Akademik",
                "Unit Kegiatan Mahasiswa Robotika",
                33,
                14
        ));

        allOrganizations.add(new Organization(
                "15",
                "UKM English Club",
                "Akademik",
                "Unit Kegiatan Mahasiswa English Club",
                47,
                15
        ));

        allOrganizations.add(new Organization(
                "16",
                "UKM Labbaik",
                "Rohani",
                "Unit Kerohanian Islam atau Lembaga Dakwah Kampus",
                75,
                16
        ));

        allOrganizations.add(new Organization(
                "17",
                "UKM PMKK",
                "Rohani",
                "Unit Persekutuan Mahasiswa Kristen Katolik",
                42,
                17
        ));

        allOrganizations.add(new Organization(
                "18",
                "UKM KSR PMI",
                "Minat",
                "Unit Kegiatan Mahasiswa Korps Sukarela Palang Merah Indonesia",
                56,
                18
        ));

        allOrganizations.add(new Organization(
                "19",
                "UKM Menwa",
                "Minat",
                "Unit Kegiatan Resimen Mahasiswa",
                63,
                19
        ));

        allOrganizations.add(new Organization(
                "20",
                "UKM Pramuka",
                "Minat",
                "Unit Kegiatan Mahasiswa Pramuka",
                49,
                20
        ));

        allOrganizations.add(new Organization(
                "21",
                "UKM Explant",
                "Minat",
                "Unit Kegiatan Mahasiswa yang bergerak di bidang jurnalistik",
                38,
                21
        ));

        allOrganizations.add(new Organization(
                "22",
                "UKM Bekisar",
                "Minat",
                "Unit Kegiatan Mahasiswa Pecinta Alam",
                52,
                22
        ));

        allOrganizations.add(new Organization(
                "23",
                "UKM Olahraga",
                "Olahraga",
                "Unit Kegiatan Mahasiswa Olahraga",
                78,
                23
        ));

        allOrganizations.add(new Organization(
                "24",
                "UKM Lumut",
                "Seni",
                "Unit Kegiatan Mahasiswa Lukis, Musik, Tari",
                55,
                24
        ));

        allOrganizations.add(new Organization(
                "25",
                "UKM PSM",
                "Seni",
                "Unit Kegiatan Mahasiswa Paduan Suara Mahasiswa",
                61,
                25
        ));

        allOrganizations.add(new Organization(
                "26",
                "UKM Barabas",
                "Seni",
                "Unit Kegiatan Mahasiswa Barabas Drum Corps",
                44,
                26
        ));

        allOrganizations.add(new Organization(
                "27",
                "UKM Kotak",
                "Seni",
                "Unit Kegiatan Mahasiswa Teater",
                36,
                27
        ));
    }
}