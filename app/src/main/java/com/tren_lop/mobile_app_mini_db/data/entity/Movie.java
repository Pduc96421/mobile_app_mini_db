package com.tren_lop.mobile_app_mini_db.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public int duration;

    public Movie(String title, String description, int duration) {
        this.title = title;
        this.description = description;
        this.duration = duration;
    }
}
