package com.tren_lop.mobile_app_mini_db.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tren_lop.mobile_app_mini_db.R;
import com.tren_lop.mobile_app_mini_db.data.AppDatabase;
import com.tren_lop.mobile_app_mini_db.data.entity.Ticket;


public class TicketDetailActivity extends AppCompatActivity {
    private int ticketId = -1;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        if (getIntent() != null) {
            ticketId = getIntent().getIntExtra("ticketId", -1);
        }

        db = AppDatabase.getDatabase(this);

        Button btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(TicketDetailActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        loadTicketDetails();
    }

    private void loadTicketDetails() {
        if (ticketId == -1) return;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Ticket t = db.ticketDao().getTicketById(ticketId);
            if (t != null) {
                var st = db.showtimeDao().getShowtimeById(t.showtimeId);
                var m = db.movieDao().getMovieById(st.movieId);
                var theater = db.theaterDao().getTheaterById(st.theaterId);
                runOnUiThread(() -> {
                    ((TextView) findViewById(R.id.tvTicketId)).setText("Ticket #" + t.id);
                    ((TextView) findViewById(R.id.tvMovieTitle)).setText(m != null ? m.title : "");
                    ((TextView) findViewById(R.id.tvLocationAndTime)).setText(
                        (theater != null ? theater.name : "") + " | " + st.time
                    );
                    ((TextView) findViewById(R.id.tvSeat)).setText("Seat: " + t.seatNumber);
                });
            }
        });
    }
}
