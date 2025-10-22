package com.inovarka.myormawa.views.auth.login;

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
import com.inovarka.myormawa.views.auth.password.ForgotPasswordActivity;
import com.inovarka.myormawa.views.auth.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar();
        setContentView(R.layout.activity_login);
        initViews();
        loadEmailFromIntent();
    }

    private void setWhiteStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initViews() {
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        edtEmail.addTextChangedListener(createErrorClearer(tilEmail));
        edtPassword.addTextChangedListener(createErrorClearer(tilPassword));

        findViewById(R.id.btn_login).setOnClickListener(v -> handleLogin());
        findViewById(R.id.txt_register).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
        findViewById(R.id.txt_forgotpw).setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void loadEmailFromIntent() {
        // Get email dari Intent (dari RegisterSuccessActivity)
        String email = getIntent().getStringExtra("registered_email");
        if (email != null && !email.isEmpty()) {
            edtEmail.setText(email);
            edtPassword.requestFocus();
        }
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

    private void handleLogin() {
        if (validateInputs()) {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // TODO: Implement API call
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

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