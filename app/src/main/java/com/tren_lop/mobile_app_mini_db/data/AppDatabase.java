package com.tren_lop.mobile_app_mini_db.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tren_lop.mobile_app_mini_db.data.dao.MovieDao;
import com.tren_lop.mobile_app_mini_db.data.dao.ShowtimeDao;
import com.tren_lop.mobile_app_mini_db.data.dao.TheaterDao;
import com.tren_lop.mobile_app_mini_db.data.dao.TicketDao;
import com.tren_lop.mobile_app_mini_db.data.dao.UserDao;
import com.tren_lop.mobile_app_mini_db.data.entity.Movie;
import com.tren_lop.mobile_app_mini_db.data.entity.Showtime;
import com.tren_lop.mobile_app_mini_db.data.entity.Theater;
import com.tren_lop.mobile_app_mini_db.data.entity.Ticket;
import com.tren_lop.mobile_app_mini_db.data.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Movie.class, Theater.class, Showtime.class, Ticket.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract TheaterDao theaterDao();
    public abstract ShowtimeDao showtimeDao();
    public abstract TicketDao ticketDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "movie_ticket_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                MovieDao dao = INSTANCE.movieDao();
                dao.insertAll(
                    new Movie("Avengers: Endgame", "Epic conclusion to the MCU infinity saga.", 181),
                    new Movie("Inception", "A thief who steals corporate secrets through the use of dream-sharing technology.", 148),
                    new Movie("Interstellar", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", 169)
                );

                TheaterDao thDao = INSTANCE.theaterDao();
                thDao.insertAll(
                    new Theater("CGV Vincom", "District 1, HCMC"),
                    new Theater("Galaxy Cinema", "District 3, HCMC")
                );

                ShowtimeDao stDao = INSTANCE.showtimeDao();
                // Assumes IDs are generated sequentially 1, 2, 3...
                stDao.insertAll(
                    new Showtime(1, 1, "10:00 AM"),
                    new Showtime(1, 1, "02:00 PM"),
                    new Showtime(2, 2, "01:30 PM"),
                    new Showtime(3, 1, "04:00 PM")
                );
            });
        }
    };
}
