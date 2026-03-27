package com.tren_lop.mobile_app_mini_db.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tren_lop.mobile_app_mini_db.R;
import com.tren_lop.mobile_app_mini_db.data.AppDatabase;
import com.tren_lop.mobile_app_mini_db.data.entity.Movie;
import com.tren_lop.mobile_app_mini_db.data.entity.Showtime;
import com.tren_lop.mobile_app_mini_db.data.entity.Theater;
import com.tren_lop.mobile_app_mini_db.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class ShowtimesActivity extends AppCompatActivity {
    private ShowtimeAdapter adapter;
    private AppDatabase db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Lịch chiếu");

        sessionManager = new SessionManager(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShowtimeAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setListener(item -> {
            if (sessionManager.isLoggedIn()) {
                Intent intent = new Intent(ShowtimesActivity.this, SeatSelectionActivity.class);
                intent.putExtra("showtimeId", item.showtimeId);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ShowtimesActivity.this, LoginActivity.class);
                intent.putExtra("showtimeId", item.showtimeId);
                startActivity(intent);
            }
        });

        db = AppDatabase.getDatabase(this);
        loadShowtimes();
    }

    private void loadShowtimes() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Showtime> showtimes = db.showtimeDao().getAllShowtimes();
            List<ShowtimeAdapter.ShowtimeItem> items = new ArrayList<>();
            for (Showtime st : showtimes) {
                Movie m = db.movieDao().getMovieById(st.movieId);
                Theater t = db.theaterDao().getTheaterById(st.theaterId);
                String mTitle = m != null ? m.title : "Unknown Movie";
                String tName = t != null ? t.name : "Unknown Theater";
                items.add(new ShowtimeAdapter.ShowtimeItem(st.id, mTitle, tName, st.time));
            }
            runOnUiThread(() -> adapter.setItems(items));
        });
    }
}
