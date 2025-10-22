package com.inovarka.myormawa.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.inovarka.myormawa.R;

public class SetNewPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilPassword, tilConfirmPassword;
    private TextInputEditText edtPassword, edtConfirmPassword;
    private String email;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar();
        setContentView(R.layout.activity_set_new_password);

        email = getIntent().getStringExtra("email");
        verificationCode = getIntent().getStringExtra("code");

        initViews();
    }

    private void setWhiteStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initViews() {
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilConfirmPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_update_password).setOnClickListener(v -> handleUpdatePassword());
    }

    private void handleUpdatePassword() {
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (!validateInputs(password, confirmPassword)) {
            return;
        }

        // TODO: Implement API call to update password
        Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ResetPasswordSuccessActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private boolean validateInputs(String password, String confirmPassword) {
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

        if (confirmPassword.isEmpty()) {
            tilConfirmPassword.setError("Please confirm your password");
            edtConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            edtConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}