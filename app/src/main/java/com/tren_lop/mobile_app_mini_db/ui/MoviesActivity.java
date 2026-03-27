package com.tren_lop.mobile_app_mini_db.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tren_lop.mobile_app_mini_db.R;
import com.tren_lop.mobile_app_mini_db.data.AppDatabase;

public class MoviesActivity extends AppCompatActivity {
    private MovieAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Danh sách Movies");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter();
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getDatabase(this);
        loadMovies();
    }

    private void loadMovies() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            var movies = db.movieDao().getAllMovies();
            runOnUiThread(() -> adapter.setMovies(movies));
        });
    }
}
