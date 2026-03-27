package com.tren_lop.mobile_app_mini_db.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tren_lop.mobile_app_mini_db.R;
import com.tren_lop.mobile_app_mini_db.data.AppDatabase;
import com.tren_lop.mobile_app_mini_db.data.entity.Ticket;
import com.tren_lop.mobile_app_mini_db.utils.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketHistoryActivity extends AppCompatActivity {
    private AppDatabase db;
    private SessionManager sessionManager;
    private TicketHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Lịch sử vé đã đặt");

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketHistoryAdapter();
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getDatabase(this);
        loadTicketHistory();
    }

    private void loadTicketHistory() {
        int userId = sessionManager.getUserId();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Ticket> tickets = db.ticketDao().getTicketsByUser(userId);
            List<TicketHistoryAdapter.TicketHistoryItem> items = new ArrayList<>();

            for (Ticket ticket : tickets) {
                var st = db.showtimeDao().getShowtimeById(ticket.showtimeId);
                if (st == null) continue;

                var movie = db.movieDao().getMovieById(st.movieId);
                var theater = db.theaterDao().getTheaterById(st.theaterId);
                String movieTitle = movie != null ? movie.title : "Unknown Movie";
                String theaterName = theater != null ? theater.name : "Unknown Theater";
                items.add(new TicketHistoryAdapter.TicketHistoryItem(
                    ticket.id,
                    movieTitle,
                    theaterName,
                    st.time,
                    ticket.seatNumber
                ));
            }

            Collections.reverse(items);
            runOnUiThread(() -> {
                adapter.setItems(items);
                if (items.isEmpty()) {
                    Toast.makeText(this, "Bạn chưa đặt vé nào", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
