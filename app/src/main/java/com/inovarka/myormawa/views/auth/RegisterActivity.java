package com.inovarka.myormawa.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.inovarka.myormawa.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilNim, tilFullname, tilEmail, tilProdi, tilPassword;
    private TextInputEditText edtNim, edtFullname, edtEmail, edtProdi, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar();
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void setWhiteStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initViews() {
        tilNim = findViewById(R.id.til_nim);
        tilFullname = findViewById(R.id.til_fullname);
        tilEmail = findViewById(R.id.til_email);
        tilProdi = findViewById(R.id.til_prodi);
        tilPassword = findViewById(R.id.til_password);

        edtNim = findViewById(R.id.edt_nim);
        edtFullname = findViewById(R.id.edt_fullname);
        edtEmail = findViewById(R.id.edt_email);
        edtProdi = findViewById(R.id.edt_prodi);
        edtPassword = findViewById(R.id.edt_password);

        edtNim.addTextChangedListener(createErrorClearer(tilNim));
        edtFullname.addTextChangedListener(createErrorClearer(tilFullname));
        edtEmail.addTextChangedListener(createErrorClearer(tilEmail));
        edtProdi.addTextChangedListener(createErrorClearer(tilProdi));
        edtPassword.addTextChangedListener(createErrorClearer(tilPassword));

        findViewById(R.id.btn_register).setOnClickListener(v -> handleRegister());
        findViewById(R.id.txt_login).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private TextWatcher createErrorClearer(TextInputLayout layout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    private void handleRegister() {
        if (!validateInputs()) return;

        String nim = edtNim.getText().toString().trim();
        String fullname = edtFullname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String prodi = edtProdi.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // TODO: Implement API call untuk register
        Toast.makeText(this, "Verification code sent to " + email, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, RegisterVerificationActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("nim", nim);
        intent.putExtra("fullname", fullname);
        intent.putExtra("prodi", prodi);
        startActivity(intent);
    }

    private boolean validateInputs() {
        String nim = edtNim.getText().toString().trim();
        String fullname = edtFullname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String prodi = edtProdi.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (nim.isEmpty()) {
            tilNim.setError("NIM is required");
            edtNim.requestFocus();
            return false;
        }

        if (nim.length() < 8) {
            tilNim.setError("NIM must be at least 8 digits");
            edtNim.requestFocus();
            return false;
        }

        if (fullname.isEmpty()) {
            tilFullname.setError("Full name is required");
            edtFullname.requestFocus();
            return false;
        }

        if (fullname.length() < 3) {
            tilFullname.setError("Full name must be at least 3 characters");
            edtFullname.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            tilEmail.setError("Email is required");
            edtEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Invalid email format");
            edtEmail.requestFocus();
            return false;
        }

        if (prodi.isEmpty()) {
            tilProdi.setError("Program study is required");
            edtProdi.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            tilPassword.setError("Password is required");
            edtPassword.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            tilPassword.setError("Password must be at least 8 characters");
            edtPassword.requestFocus();
            return false;
        }

        return true;
    }
}