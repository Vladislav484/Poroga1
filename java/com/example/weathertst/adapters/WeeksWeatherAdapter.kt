package com.example.weathertst.adapters

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weathertst.model.weeksWeather.ItemWeekWeather
import kotlinx.android.synthetic.main.week_weather_item.view.*
import java.text.SimpleDateFormat
import java.util.*


class WeeksWeatherAdapters(val weeksWeather: List<ItemWeekWeather>, val context: Context) :
    RecyclerView.Adapter<WeeksWeatherAdapters.WeeksWeatherViewHolder>() {

    class WeeksWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeksWeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(com.example.weathertst.R.layout.week_weather_item, parent, false)
        return WeeksWeatherViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: WeeksWeatherViewHolder, position: Int) {
        val week = weeksWeather[position]

        holder.itemView.apply {

            val dateNow = Date(week.dt.toLong() * 1000)
            val dt1 = SimpleDateFormat("EEEE dd.MM")
            date.text = dt1.format(dateNow)

            val mDrawableName = "ic_" + week.weather[0].icon
            val resID = context.resources.getIdentifier(
                mDrawableName,
                "drawable",
                com.example.weathertst.utils.PACKAGE_NAME
            )

            image_weather.setImageResource(resID)

            temp.text = "Температура " + week.temp.day.toString() + "°С"
            temp_feel.text = "Ощущается как " + week.feels_like.day.toString() + "°С"
            pressure.text = "Давление " + week.pressure.toString() + " гПа"
            humidity.text = "Влажность " + week.humidity.toString() + " %"
            windSpeed.text = "Скорость ветра " + week.speed.toString() + " м/с"

        }
    }

    override fun getItemCount(): Int {
        return weeksWeather.size
    }

}