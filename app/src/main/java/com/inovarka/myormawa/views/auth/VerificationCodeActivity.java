package com.inovarka.myormawa.views.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.inovarka.myormawa.R;

public class VerificationCodeActivity extends AppCompatActivity {

    private EditText[] codeInputs = new EditText[6];
    private TextView txtEmailDisplay, txtEmailInfo;
    private ProgressBar progressLoading;
    private String email;
    private boolean isVerifying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar();
        setContentView(R.layout.activity_verification_code);

        email = getIntent().getStringExtra("email");
        initViews();
        setupCodeInputs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetCodeInputs();
    }

    private void setWhiteStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initViews() {
        txtEmailDisplay = findViewById(R.id.txt_email_display);
        txtEmailInfo = findViewById(R.id.txt_email_info);
        progressLoading = findViewById(R.id.progress_loading);

        codeInputs[0] = findViewById(R.id.edt_code_1);
        codeInputs[1] = findViewById(R.id.edt_code_2);
        codeInputs[2] = findViewById(R.id.edt_code_3);
        codeInputs[3] = findViewById(R.id.edt_code_4);
        codeInputs[4] = findViewById(R.id.edt_code_5);
        codeInputs[5] = findViewById(R.id.edt_code_6);

        if (email != null) {
            txtEmailDisplay.setText(email);
            txtEmailInfo.setText("We sent a reset link to");
        }

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_verify_code).setOnClickListener(v -> handleVerifyCode());
        findViewById(R.id.txt_resend).setOnClickListener(v -> handleResend());
    }

    private void setupCodeInputs() {
        for (int i = 0; i < codeInputs.length; i++) {
            final int index = i;

            codeInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < codeInputs.length - 1) {
                        codeInputs[index + 1].requestFocus();
                    } else if (s.length() == 1 && index == codeInputs.length - 1) {
                        if (isAllFieldsFilled()) {
                            autoVerifyCode();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            codeInputs[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (codeInputs[index].getText().toString().isEmpty() && index > 0) {
                        codeInputs[index - 1].requestFocus();
                        codeInputs[index - 1].setText("");
                    }
                }
                return false;
            });
        }
    }

    private void resetCodeInputs() {
        for (EditText input : codeInputs) {
            input.setText("");
            input.setEnabled(true);
        }
        codeInputs[0].requestFocus();
        findViewById(R.id.btn_verify_code).setEnabled(true);
        progressLoading.setVisibility(View.GONE);
        isVerifying = false;
    }

    private boolean isAllFieldsFilled() {
        for (EditText input : codeInputs) {
            if (input.getText().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void autoVerifyCode() {
        if (isVerifying) return;

        setCodeInputsEnabled(false);
        progressLoading.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_verify_code).setEnabled(false);
        isVerifying = true;

        StringBuilder code = new StringBuilder();
        for (EditText input : codeInputs) {
            code.append(input.getText().toString().trim());
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            verifyCodeSuccess(code.toString());
        }, 1500);
    }

    private void handleVerifyCode() {
        StringBuilder code = new StringBuilder();
        for (EditText input : codeInputs) {
            String digit = input.getText().toString().trim();
            if (digit.isEmpty()) {
                Toast.makeText(this, "Please enter complete verification code", Toast.LENGTH_SHORT).show();
                return;
            }
            code.append(digit);
        }

        setCodeInputsEnabled(false);
        progressLoading.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_verify_code).setEnabled(false);
        isVerifying = true;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            verifyCodeSuccess(code.toString());
        }, 1500);
    }

    private void verifyCodeSuccess(String code) {
        progressLoading.setVisibility(View.GONE);

        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("code", code);
        startActivity(intent);
    }

    private void setCodeInputsEnabled(boolean enabled) {
        for (EditText input : codeInputs) {
            input.setEnabled(enabled);
        }
    }

    private void handleResend() {
        Toast.makeText(this, "Verification code resent to " + email, Toast.LENGTH_SHORT).show();
        resetCodeInputs();
    }
}