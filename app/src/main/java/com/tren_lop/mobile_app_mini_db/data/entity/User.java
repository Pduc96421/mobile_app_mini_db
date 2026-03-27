package com.tren_lop.mobile_app_mini_db.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
