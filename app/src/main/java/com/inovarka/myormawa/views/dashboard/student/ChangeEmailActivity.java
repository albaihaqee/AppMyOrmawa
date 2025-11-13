package com.inovarka.myormawa.views.dashboard.student;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.inovarka.myormawa.R;
import com.inovarka.myormawa.views.auth.RegisterVerificationActivity;

public class ChangeEmailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextInputLayout tilEmail;
    private TextInputEditText edtEmail;
    private MaterialButton btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_change_email);

        initViews();
        setupListeners();
    }

    private void setStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_blue));
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tilEmail = findViewById(R.id.til_email);
        edtEmail = findViewById(R.id.edt_email);
        btnContinue = findViewById(R.id.btn_continue);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnContinue.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            Intent intent = new Intent(this, RegisterVerificationActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("from", "change_email");
            startActivity(intent);
        });
    }

    private void validateEmail(String email) {
        tilEmail.setError(null);

        if (email.isEmpty()) {
            setButtonState(false);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setButtonState(false);
            return;
        }

        boolean isValidDomain = email.endsWith("@gmail.com") ||
                email.endsWith("@student.polije.ac.id");
        setButtonState(isValidDomain);
    }

    private void setButtonState(boolean enabled) {
        btnContinue.setEnabled(enabled);
        btnContinue.setAlpha(enabled ? 1.0f : 0.5f);
    }
}