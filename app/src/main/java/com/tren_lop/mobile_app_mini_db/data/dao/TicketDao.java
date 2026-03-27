package com.tren_lop.mobile_app_mini_db.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tren_lop.mobile_app_mini_db.data.entity.Ticket;

import java.util.List;

@Dao
public interface TicketDao {
    @Insert
    long insert(Ticket ticket);

    @Query("SELECT * FROM tickets WHERE userId = :userId")
    List<Ticket> getTicketsByUser(int userId);

    @Query("SELECT * FROM tickets WHERE id = :id LIMIT 1")
    Ticket getTicketById(int id);

    @Query("SELECT seatNumber FROM tickets WHERE showtimeId = :showtimeId")
    List<String> getBookedSeatsByShowtime(int showtimeId);

    @Query("SELECT COUNT(*) FROM tickets WHERE showtimeId = :showtimeId AND LOWER(seatNumber) = LOWER(:seatNumber)")
    int countSeatBookings(int showtimeId, String seatNumber);
}
