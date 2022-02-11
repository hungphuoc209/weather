package com.example.weather.ui.weatherreport.nextweek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.dailyweather.DailyWeather;
import com.example.weather.ultils.ConvertUnit;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyHolder> {
    private final List<DailyWeather> dailyWeathers;
    private final Context context;

    public DailyAdapter(Context context, List<DailyWeather> dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.daily_item_view, parent, false);
        return new DailyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyHolder holder, int position) {
        holder.onBind(dailyWeathers.get(position));

    }

    @Override
    public int getItemCount() {
        return dailyWeathers.size();
    }

    public class DailyHolder extends RecyclerView.ViewHolder {
        TextView tvWeekDay, tvDay, tvTempMax, tvTempMin;
        ImageView imgWeatherIcon;

        public DailyHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tvWeekDay = itemView.findViewById(R.id.tvWeekday);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTempMax = itemView.findViewById(R.id.tvTempMax);
            tvTempMin = itemView.findViewById(R.id.tvTempMin);
            imgWeatherIcon = itemView.findViewById(R.id.imgWeatherIcon);
        }

        public void onBind(DailyWeather dailyWeather) {
            tvWeekDay.setText(ConvertUnit.getDateFromFt(dailyWeather.getDt(), "EEEE"));
            tvDay.setText(ConvertUnit.getDateFromFt(dailyWeather.getDt(), "MMM dd"));
            tvTempMax.setText(String.format(context.getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getMax())));
            tvTempMin.setText(String.format(context.getString(R.string.temp_C_format), ConvertUnit.fahrenheitToCelsius(dailyWeather.getTemp().getMin())));
            Picasso.get().load(ConvertUnit.iconPath(dailyWeather.getWeather().get(0).getIcon())).into(imgWeatherIcon);
        }
    }
}
