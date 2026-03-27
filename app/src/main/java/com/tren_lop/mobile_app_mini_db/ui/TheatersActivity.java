package com.tren_lop.mobile_app_mini_db.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tren_lop.mobile_app_mini_db.R;
import com.tren_lop.mobile_app_mini_db.data.AppDatabase;

public class TheatersActivity extends AppCompatActivity {
    private TheaterAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Danh sách Rạp chiếu");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TheaterAdapter();
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getDatabase(this);
        loadTheaters();
    }

    private void loadTheaters() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            var theaters = db.theaterDao().getAllTheaters();
            runOnUiThread(() -> adapter.setTheaters(theaters));
        });
    }
}
