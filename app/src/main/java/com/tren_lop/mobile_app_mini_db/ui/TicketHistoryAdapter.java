package com.tren_lop.mobile_app_mini_db.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tren_lop.mobile_app_mini_db.R;

import java.util.ArrayList;
import java.util.List;

public class TicketHistoryAdapter extends RecyclerView.Adapter<TicketHistoryAdapter.TicketHistoryViewHolder> {
    private List<TicketHistoryItem> items = new ArrayList<>();

    public void setItems(List<TicketHistoryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_history, parent, false);
        return new TicketHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHistoryViewHolder holder, int position) {
        TicketHistoryItem item = items.get(position);
        holder.tvTicketId.setText("Ticket #" + item.ticketId);
        holder.tvMovieTitle.setText(item.movieTitle);
        holder.tvTheaterAndTime.setText(item.theaterName + " | " + item.time);
        holder.tvSeat.setText("Seat: " + item.seat);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TicketHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketId;
        TextView tvMovieTitle;
        TextView tvTheaterAndTime;
        TextView tvSeat;

        public TicketHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketId = itemView.findViewById(R.id.tvTicketId);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvTheaterAndTime = itemView.findViewById(R.id.tvTheaterAndTime);
            tvSeat = itemView.findViewById(R.id.tvSeat);
        }
    }

    public static class TicketHistoryItem {
        public int ticketId;
        public String movieTitle;
        public String theaterName;
        public String time;
        public String seat;

        public TicketHistoryItem(int ticketId, String movieTitle, String theaterName, String time, String seat) {
            this.ticketId = ticketId;
            this.movieTitle = movieTitle;
            this.theaterName = theaterName;
            this.time = time;
            this.seat = seat;
        }
    }
}
