package com.inovarka.myormawa.views.dashboard.student;

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

import java.util.regex.Pattern;

public class ChangePasswordNewActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyOrmawaPrefs";
    private static final String KEY_PASSWORD = "user_password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NIM = "nim";

    private ImageView btnBack;
    private TextInputLayout tilPasswordBaru, tilKonfirmasi;
    private TextInputEditText edtPasswordBaru, edtKonfirmasi;
    private MaterialButton btnContinue;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_change_password_new);

        initViews();
        loadUsername();
        setupListeners();
    }

    private void setStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_blue));
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tilPasswordBaru = findViewById(R.id.til_password_baru);
        tilKonfirmasi = findViewById(R.id.til_konfirmasi_password);
        edtPasswordBaru = findViewById(R.id.edt_password_baru);
        edtKonfirmasi = findViewById(R.id.edt_konfirmasi_password);
        btnContinue = findViewById(R.id.btn_continue);
    }

    private void loadUsername() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        username = prefs.getString(KEY_USERNAME, prefs.getString(KEY_NIM, ""));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswords();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        edtPasswordBaru.addTextChangedListener(watcher);
        edtKonfirmasi.addTextChangedListener(watcher);

        btnContinue.setOnClickListener(v -> savePassword());
    }

    private void validatePasswords() {
        String newPass = edtPasswordBaru.getText().toString().trim();
        String confirmPass = edtKonfirmasi.getText().toString().trim();

        tilPasswordBaru.setError(null);
        tilKonfirmasi.setError(null);

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            setButtonState(false);
            return;
        }

        boolean isValid = isPasswordValid(newPass);
        boolean isMatch = newPass.equals(confirmPass);

        // Tampilkan error jika ada
        if (!isValid && !newPass.isEmpty()) {
            tilPasswordBaru.setError("Password tidak memenuhi syarat");
        }
        if (!confirmPass.isEmpty() && !isMatch) {
            tilKonfirmasi.setError("Password tidak cocok");
        }

        // Button enabled hanya jika valid dan match
        setButtonState(isValid && isMatch);
    }

    private boolean isPasswordValid(String pass) {
        // 1. Panjang 8-12 karakter
        if (pass.length() < 8 || pass.length() > 12) {
            return false;
        }

        // 2. Minimal 1 huruf kapital
        if (!Pattern.compile("[A-Z]").matcher(pass).find()) {
            return false;
        }

        // 3. Minimal 1 huruf kecil
        if (!Pattern.compile("[a-z]").matcher(pass).find()) {
            return false;
        }

        // 4. Minimal 1 angka
        if (!Pattern.compile("[0-9]").matcher(pass).find()) {
            return false;
        }

        // 5. Tidak boleh mengandung spasi
        if (pass.contains(" ")) {
            return false;
        }

        // 6. Tidak boleh sama dengan username
        if (!username.isEmpty() && pass.equalsIgnoreCase(username)) {
            return false;
        }

        return true;
    }

    private void savePassword() {
        String newPass = edtPasswordBaru.getText().toString().trim();

        if (!isPasswordValid(newPass)) {
            Toast.makeText(this, "Password tidak memenuhi syarat", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_PASSWORD, newPass);
        editor.apply();

        Toast.makeText(this, "Password berhasil diubah!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void setButtonState(boolean enabled) {
        btnContinue.setEnabled(enabled);
        btnContinue.setAlpha(enabled ? 1.0f : 0.5f);
    }
}