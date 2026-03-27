package com.tren_lop.mobile_app_mini_db.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tren_lop.mobile_app_mini_db.data.entity.User;


@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);
    
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(int userId);
}
