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
import com.tren_lop.mobile_app_mini_db.data.entity.Showtime;
import com.tren_lop.mobile_app_mini_db.data.entity.Ticket;
import com.tren_lop.mobile_app_mini_db.utils.SessionManager;


public class SeatSelectionActivity extends AppCompatActivity {
    private int showtimeId = -1;
    private AppDatabase db;
    private SessionManager sessionManager;
    private TextView tvShowtimeInfo;
    private EditText etSeatNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        setTitle("Chọn Ngồi");

        if (getIntent() != null) {
            showtimeId = getIntent().getIntExtra("showtimeId", -1);
        }

        db = AppDatabase.getDatabase(this);
        sessionManager = new SessionManager(this);

        tvShowtimeInfo = findViewById(R.id.tvShowtimeInfo);
        etSeatNumber = findViewById(R.id.etSeatNumber);
        Button btnBook = findViewById(R.id.btnBook);

        loadShowtimeInfo();

        btnBook.setOnClickListener(v -> bookSeat());
    }

    private void loadShowtimeInfo() {
        if (showtimeId == -1) return;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Showtime st = db.showtimeDao().getShowtimeById(showtimeId);
            if (st != null) {
                var m = db.movieDao().getMovieById(st.movieId);
                var t = db.theaterDao().getTheaterById(st.theaterId);
                String info = (m != null ? m.title : "") + "\n" + (t != null ? t.name : "") + "\n" + st.time;
                runOnUiThread(() -> tvShowtimeInfo.setText("Booking for:\n" + info));
            }
        });
    }

    private void bookSeat() {
        String seat = etSeatNumber.getText().toString().trim();
        if (seat.isEmpty()) {
            Toast.makeText(this, "Please enter a seat number", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId == -1 || showtimeId == -1) {
            Toast.makeText(this, "Error in booking", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            long ticketId = db.ticketDao().insert(new Ticket(userId, showtimeId, seat));
            runOnUiThread(() -> {
                if (ticketId > 0) {
                    Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SeatSelectionActivity.this, TicketDetailActivity.class);
                    intent.putExtra("ticketId", (int) ticketId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Booking Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
