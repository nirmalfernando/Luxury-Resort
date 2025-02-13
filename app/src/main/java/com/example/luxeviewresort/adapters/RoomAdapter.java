package com.example.luxeviewresort.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxeviewresort.R;
import com.example.luxeviewresort.models.Room;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private static final String TAG = "RoomAdapter";
    private Context context;
    private List<Room> roomList;
    private OnRoomClickListener onRoomClickListener;

    public interface OnRoomClickListener {
        void onRoomClick(int roomId);
    }

    public RoomAdapter(Context context, List<Room> roomList, OnRoomClickListener listener) {
        this.context = context;
        this.roomList = roomList;
        this.onRoomClickListener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.roomTitle.setText(room.getName());
        holder.roomPrice.setText("$" + room.getPrice() + " per night");

        // Handle the room image
        Bitmap roomImage = room.getImage();
        if (roomImage != null) {
            // If we have an image, display it
            holder.roomImage.setImageBitmap(roomImage);
            holder.roomImage.setVisibility(View.VISIBLE);
            Log.d(TAG, "Setting image for room: " + room.getName());
        } else {
            // If no image is available, set a default placeholder
            holder.roomImage.setImageResource(R.drawable.room1);
            Log.d(TAG, "Using default image for room: " + room.getName());
        }


        holder.btnBook.setOnClickListener(v -> onRoomClickListener.onRoomClick(room.getId()));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomTitle, roomPrice;
        Button btnBook;
        ImageView roomImage;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomTitle = itemView.findViewById(R.id.roomTitle);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            btnBook = itemView.findViewById(R.id.btnBookRoom);
            roomImage = itemView.findViewById(R.id.roomImage);
        }
    }
}