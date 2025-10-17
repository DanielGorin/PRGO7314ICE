package com.example.prog7314ices.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> CurrentWeatherFragment()     // TODAY
        1 -> DailyForecastsFragment()     // 5-DAY
        2 -> CityWeatherFragment()        // CITY WEATHER
        else -> throw IndexOutOfBoundsException("Invalid tab index: $position")
    }

    // Optional helper for titles (used in MainActivity with TabLayoutMediator)
    fun getTabTitle(position: Int): String = when (position) {
        0 -> "TODAY"
        1 -> "5-DAY FORECAST"
        2 -> "CITY WEATHER"
        else -> ""
    }
}
