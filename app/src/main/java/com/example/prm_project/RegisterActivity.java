package com.example.prm_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_project.databinding.ActivityRegisterBinding;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ArrayList<String> existingEmails;

    // Định nghĩa hằng số để tránh "magic strings"
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận danh sách email đã tồn tại từ LoginActivity
        existingEmails = getIntent().getStringArrayListExtra("EXISTING_EMAILS");
        if (existingEmails == null) {
            existingEmails = new ArrayList<>(); // Tránh lỗi NullPointerException
        }

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.signInTextView.setOnClickListener(v -> finish());
        binding.registerButton.setOnClickListener(v -> handleRegistration());
    }

    private void handleRegistration() {
        String fullName = binding.fullNameEditText.getText().toString().trim();
        // Chuyển email về chữ thường
        String email = binding.emailEditText.getText().toString().trim().toLowerCase();
        String password = binding.passwordEditText.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString().trim();

        // ... validation cho fullName, password, confirmPassword giữ nguyên ...
        if (TextUtils.isEmpty(fullName)) { /* ... */ return; }
        if (TextUtils.isEmpty(email)) { /* ... */ return; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { /* ... */ return; }

        // ==>> KIỂM TRA EMAIL TỒN TẠI (ĐÃ THÊM) <<==
        if (existingEmails.contains(email)) {
            Toast.makeText(this, "Email này đã được sử dụng", Toast.LENGTH_SHORT).show();
            binding.emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) { /* ... */ return; }
        if (!password.equals(confirmPassword)) { /* ... */ return; }

        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_EMAIL, email); // Dùng hằng số
        resultIntent.putExtra(EXTRA_PASSWORD, password); // Dùng hằng số
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}