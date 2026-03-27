package com.tren_lop.mobile_app_mini_db.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tren_lop.mobile_app_mini_db.data.entity.Showtime;

import java.util.List;

@Dao
public interface ShowtimeDao {
    @Insert
    void insertAll(Showtime... showtimes);

    @Query("SELECT * FROM showtimes")
    List<Showtime> getAllShowtimes();

    @Query("SELECT * FROM showtimes WHERE movieId = :movieId")
    List<Showtime> getShowtimesByMovie(int movieId);

    @Query("SELECT * FROM showtimes WHERE id = :id LIMIT 1")
    Showtime getShowtimeById(int id);
}
