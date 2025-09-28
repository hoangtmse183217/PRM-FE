package com.example.prm_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_project.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Map<String, String> mockUserDatabase;
    private ActivityResultLauncher<Intent> registerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupMockUsers();
        setupRegisterLauncher();
        setupClickListeners();
    }

    private void setupMockUsers() {
        mockUserDatabase = new HashMap<>();
        mockUserDatabase.put("user1@app.com", "password123");
        mockUserDatabase.put("user2@app.com", "securepass");
        mockUserDatabase.put("admin@app.com", "admin123");
    }

    private void setupRegisterLauncher() {
        registerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Sử dụng hằng số để lấy dữ liệu, tránh "magic strings"
                            String email = data.getStringExtra(RegisterActivity.EXTRA_EMAIL);
                            String password = data.getStringExtra(RegisterActivity.EXTRA_PASSWORD);

                            // Thêm người dùng mới vào database (đã được đảm bảo là không trùng lặp)
                            mockUserDatabase.put(email, password);

                            binding.emailEditText.setText(email);
                            binding.passwordEditText.setText(password);
                            Toast.makeText(LoginActivity.this, "Tạo tài khoản thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setupClickListeners() {
        binding.loginButton.setOnClickListener(v -> handleLogin());

        binding.googleSignInButton.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Chức năng đăng nhập Google sẽ được phát triển sau!", Toast.LENGTH_SHORT).show();
        });

        binding.signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            // Gửi danh sách các email đã tồn tại sang RegisterActivity
            intent.putStringArrayListExtra("EXISTING_EMAILS", new ArrayList<>(mockUserDatabase.keySet()));
            registerLauncher.launch(intent);
        });
    }

    private void handleLogin() {
        // Chuyển email về chữ thường
        String email = binding.emailEditText.getText().toString().trim().toLowerCase();
        String password = binding.passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) { /* ... validation giữ nguyên ... */ return; }
        if (TextUtils.isEmpty(password)) { /* ... validation giữ nguyên ... */ return; }

        if (mockUserDatabase.containsKey(email)) {
            String correctPassword = mockUserDatabase.get(email);
            if (password.equals(correctPassword)) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Mật khẩu không chính xác.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Tài khoản không tồn tại.", Toast.LENGTH_LONG).show();
        }
    }
}