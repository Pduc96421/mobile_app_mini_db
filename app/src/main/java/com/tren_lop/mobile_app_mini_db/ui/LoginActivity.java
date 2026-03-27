package com.tren_lop.mobile_app_mini_db.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.tren_lop.mobile_app_mini_db.R;
import com.tren_lop.mobile_app_mini_db.data.AppDatabase;
import com.tren_lop.mobile_app_mini_db.data.entity.User;
import com.tren_lop.mobile_app_mini_db.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private SessionManager sessionManager;
    private AppDatabase db;
    private int showtimeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent() != null) {
            showtimeId = getIntent().getIntExtra("showtimeId", -1);
        }

        sessionManager = new SessionManager(this);
        db = AppDatabase.getDatabase(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> loginUser());
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("showtimeId", showtimeId);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = db.userDao().login(username, password);
            runOnUiThread(() -> {
                if (user != null) {
                    sessionManager.createLoginSession(user.id);
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    if (showtimeId != -1) {
                        Intent intent = new Intent(LoginActivity.this, SeatSelectionActivity.class);
                        intent.putExtra("showtimeId", showtimeId);
                        startActivity(intent);
                    } else {
                        // Go back to home
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
