package com.tren_lop.mobile_app_mini_db.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tren_lop.mobile_app_mini_db.data.entity.Theater;

import java.util.List;

@Dao
public interface TheaterDao {
    @Insert
    void insertAll(Theater... theaters);

    @Query("SELECT * FROM theaters")
    List<Theater> getAllTheaters();
    
    @Query("SELECT * FROM theaters WHERE id = :id LIMIT 1")
    Theater getTheaterById(int id);
}
