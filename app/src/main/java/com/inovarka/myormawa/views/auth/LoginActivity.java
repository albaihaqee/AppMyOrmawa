package com.inovarka.myormawa.views.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.inovarka.myormawa.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        setupWindowInsets();
        initializeViews();
        setupPasswordToggle();
        setupLoginButton();
        setupRegisterLink();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        editEmail = findViewById(R.id.edt_email);
        editPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.txt_register);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPasswordToggle() {
        editPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int iconWidth = editPassword.getCompoundDrawables()[2].getBounds().width();
                if (event.getRawX() >= (editPassword.getRight() - iconWidth - 50)) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_hideeye, 0);
        } else {
            editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_showeye, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        editPassword.setSelection(editPassword.getText().length());
    }

    private void setupLoginButton() {
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void setupRegisterLink() {
        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        if (!validateForm()) return;

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // TODO: Implement login logic here (API call, database check, etc.)
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateForm() {
        if (isEmpty(editEmail, "Email is required")) return false;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString().trim()).matches()) {
            editEmail.requestFocus();
            editEmail.setError("Please enter a valid email");
            return false;
        }

        if (isEmpty(editPassword, "Password is required")) return false;

        return true;
    }

    private boolean isEmpty(EditText editText, String errorMessage) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.requestFocus();
            editText.setError(errorMessage);
            return true;
        }
        return false;
    }
}