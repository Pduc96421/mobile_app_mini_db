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

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {
    private List<ShowtimeItem> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ShowtimeItem item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<ShowtimeItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        ShowtimeItem item = items.get(position);
        holder.tvMovieTitle.setText(item.movieTitle);
        holder.tvTheaterName.setText(item.theaterName);
        holder.tvTime.setText(item.time);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle, tvTheaterName, tvTime;
        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvTheaterName = itemView.findViewById(R.id.tvTheaterName);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    public static class ShowtimeItem {
        public int showtimeId;
        public String movieTitle;
        public String theaterName;
        public String time;

        public ShowtimeItem(int showtimeId, String movieTitle, String theaterName, String time) {
            this.showtimeId = showtimeId;
            this.movieTitle = movieTitle;
            this.theaterName = theaterName;
            this.time = time;
        }
    }
}
