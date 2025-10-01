package com.inovarka.myormawa.views.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.inovarka.myormawa.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText editNim, editFullname, editEmail, editPassword;
    private Spinner spinnerProdi;
    private Button btnRegister;
    private String selectedProdi = "";
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        setupWindowInsets();
        initializeViews();
        setupSpinner();
        setupPasswordToggle();
        setupRegisterButton();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        editNim = findViewById(R.id.edt_nim);
        editFullname = findViewById(R.id.edt_fullname);
        editEmail = findViewById(R.id.edt_emailreg);
        spinnerProdi = findViewById(R.id.spin_prodi);
        editPassword = findViewById(R.id.edt_passwordreg);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void setupSpinner() {
        String[] items = getResources().getStringArray(R.array.spinner_prodi);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.item_spinner_selected, items) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(position == 0 ? 0xFF999999 : 0xFF000000);
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinnerProdi.setAdapter(adapter);

        spinnerProdi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(position == 0 ? 0xFF999999 : 0xFF000000);
                }
                selectedProdi = position > 0 ? parent.getItemAtPosition(position).toString() : "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedProdi = "";
            }
        });
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

    private void setupRegisterButton() {
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        if (!validateForm()) return;

        String nim = editNim.getText().toString().trim();
        String fullname = editFullname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // TODO: Implement register logic here (API call, database insert, etc.)
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateForm() {
        if (isEmpty(editNim, "NIM is required")) return false;
        if (isEmpty(editFullname, "Full name is required")) return false;
        if (isEmpty(editEmail, "Email is required")) return false;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString().trim()).matches()) {
            editEmail.requestFocus();
            editEmail.setError("Please enter a valid email");
            return false;
        }

        if (selectedProdi.isEmpty()) {
            Toast.makeText(this, "Please select a study program", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isEmpty(editPassword, "Password is required")) return false;

        if (editPassword.getText().toString().trim().length() < 6) {
            editPassword.requestFocus();
            editPassword.setError("Password must be at least 6 characters");
            return false;
        }

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