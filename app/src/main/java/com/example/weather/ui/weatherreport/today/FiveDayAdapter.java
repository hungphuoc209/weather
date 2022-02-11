package com.example.weather.ui.weatherreport.today;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.dailyweather.DailyWeather;
import com.example.weather.ultils.ConvertUnit;
import com.squareup.picasso.Picasso;

import java.util.List;


class FiveDayAdapter extends RecyclerView.Adapter<FiveDayAdapter.Next4daysHolder> {
    private final List<DailyWeather> dailyWeathers;
    private final Context context;
    private final onItemClickListener onItemClickListener;

    public FiveDayAdapter(Context context, onItemClickListener onItemClickListener, List<DailyWeather> dailyWeathers) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.dailyWeathers = dailyWeathers;
    }

    @NonNull
    @Override
    public Next4daysHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fiveday_item_view, parent, false);
        return new Next4daysHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Next4daysHolder holder, int position) {
        holder.onBind(dailyWeathers.get(position));
    }

    @Override
    public int getItemCount() {
        return dailyWeathers.size();
    }

    public class Next4daysHolder extends RecyclerView.ViewHolder {
        //ui
        TextView tvWeekdayItem, tvMinTempItem, tvMaxTempItem, tvTempItem;
        ImageView imgWeatherItem;
        CardView cvDaily;

        public Next4daysHolder(@NonNull View itemView) {
            super(itemView);
            initView();
            initListener();
        }

        private void initView() {
            tvTempItem = itemView.findViewById(R.id.tvTempItem);
            tvMaxTempItem = itemView.findViewById(R.id.tvMaxTempItem);
            tvMinTempItem = itemView.findViewById(R.id.tvMinTempItem);
            tvWeekdayItem = itemView.findViewById(R.id.tvWeekdayItem);
            imgWeatherItem = itemView.findViewById(R.id.imgWeatherItem);
            cvDaily = itemView.findViewById(R.id.cvDailyItem);
        }

        private void initListener() {
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition()));
        }

        private void onBind(DailyWeather dailyWeather) {
            tvTempItem.setText(String.format(context.getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getDay())));
            tvMaxTempItem.setText(String.format(context.getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getMax())));
            tvMinTempItem.setText(String.format(context.getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getMin())));
            tvWeekdayItem.setText(ConvertUnit.getDateFromFt(dailyWeather.getDt(), "EEEE"));
            Picasso.get().load(ConvertUnit.iconPath(dailyWeather.getWeather().get(0).getIcon())).into(imgWeatherItem);
            cvDaily.setCardBackgroundColor(getColorBg(getAdapterPosition()));
        }

        private int getColorBg(int position) {
            int colorId;
            switch (position) {
                case 0:
                    colorId = ContextCompat.getColor(context, R.color.holo_light_sky_blue);
                    break;
                case 1:
                    colorId = context.getResources().getColor(R.color.holo_pink);
                    break;
                case 2:
                    colorId = context.getResources().getColor(R.color.holo_orange);
                    break;
                case 3:
                    colorId = context.getResources().getColor(R.color.holo_red);
                    break;
                default:
                    colorId = context.getResources().getColor(R.color.holo_dark_sky_blue);
                    break;
            }
            return colorId;
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }
}


