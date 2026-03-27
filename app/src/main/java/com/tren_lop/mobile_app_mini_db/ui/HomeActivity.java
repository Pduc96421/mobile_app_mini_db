package com.tren_lop.mobile_app_mini_db.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.tren_lop.mobile_app_mini_db.R;
import com.tren_lop.mobile_app_mini_db.utils.SessionManager;

public class HomeActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);

        Button btnMovies = findViewById(R.id.btnMovies);
        Button btnTheaters = findViewById(R.id.btnTheaters);
        Button btnShowtimes = findViewById(R.id.btnShowtimes);
        Button btnTicketHistory = findViewById(R.id.btnTicketHistory);
        btnProfile = findViewById(R.id.btnProfile);

        btnMovies.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MoviesActivity.class)));
        btnTheaters.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, TheatersActivity.class)));
        btnShowtimes.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ShowtimesActivity.class)));
        btnTicketHistory.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(HomeActivity.this, TicketHistoryActivity.class));
            } else {
                Toast.makeText(this, "Vui lòng đăng nhập để xem lịch sử vé", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        updateProfileButton();

        btnProfile.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                sessionManager.logoutUser();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                updateProfileButton();
            } else {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileButton();
    }

    private void updateProfileButton() {
        if (sessionManager.isLoggedIn()) {
            btnProfile.setText("Logout");
        } else {
            btnProfile.setText("Login");
        }
    }
}
