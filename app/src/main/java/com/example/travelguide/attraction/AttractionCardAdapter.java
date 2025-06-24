package com.example.travelguide.attraction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelguide.R;

import java.util.List;

public class AttractionCardAdapter extends RecyclerView.Adapter<AttractionCardAdapter.CardViewHolder> {

    public interface OnAddToPlanClickListener {
        void onAddToPlanClicked(AttractionLocation attraction);
    }

    private List<AttractionLocation> attractions;
    private OnAddToPlanClickListener onAddToPlanClickListener;

    public AttractionCardAdapter(List<AttractionLocation> attractions, OnAddToPlanClickListener listener) {
        this.attractions = attractions;
        this.onAddToPlanClickListener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attraction_horizontal_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        AttractionLocation attraction = attractions.get(position);
        holder.bind(attraction);
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private ImageView attractionImage;
        private TextView attractionChineseName;
        private TextView attractionEnglishName;
        private TextView attractionRating;
        private TextView attractionDistance;
        private Button addToPlanButton;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            attractionImage = itemView.findViewById(R.id.attraction_image);
            attractionChineseName = itemView.findViewById(R.id.attraction_chinese_name);
            attractionEnglishName = itemView.findViewById(R.id.attraction_english_name);
            attractionRating = itemView.findViewById(R.id.attraction_rating);
            attractionDistance = itemView.findViewById(R.id.attraction_distance);
            addToPlanButton = itemView.findViewById(R.id.add_to_plan_button);
        }

        public void bind(AttractionLocation attraction) {
            // 设置景点信息
            attractionImage.setImageResource(attraction.getImageResId());
            attractionChineseName.setText(attraction.getChineseName());
            attractionEnglishName.setText(attraction.getEnglishName());
            attractionRating.setText("★ " + attraction.getRating());
            attractionDistance.setText("📍 " + attraction.getDistance());

            // 设置按钮点击事件
            addToPlanButton.setOnClickListener(v -> {
                if (onAddToPlanClickListener != null) {
                    onAddToPlanClickListener.onAddToPlanClicked(attraction);
                }
            });
        }
    }
}