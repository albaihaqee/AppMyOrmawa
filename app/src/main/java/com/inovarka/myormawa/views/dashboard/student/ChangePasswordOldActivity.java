package com.inovarka.myormawa.views.dashboard.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.inovarka.myormawa.R;

public class ChangePasswordOldActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyOrmawaPrefs";
    private static final String KEY_PASSWORD = "user_password";
    private static final String DEFAULT_PASSWORD = "Test1234";
    private static final int MAX_ATTEMPTS = 3;

    private ImageView btnBack;
    private TextInputLayout tilPassword;
    private TextInputEditText edtPassword;
    private MaterialButton btnContinue;

    private int attemptCount = 0;
    private String savedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_change_password_old);

        initViews();
        loadPassword();
        setupListeners();
    }

    private void setStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_blue));
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tilPassword = findViewById(R.id.til_password_lama);
        edtPassword = findViewById(R.id.edt_password_lama);
        btnContinue = findViewById(R.id.btn_continue);
    }

    private void loadPassword() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        savedPassword = prefs.getString(KEY_PASSWORD, DEFAULT_PASSWORD);

        // Pastikan password default tersimpan jika belum ada
        if (savedPassword == null || savedPassword.isEmpty()) {
            savedPassword = DEFAULT_PASSWORD;
            prefs.edit().putString(KEY_PASSWORD, DEFAULT_PASSWORD).apply();
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setError(null);
                String input = s.toString().trim();

                // Button enabled jika minimal 8 karakter
                boolean enabled = input.length() >= 8;
                setButtonState(enabled);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnContinue.setOnClickListener(v -> validatePassword());
    }

    private void validatePassword() {
        String input = edtPassword.getText().toString().trim();

        if (input.isEmpty()) {
            tilPassword.setError("Password tidak boleh kosong");
            return;
        }

        if (input.equals(savedPassword)) {
            // Password benar, lanjut ke change password new
            startActivity(new Intent(this, ChangePasswordNewActivity.class));
            finish();
        } else {
            handleWrongPassword();
        }
    }

    private void handleWrongPassword() {
        attemptCount++;
        int remaining = MAX_ATTEMPTS - attemptCount;

        if (remaining > 0) {
            String msg = "Password salah! Kesempatan tersisa " + remaining + "x lagi";
            tilPassword.setError(msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            String msg = "Password salah! Anda telah melebihi batas percobaan";
            tilPassword.setError(msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            lockTemporarily();
        }
    }

    private void lockTemporarily() {
        edtPassword.setEnabled(false);
        setButtonState(false);

        edtPassword.postDelayed(() -> {
            attemptCount = 0;
            tilPassword.setError(null);
            edtPassword.setText("");
            edtPassword.setEnabled(true);
        }, 3000);
    }

    private void setButtonState(boolean enabled) {
        btnContinue.setEnabled(enabled);
        btnContinue.setAlpha(enabled ? 1.0f : 0.5f);
    }
}