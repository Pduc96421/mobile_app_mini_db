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

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private AppDatabase db;
    private SessionManager sessionManager;
    private int showtimeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getIntent() != null) {
            showtimeId = getIntent().getIntExtra("showtimeId", -1);
        }

        db = AppDatabase.getDatabase(this);
        sessionManager = new SessionManager(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> registerUser());
        tvLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            long newId = db.userDao().insert(new User(username, password));
            runOnUiThread(() -> {
                if (newId > 0) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    sessionManager.createLoginSession((int) newId);
                    if (showtimeId != -1) {
                        Intent intent = new Intent(RegisterActivity.this, SeatSelectionActivity.class);
                        intent.putExtra("showtimeId", showtimeId);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
