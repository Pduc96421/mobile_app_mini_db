package com.tren_lop.mobile_app_mini_db.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "showtimes",
    foreignKeys = {
        @ForeignKey(entity = Movie.class, parentColumns = "id", childColumns = "movieId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Theater.class, parentColumns = "id", childColumns = "theaterId", onDelete = ForeignKey.CASCADE)
    },
    indices = {@Index("movieId"), @Index("theaterId")}
)
public class Showtime {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int movieId;
    public int theaterId;
    public String time;

    public Showtime(int movieId, int theaterId, String time) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.time = time;
    }
}
