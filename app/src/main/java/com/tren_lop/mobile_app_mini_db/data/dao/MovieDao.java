package com.tren_lop.mobile_app_mini_db.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tren_lop.mobile_app_mini_db.data.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insertAll(Movie... movies);

    @Query("SELECT * FROM movies")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    Movie getMovieById(int id);
}
