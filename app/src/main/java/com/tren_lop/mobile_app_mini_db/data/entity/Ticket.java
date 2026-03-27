package com.tren_lop.mobile_app_mini_db.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "tickets",
    foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Showtime.class, parentColumns = "id", childColumns = "showtimeId", onDelete = ForeignKey.CASCADE)
    },
    indices = {@Index("userId"), @Index("showtimeId")}
)
public class Ticket {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public int showtimeId;
    public String seatNumber;

    public Ticket(int userId, int showtimeId, String seatNumber) {
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
    }
}
