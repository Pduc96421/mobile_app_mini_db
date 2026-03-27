package com.tren_lop.mobile_app_mini_db.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tren_lop.mobile_app_mini_db.R;
import com.tren_lop.mobile_app_mini_db.data.AppDatabase;
import com.tren_lop.mobile_app_mini_db.data.entity.Showtime;
import com.tren_lop.mobile_app_mini_db.data.entity.Ticket;
import com.tren_lop.mobile_app_mini_db.utils.SessionManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class SeatSelectionActivity extends AppCompatActivity {
    private int showtimeId = -1;
    private AppDatabase db;
    private SessionManager sessionManager;
    private TextView tvShowtimeInfo;
    private TextView tvSelectedSeat;
    private final Set<String> selectedSeats = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        setTitle("Chọn Ngồi");

        if (getIntent() != null) {
            showtimeId = getIntent().getIntExtra("showtimeId", -1);
        }

        db = AppDatabase.getDatabase(this);
        sessionManager = new SessionManager(this);

        tvShowtimeInfo = findViewById(R.id.tvShowtimeInfo);
        tvSelectedSeat = findViewById(R.id.tvSelectedSeat);
        Button btnPickSeat = findViewById(R.id.btnPickSeat);
        Button btnBook = findViewById(R.id.btnBook);

        loadShowtimeInfo();

        btnPickSeat.setOnClickListener(v -> showSeatSelectionDialog());
        btnBook.setOnClickListener(v -> bookSeat());

        tvShowtimeInfo.post(this::showSeatSelectionDialog);
    }

    private void loadShowtimeInfo() {
        if (showtimeId == -1) return;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Showtime st = db.showtimeDao().getShowtimeById(showtimeId);
            if (st != null) {
                var m = db.movieDao().getMovieById(st.movieId);
                var t = db.theaterDao().getTheaterById(st.theaterId);
                String info = (m != null ? m.title : "") + "\n" + (t != null ? t.name : "") + "\n" + st.time;
                runOnUiThread(() -> tvShowtimeInfo.setText("Booking for:\n" + info));
            }
        });
    }

    private void showSeatSelectionDialog() {
        if (showtimeId == -1) {
            Toast.makeText(this, "Showtime không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            var bookedSeats = db.ticketDao().getBookedSeatsByShowtime(showtimeId);
            var bookedSeatSet = new java.util.HashSet<String>();
            for (String seat : bookedSeats) {
                if (seat != null) bookedSeatSet.add(seat.toUpperCase());
            }

            runOnUiThread(() -> {
                AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Chọn ghế")
                    .create();

                ScrollView scrollView = new ScrollView(this);
                LinearLayout container = new LinearLayout(this);
                container.setOrientation(LinearLayout.VERTICAL);
                int padding = 24;
                container.setPadding(padding, padding, padding, padding);

                GridLayout gridLayout = new GridLayout(this);
                gridLayout.setColumnCount(6);

                for (char row = 'A'; row <= 'E'; row++) {
                    for (int col = 1; col <= 6; col++) {
                        String seat = row + String.valueOf(col);
                        Button seatButton = new Button(this);
                        seatButton.setText(seat);

                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = 160;
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        params.setMargins(8, 8, 8, 8);
                        seatButton.setLayoutParams(params);

                        if (bookedSeatSet.contains(seat)) {
                            seatButton.setBackgroundColor(Color.parseColor("#E53935"));
                            seatButton.setTextColor(Color.WHITE);
                            seatButton.setEnabled(false);
                        } else {
                            if (selectedSeats.contains(seat)) {
                                seatButton.setBackgroundColor(Color.parseColor("#1E88E5"));
                            } else {
                                seatButton.setBackgroundColor(Color.parseColor("#43A047"));
                            }
                            seatButton.setTextColor(Color.WHITE);
                            seatButton.setOnClickListener(v -> {
                                if (selectedSeats.contains(seat)) {
                                    selectedSeats.remove(seat);
                                    seatButton.setBackgroundColor(Color.parseColor("#43A047"));
                                } else {
                                    selectedSeats.add(seat);
                                    seatButton.setBackgroundColor(Color.parseColor("#1E88E5"));
                                }
                            });
                        }
                        gridLayout.addView(seatButton);
                    }
                }

                container.addView(gridLayout);
                scrollView.addView(container);
                dialog.setView(scrollView);
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Xác nhận", (d, which) -> {
                    if (selectedSeats.isEmpty()) {
                        tvSelectedSeat.setText("Chưa chọn ghế");
                    } else {
                        tvSelectedSeat.setText("Ghế đã chọn: " + TextUtils.join(", ", selectedSeats));
                    }
                });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Đóng", (d, which) -> d.dismiss());
                dialog.show();
            });
        });
    }

    private void bookSeat() {
        if (selectedSeats.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 ghế", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId == -1 || showtimeId == -1) {
            Toast.makeText(this, "Error in booking", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<String> seatsToBook = new ArrayList<>(selectedSeats);
            int successCount = 0;
            int failedCount = 0;
            long firstTicketId = -1;

            for (String seat : seatsToBook) {
                int existingBookings = db.ticketDao().countSeatBookings(showtimeId, seat);
                if (existingBookings > 0) {
                    failedCount++;
                    continue;
                }

                long ticketId = db.ticketDao().insert(new Ticket(userId, showtimeId, seat));
                if (ticketId > 0) {
                    if (firstTicketId == -1) firstTicketId = ticketId;
                    successCount++;
                } else {
                    failedCount++;
                }
            }

            int finalSuccessCount = successCount;
            int finalFailedCount = failedCount;
            long finalFirstTicketId = firstTicketId;
            runOnUiThread(() -> {
                if (finalSuccessCount > 0 && finalFailedCount == 0 && finalSuccessCount == 1 && finalFirstTicketId > 0) {
                    Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SeatSelectionActivity.this, TicketDetailActivity.class);
                    intent.putExtra("ticketId", (int) finalFirstTicketId);
                    startActivity(intent);
                    finish();
                } else if (finalSuccessCount > 0) {
                    Toast.makeText(
                        this,
                        "Đặt thành công " + finalSuccessCount + " ghế, " + finalFailedCount + " ghế đã có người mua.",
                        Toast.LENGTH_LONG
                    ).show();
                    selectedSeats.clear();
                    tvSelectedSeat.setText("Chưa chọn ghế");
                    showSeatSelectionDialog();
                } else {
                    Toast.makeText(this, "Không thể đặt ghế đã chọn", Toast.LENGTH_SHORT).show();
                    showSeatSelectionDialog();
                }
            });
        });
    }
}
