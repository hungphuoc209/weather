package com.example.weather.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.daysweather.HourlyWeather;
import com.example.weather.ultils.ConvertUnit;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.HourlyHolder> {
    private final List<HourlyWeather> hourlyWeathers;
    private final Context context;

    public DetailAdapter(List<HourlyWeather> hourlyWeathers, Context context) {
        this.hourlyWeathers = hourlyWeathers;
        this.context = context;
    }

    @NonNull
    @Override
    public HourlyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.hour_item_view, parent, false);
        return new HourlyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyHolder holder, int position) {
        holder.onBind(hourlyWeathers.get(position));

    }

    @Override
    public int getItemCount() {
        return hourlyWeathers.size();
    }

    public class HourlyHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTemp;
        ImageView imgWeatherIcon;

        public HourlyHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tvTime = itemView.findViewById(R.id.tvTimeDetailItem);
            tvTemp = itemView.findViewById(R.id.tvTempDetailItem);
            imgWeatherIcon = itemView.findViewById(R.id.imgWeatherIconDetailItem);
        }

        public void onBind(HourlyWeather hourlyWeather) {
            tvTime.setText(ConvertUnit.getDateFromFt(hourlyWeather.getDt(), "HH:mm"));
            tvTemp.setText(String.format(context.getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(hourlyWeather.getMain().getTemp())));
            Picasso.get().load(ConvertUnit.iconPath(hourlyWeather.getWeather().get(0).getIcon())).into(imgWeatherIcon);
        }
    }
}

